package group6.learnlock.ui.calender

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


class GoogleCalenderFragment : Fragment() {

    private var _binding: FragmentGoogleCalendarBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView
    lateinit var assignmentAdapter: AssignmentAdapter
    lateinit var assignmentViewModel: AssignmentViewModel
//    lateinit var calendarView: CalenderView
    lateinit var integrateButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        integrateButton=binding.integrateButton
//        calendarView = binding.calendarView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        assignmentAdapter = AssignmentAdapter()
        recyclerView.adapter = assignmentAdapter

        val viewModelFactory = CalendarViewModelFactory((requireActivity().application as AssignmentApplication).repository)
        assignmentViewModel = ViewModelProvider(this, viewModelFactory).get(AssignmentViewModel::class.java)

        assignmentViewModel.myAllAssignments.observe(viewLifecycleOwner, Observer { assignments ->
            assignmentAdapter.setAssignment(assignments)
        })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
