package group6.learnlock.ui.archive

import android.graphics.Color
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
import group6.learnlock.R
import group6.learnlock.databinding.FragmentArchiveBinding
import group6.learnlock.model.Assignment
import group6.learnlock.ui.calender.AssignmentAdapter
import group6.learnlock.ui.calender.AssignmentViewModel
import group6.learnlock.ui.calender.CalendarViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
    private lateinit var assignmentViewModel : AssignmentViewModel


    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val galViewModel = ViewModelProvider(this)[ArchiveViewModel::class.java]

        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        val root : View = binding.root
        val recyclerView : RecyclerView = binding.archiveRecycler
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        val adapter = AssignmentAdapter()

        val viewModelFactory = CalendarViewModelFactory((requireActivity().application as AssignmentApplication).repository)
        assignmentViewModel = ViewModelProvider(this, viewModelFactory).get(AssignmentViewModel::class.java)
        assignmentViewModel.getCompletedAssignments().observe(viewLifecycleOwner, Observer { assignments ->
            adapter.setAssignment(assignments)
        })


        recyclerView.adapter = adapter

        return root
    }
}