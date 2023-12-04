package group6.learnlock.ui.calender

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.AssignmentApplication
import group6.learnlock.databinding.FragmentGoogleCalendarBinding
import group6.learnlock.repository.ClassRepository
import group6.learnlock.room.ClassDao
import group6.learnlock.room.ClassDatabase
import java.util.Calendar

class GoogleCalenderFragment : Fragment() {

    private var _binding: FragmentGoogleCalendarBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView
    lateinit var assignmentAdapter: AssignmentAdapter
    lateinit var assignmentViewModel: AssignmentViewModel
    lateinit var classScheduleView: RecyclerView
    lateinit var classAdapter: ClassAdapter
    lateinit var classRepository: ClassRepository
    lateinit var classDatabase: ClassDatabase
    lateinit var classDao: ClassDao
    lateinit var classViewModelFactory: ClassViewModelFactory
    lateinit var classViewModel: ClassViewModel
    lateinit var calendarView: CalendarView
    lateinit var integrateButton : Button
    lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        integrateButton=binding.integrateButton
        addButton = binding.addClassButton
        calendarView = binding.calendarView
        classScheduleView = binding.classScheduleView
        classScheduleView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        assignmentAdapter = AssignmentAdapter()
        recyclerView.adapter = assignmentAdapter

        val viewModelFactory = CalendarViewModelFactory((requireActivity().application as AssignmentApplication).repository)
        assignmentViewModel = ViewModelProvider(this, viewModelFactory).get(AssignmentViewModel::class.java)

        classDatabase = ClassDatabase.getInstance(requireActivity())
        classDao = classDatabase.classDao
        classRepository = ClassRepository(classDao)
        classViewModelFactory = ClassViewModelFactory(classRepository)
        classViewModel = ViewModelProvider(this, classViewModelFactory).get(ClassViewModel::class.java)
        classAdapter = ClassAdapter()

        assignmentViewModel.myAllAssignments.observe(viewLifecycleOwner, Observer { assignments ->
            assignmentAdapter.setAssignment(assignments)
        })


        /*classViewModel.allClasses.observe(viewLifecycleOwner, Observer { classes ->
            classAdapter.setClasses(classes)
        })*/

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            if (year == 0 && month == 0 && dayOfMonth == 0) {
                openWeeklyView()
            } else {
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time
                classViewModel.getClassesForDate(selectedDate).observe(viewLifecycleOwner, Observer {classes ->
                    classAdapter.setClasses(classes)
                })
            }
        }

        addButton.setOnClickListener{
            val intent = Intent(requireContext(), AddClassActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun openWeeklyView() {
        val intent = Intent(requireContext(), WeeklyViewActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
