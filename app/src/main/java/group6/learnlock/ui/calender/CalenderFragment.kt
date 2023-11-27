package group6.learnlock.ui.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.databinding.FragmentCalenderBinding

class gitCalenderFragment : Fragment() {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView : RecyclerView
    lateinit var adapter: AssignmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView=binding.recycleView


        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        adapter = AssignmentAdapter()
        recyclerView.adapter=adapter



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}