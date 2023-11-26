package group6.learnlock.ui.calender

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.AssignmentApplication
import group6.learnlock.databinding.FragmentCalenderBinding





class CalenderFragment : Fragment() {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView : RecyclerView
    lateinit var assignmentAdapter: AssignmentAdapter
    lateinit var calenderViewModel:CalenderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView=binding.recyclerView
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        assignmentAdapter = AssignmentAdapter()
        recyclerView.adapter=assignmentAdapter
        val viewModelFactory=CalendarViewModelFactory((requireActivity().application as AssignmentApplication).repository)
        calenderViewModel=ViewModelProvider(this,viewModelFactory).get(CalenderViewModel::class.java)
        calenderViewModel.myAllAssignments.observe(viewLifecycleOwner, Observer{ assignments->
            assignmentAdapter.setAssignment(assignments)
        })


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}