package group6.learnlock.ui.calender

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.R
import group6.learnlock.databinding.FragmentCalenderBinding

import group6.learnlock.ui.calender.CalendarUtils
import java.time.LocalDate
import java.util.Calendar


class CalenderFragment : Fragment(), CalendarAdapter.OnItemListener {

    private var _binding: FragmentCalenderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var classList: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(CalenderViewModel::class.java)

        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initWidgets(root)
        setWeekView()

        return root
    }

    private fun initWidgets(root: View) {
        calendarRecyclerView = root.findViewById(R.id.calendarRecyclerView)
        monthYearText = root.findViewById(R.id.monthYearTV)
        classList = root.findViewById(R.id.eventListView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setWeekView() {
        monthYearText.text = CalendarUtils.selectedDate?.let { CalendarUtils.monthYear(it) }
        val days = CalendarUtils.selectedDate?.let { CalendarUtils.thisWeek(it) }
        val calendarAdapter = days?.let { CalendarAdapter(it, this) }
        val layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
        setEventAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate) {
        CalendarUtils.selectedDate = date
        setWeekView()
    }

    private fun setEventAdapter() {
        val classToday = CalendarUtils.selectedDate?.let { Classes.classesOnDate(it) }
        val eventAdapter = classToday?.let { ClassAdapter(requireContext(), it) }
        classList.adapter = eventAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousWeekAction(view: View) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextWeekAction(view: View) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusWeeks(1)
        setWeekView()
    }

    fun newClassAction(view: View) {
        startActivity(Intent(requireContext(), ClassEditActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        setEventAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}