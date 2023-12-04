package group6.learnlock.ui.calender

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import group6.learnlock.AssignmentApplication
import group6.learnlock.databinding.FragmentGoogleCalendarBinding
import group6.learnlock.model.Assignment
import java.util.Calendar
class GoogleCalenderFragment : Fragment(),AssignmentsDialogFragment.OnAssignmentsDeletedListener{
    private var calendarAssignmentIds: MutableMap<Long, MutableSet<Int>> = mutableMapOf()
    private var _binding: FragmentGoogleCalendarBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView
    lateinit var assignmentAdapter: AssignmentAdapter
    lateinit var assignmentViewModel: AssignmentViewModel
    lateinit var calendarView: CalendarView
    lateinit var integrateButton : Button
    lateinit var addButton: Button
    lateinit var classScheduleView: RecyclerView


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
        classScheduleView.layoutManager = LinearLayoutManager(requireContext())


        val viewModelFactory = CalendarViewModelFactory((requireActivity().application as AssignmentApplication).repository)
        assignmentViewModel = ViewModelProvider(this, viewModelFactory).get(AssignmentViewModel::class.java)

        assignmentViewModel.myAllAssignments.observe(viewLifecycleOwner, Observer { assignments ->
            assignmentAdapter.setAssignment(assignments)
        })
        assignmentViewModel.myAllAssignments.observe(viewLifecycleOwner, Observer { assignments ->
            // Update RecyclerView with active assignments
            val activeAssignments = assignments.filter { !it.isCompleted }
            assignmentAdapter.setAssignment(activeAssignments)

            // Filter active assignments that are already marked on the calendar
            val markedActiveAssignments = activeAssignments.filter { assignment ->
                calendarAssignmentIds.any { (_, assignmentIds) -> assignment.id in assignmentIds }
            }

            // Refresh the calendar with these assignments
            refreshCalendarWithAssignments(markedActiveAssignments)
            assignmentAdapter.clearSelection()

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

        loadAssignmentsFromSharedPreferences()
        updateCalendarEvents()

        return root
    }
    private fun addSelectedAssignmentsToCalendar(newAssignments: List<Assignment>) {
        newAssignments.forEach { assignment ->
            val calendarTime = Calendar.getInstance().apply {
                timeInMillis = assignment.dueDateTime
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            // Add the assignment id to the map for the corresponding date
            calendarAssignmentIds.getOrPut(calendarTime) { mutableSetOf() }.add(assignment.id)
        }

        // Create events and update the calendar view
        updateCalendarEvents()
        saveAssignmentsToSharedPreferences()
    }
    private fun updateCalendarEvents() {
        calendarAssignmentIds.forEach { (calendarTime, assignmentIds) ->
            assignmentViewModel.getAssignmentsByIds(assignmentIds).observe(viewLifecycleOwner, { assignments ->
                if (assignments.isNotEmpty()) {
                    val drawable = createCompositeDrawable(assignments.mapNotNull { it.color })
                    val calendar = Calendar.getInstance().apply { timeInMillis = calendarTime }
                    val eventDay = EventDay(calendar, drawable)
                    calendarView.setEvents(listOf(eventDay)) // This sets events for each individual day
                }
            })
        }
        // Note: This approach updates the calendar for each day separately.
        // If you want to update the calendar in one go, you'll need to accumulate the events and then set them.
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
        val fragmentManager = childFragmentManager
        val existingFragment = fragmentManager.findFragmentByTag("AssignmentsDialog")

        if (existingFragment == null) {
            val dialog = AssignmentsDialogFragment.newInstance(assignments)
            dialog.assignmentsDeletedListener = this
            dialog.show(fragmentManager, "AssignmentsDialog")
        }
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
        updateAndSaveCalendarAssignments(remainingAssignments)
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
    private fun saveAssignmentsToSharedPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("CalendarAssignments", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(calendarAssignmentIds)
        editor.putString("assignments", json)
        editor.apply()
    }

    private fun loadAssignmentsFromSharedPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("CalendarAssignments", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("assignments", null)
        json?.let {
            val type = object : TypeToken<MutableMap<Long, MutableSet<Int>>>() {}.type
            calendarAssignmentIds = Gson().fromJson(it, type)
        }
    }

    private fun updateAndSaveCalendarAssignments(newAssignments: List<Assignment>) {
        addSelectedAssignmentsToCalendar(newAssignments)
        saveAssignmentsToSharedPreferences()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}