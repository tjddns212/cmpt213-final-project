package group6.learnlock.ui.calender

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import group6.learnlock.R
import group6.learnlock.model.Assignment
import java.util.Calendar
import java.util.Random

class AddAssignmentDialogFragment : DialogFragment() {

    private var dueDate = Calendar.getInstance()

    interface AddAssignmentListener {
        fun onAssignmentAdded(assignment: Assignment)
    }

    private var listener: AddAssignmentListener? = null

    fun setAddAssignmentListener(listener: AddAssignmentListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_add_assignment_dialog, null)

        val buttonSelectDate = view.findViewById<Button>(R.id.buttonSelectDate)
        val buttonSelectTime = view.findViewById<Button>(R.id.buttonSelectTime)

        buttonSelectDate.setOnClickListener {
            showDatePicker()
        }

        buttonSelectTime.setOnClickListener {
            showTimePicker()
        }

        builder.setView(view)
            .setPositiveButton("Save") { dialog, id ->
                val course = view.findViewById<EditText>(R.id.editTextCourse).text.toString()
                val description = view.findViewById<EditText>(R.id.editTextDescription).text.toString()
                val dueDateTime = dueDate.timeInMillis

                // Generate a random color
                val random = Random()
                val color = android.graphics.Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

                val assignment = Assignment(course, description, dueDateTime, color)
                listener?.onAssignmentAdded(assignment)
            }


        return builder.create()
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            dueDate.set(Calendar.YEAR, year)
            dueDate.set(Calendar.MONTH, month)
            dueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }, dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH), dueDate.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
            dueDate.set(Calendar.MINUTE, minute)
        }, dueDate.get(Calendar.HOUR_OF_DAY), dueDate.get(Calendar.MINUTE), true)

        timePickerDialog.show()
    }

}
