package group6.learnlock.ui.calender

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.compose.ui.text.intl.Locale
import group6.learnlock.R
import group6.learnlock.model.DaySchedule
import group6.learnlock.repository.ClassRepository
import group6.learnlock.room.ClassDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import group6.learnlock.model.Class
import group6.learnlock.room.ClassDao
import java.time.DayOfWeek
import java.util.Calendar
import java.util.Date

class AddClassActivity : AppCompatActivity() {

    private lateinit var classNameEditText: EditText
    private lateinit var mondayCheckBox: CheckBox
    private lateinit var tuesdayCheckBox: CheckBox
    private lateinit var wednesdayCheckBox: CheckBox
    private lateinit var thursdayCheckBox: CheckBox
    private lateinit var fridayCheckBox: CheckBox
    private lateinit var startDatePicker: DatePicker
    private lateinit var endDatePicker: DatePicker
    private lateinit var layoutTimePickers: LinearLayout
    private lateinit var buttonCreateClass: Button
    private lateinit var buttonCancel: Button

    private lateinit var database:  ClassDatabase
    private lateinit var classRepository: ClassRepository
    private lateinit var databaseDao: ClassDao

    private lateinit var timePickers: HashMap<String, Pair<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_class)

        classNameEditText = findViewById(R.id.editTextClassName)
        mondayCheckBox = findViewById(R.id.checkBoxMonday)
        tuesdayCheckBox = findViewById(R.id.checkBoxTuesday)
        wednesdayCheckBox = findViewById(R.id.checkBoxWednesday)
        thursdayCheckBox = findViewById(R.id.checkBoxThursday)
        fridayCheckBox = findViewById(R.id.checkBoxFriday)

        startDatePicker = findViewById(R.id.startDatePicker)
        endDatePicker = findViewById(R.id.endDatePicker)

        layoutTimePickers = findViewById(R.id.layoutTimePickers)
        timePickers = HashMap()

        buttonCreateClass = findViewById(R.id.buttonCreateClass)
        buttonCancel = findViewById(R.id.buttonCancel)


        database = ClassDatabase.getInstance(this)
        databaseDao = database.classDao
        classRepository = ClassRepository(databaseDao)

        mondayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addTimePicker("Mon")
            } else {
                removeTimePicker("Mon")
            }
        }

        tuesdayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addTimePicker("Tue")
            } else {
                removeTimePicker("Tue")
            }
        }

        wednesdayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addTimePicker("Wed")
            } else {
                removeTimePicker("Wed")
            }
        }

        thursdayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addTimePicker("Thu")
            } else {
                removeTimePicker("Thu")
            }
        }

        fridayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addTimePicker("Fri")
            } else {
                removeTimePicker("Fri")
            }
        }


        buttonCreateClass.setOnClickListener {
            createClass()
        }

        buttonCancel.setOnClickListener {
            finish() // Close the activity
        }
    }

    private fun createClass() {
        val className = classNameEditText.text.toString()
        val daysOfWeek = getSelectedDaysOfWeek()

        if (className.isNotEmpty() && daysOfWeek.isNotEmpty()) {
            val daySchedules = mutableListOf<DaySchedule>()

            for (day in daysOfWeek) {
                val startTime = timePickers[day]?.first ?: ""
                val endTime = timePickers[day]?.second ?: ""
                var dayString:String = ""
                if (day == "Mon") {
                    dayString = "MONDAY"
                }
                else if (day == "Tue") {
                    dayString = "TUESDAY"
                }
                else if (day == "Wed") {
                    dayString = "WEDNESDAY"
                }
                else if (day == "Thu") {
                    dayString = "THURSDAY"
                }
                else if (day == "Fri") {
                    dayString = "FRIDAY"
                }

                daySchedules.add(
                    DaySchedule(
                    DayOfWeek.valueOf(dayString!!),
                        startTime,
                        endTime,
                        Date(),
                        Date()
                ))
            }
            val selectedStartDate = getDateFromDatePicker(startDatePicker)
            val selectedEndDate = getDateFromDatePicker(endDatePicker)
            val classInstance = Class(className, daySchedules, emptyList(), selectedStartDate, selectedEndDate)

            saveClassToDatabase(classInstance)
        } else {

        }
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }

    private fun getSelectedDaysOfWeek(): List<String> {
        val selectedDays = mutableListOf<String>()
        if (mondayCheckBox.isChecked) selectedDays.add("Mon")
        if (tuesdayCheckBox.isChecked) selectedDays.add("Tue")
        if (wednesdayCheckBox.isChecked) selectedDays.add("Wed")
        if (thursdayCheckBox.isChecked) selectedDays.add("Thu")
        if (fridayCheckBox.isChecked) selectedDays.add("Fri")

        return selectedDays
    }

    private fun saveClassToDatabase(classInstance: Class) {
        CoroutineScope(Dispatchers.IO).launch {
            classRepository.insert(classInstance)
            finish()
        }
    }

    private fun addTimePicker(day: String) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val startTime = String.format("%02d:%02d", hourOfDay, minute)
                timePickers[day] = Pair(startTime, "")

                // Open a TimePickerDialog for the end time after saving the start time
                addEndTimePicker(day, startTime)
            },
            0,
            0,
            true
        )

        timePickerDialog.show()
    }


    private fun addEndTimePicker(day: String, startTime: String) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val endTime = String.format("%02d:%02d", hourOfDay, minute)
                timePickers[day] = Pair(startTime, endTime)
            },
            0,
            0,
            true
        )

        timePickerDialog.show()
    }

    private fun removeTimePicker(day: String) {
        val timePickerToRemove = getTimePicker(day)

        if (timePickerToRemove != null) {
            timePickers.remove(day)
        }
    }

    private fun getTimePicker(day: String): TimePicker? {
        // Iterate through layoutTimePickers to find the TimePicker with the specified tag (day)
        for (i in 0 until layoutTimePickers.childCount) {
            val child = layoutTimePickers.getChildAt(i)
            if (child is TimePicker && child.tag == day) {
                return child
            }
        }
        return null
    }
}