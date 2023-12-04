package group6.learnlock.ui.calender

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.R
import group6.learnlock.ui.calender.CalendarUtils.Companion.monthYear
import group6.learnlock.ui.calender.CalendarUtils.Companion.thisWeek
import java.time.LocalDate
/*
class CalendarActivity : Fragment() {

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
        recyclerView=binding.calendarRecyclerView


        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        adapter = AssignmentAdapter()
        recyclerView.adapter=adapter



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/