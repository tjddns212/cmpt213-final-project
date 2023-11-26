package group6.learnlock.ui.calender

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(CalenderViewModel::class.java)

        CalendarUtils.selectedDate = LocalDate.now()


        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val lastWeekBtn: Button = root.findViewById(R.id.lastWeekBtn)
        val nextWeekBtn: Button = root.findViewById(R.id.nextWeekBtn)

        lastWeekBtn.setOnClickListener {
            previousWeek(it)
        }

        nextWeekBtn.setOnClickListener {
            nextWeek(it)
        }


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
    fun previousWeek(view: View) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextWeek(view: View) {
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