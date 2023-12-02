package group6.learnlock.ui.archive

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.R
import group6.learnlock.databinding.FragmentArchiveBinding
import group6.learnlock.model.Assignment
import group6.learnlock.ui.calender.AssignmentAdapter

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
    private  var compAssignment : Array<Assignment> = emptyArray<Assignment>()

    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val galViewModel = ViewModelProvider(this)[ArchiveViewModel::class.java]

        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        val root : View = binding.root
        val recyclerView : RecyclerView = binding.archiveRecycler
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        compAssignment = arrayOf(Assignment("CMPT 362", "Final Project", 1234, Color.RED), Assignment("CMPT 340", "Final Project Stroke", 5678, Color.GREEN))
        val adapter = AssignmentAdapter()
        adapter.setAssignment(compAssignment.asList())

        recyclerView.adapter = adapter

        return root
    }
}