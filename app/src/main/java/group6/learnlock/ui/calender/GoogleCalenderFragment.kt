package group6.learnlock.ui.calender

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import group6.learnlock.AssignmentApplication
import group6.learnlock.databinding.FragmentGoogleCalendarBinding
import group6.learnlock.model.Assignment
import java.util.Calendar
class GoogleCalenderFragment : Fragment(),AssignmentsDialogFragment.OnAssignmentsDeletedListener {
    private val calendarAssignmentIds: MutableMap<Long, MutableSet<Int>> = mutableMapOf()
    private var _binding: FragmentGoogleCalendarBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView
    lateinit var assignmentAdapter: AssignmentAdapter
    lateinit var assignmentViewModel: AssignmentViewModel
    lateinit var calendarView: CalendarView
    lateinit var integrateButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        integrateButton=binding.integrateButton
        calendarView = binding.calendarView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        assignmentAdapter = AssignmentAdapter()
        recyclerView.adapter = assignmentAdapter



        val viewModelFactory = CalendarViewModelFactory((requireActivity().application as AssignmentApplication).repository)
        assignmentViewModel = ViewModelProvider(this, viewModelFactory).get(AssignmentViewModel::class.java)

        assignmentViewModel.myAllAssignments.observe(viewLifecycleOwner, Observer { assignments ->
            assignmentAdapter.setAssignment(assignments)
        })
        integrateButton.setOnClickListener {
            val selectedAssignments = assignmentAdapter.getSelectedAssignments()
            addSelectedAssignmentsToCalendar(selectedAssignments)
        }

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val selectedDateInMillis = eventDay.calendar.timeInMillis
                val assignmentIdsForDate = calendarAssignmentIds[selectedDateInMillis] ?: setOf()

                assignmentViewModel.getAssignmentsByIds(assignmentIdsForDate).observe(viewLifecycleOwner, { assignments ->
                    if (assignments.isNotEmpty()) {
                        showAssignmentsDialog(assignments)
                    }
                })
            }
        })
        return root
    }
    private fun addSelectedAssignmentsToCalendar(assignments: List<Assignment>) {
        assignments.forEach { assignment ->
            // Convert the assignment due date to the start of that day in milliseconds
            val calendarTime = Calendar.getInstance().apply {
                timeInMillis = assignment.dueDateTime
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            // Add the assignment id to the calendarAssignmentIds map for the corresponding date
            calendarAssignmentIds.getOrPut(calendarTime) { mutableSetOf() }.add(assignment.id)
        }

        // Create events for each group of assignments and add them to the calendar
        val events = calendarAssignmentIds.mapNotNull { (calendarTime, assignmentIds) ->
            val assignmentsOnDay = assignmentIds.mapNotNull { id ->
                assignments.firstOrNull { it.id == id }
            }

            if (assignmentsOnDay.isNotEmpty()) {
                val drawable = createCompositeDrawable(assignmentsOnDay.mapNotNull { it.color })
                val calendar = Calendar.getInstance().apply { timeInMillis = calendarTime }
                EventDay(calendar, drawable)
            } else {
                null // Skip if no assignments found for the day
            }

        }

        calendarView.setEvents(events) // Set the events in the calendar
    }


    private fun createCompositeDrawable(colors: List<Int>): Drawable {
        val circleDiameter = 20
        val spaceBetweenMarks = 10

        val drawables = colors.map { color ->
            ShapeDrawable(OvalShape()).apply {
                paint.color = color
                intrinsicWidth = circleDiameter
                intrinsicHeight = circleDiameter
            }
        }.toTypedArray()

        val layerDrawable = LayerDrawable(drawables)

        for (i in drawables.indices) {
            // Adjusting left and right insets along with top inset for spacing
            val insetLeft = i * (circleDiameter + spaceBetweenMarks)
            val insetRight = (drawables.size - 1 - i) * (circleDiameter + spaceBetweenMarks)
            layerDrawable.setLayerInset(i, insetLeft, 0, insetRight, 0)
        }

        return layerDrawable
    }
    private fun showAssignmentsDialog(assignments: List<Assignment>) {
        val dialog = AssignmentsDialogFragment.newInstance(assignments)
        dialog.assignmentsDeletedListener = this
        dialog.show(childFragmentManager, "AssignmentsDialog")
    }
    override fun onAssignmentsDeleted(deletedAssignmentIds: List<Int>) {
        // Remove the deleted assignments from calendarAssignmentIds
        deletedAssignmentIds.forEach { id ->
            calendarAssignmentIds.forEach { (_, ids) ->
                ids.remove(id)
            }
        }

        // Remove any dates that no longer have assignments
        calendarAssignmentIds.entries.removeIf { (_, ids) -> ids.isEmpty() }
        assignmentAdapter.clearSelection()

        // Fetch the remaining assignments to update the calendar
        val remainingAssignments = assignmentViewModel.myAllAssignments.value?.filter {
            calendarAssignmentIds.values.flatten().contains(it.id)
        } ?: listOf()

        // Refresh the calendar with the remaining assignments
        refreshCalendarWithAssignments(remainingAssignments)

    }

    private fun refreshCalendarWithAssignments(assignments: List<Assignment>) {
        // Clear existing assignment IDs in the map
        calendarAssignmentIds.clear()

        // Re-populate the map with the remaining assignments
        addSelectedAssignmentsToCalendar(assignments)

        // Generate and set the events in the calendar based on the updated map
        val events = calendarAssignmentIds.mapNotNull { (calendarTime, assignmentIds) ->
            val assignmentsOnDay = assignmentIds.mapNotNull { id ->
                assignments.firstOrNull { it.id == id }
            }

            if (assignmentsOnDay.isNotEmpty()) {
                val drawable = createCompositeDrawable(assignmentsOnDay.mapNotNull { it.color })
                val calendar = Calendar.getInstance().apply { timeInMillis = calendarTime }
                EventDay(calendar, drawable)
            } else {
                null
            }
        }

        calendarView.setEvents(events)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}