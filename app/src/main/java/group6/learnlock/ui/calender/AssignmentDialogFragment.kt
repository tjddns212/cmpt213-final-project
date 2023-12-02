package group6.learnlock.ui.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import group6.learnlock.R
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.AssignmentApplication
import group6.learnlock.model.Assignment
import group6.learnlock.repository.AssignmentRepository
import kotlinx.coroutines.launch

class AssignmentsDialogFragment : DialogFragment() {

    private lateinit var assignmentRepository: AssignmentRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var assignmentAdapter: AssignmentAdapter
    private lateinit var doneButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cancelButton: Button
    interface OnAssignmentsDeletedListener {
        fun onAssignmentsDeleted(deletedAssignmentIds: List<Int>)
    }

    var assignmentsDeletedListener: OnAssignmentsDeletedListener? = null

    companion object {
        fun newInstance(assignments: List<Assignment>): AssignmentsDialogFragment {
            val fragment = AssignmentsDialogFragment()
            val args = Bundle().apply {
                putSerializable("assignments", ArrayList(assignments))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_assignment_dialog, container, false)
        assignmentRepository = (requireActivity().application as AssignmentApplication).repository
        recyclerView = view.findViewById(R.id.assignmentsRecyclerView)
        doneButton = view.findViewById(R.id.doneButton)
        deleteButton = view.findViewById(R.id.deleteButton)
        cancelButton = view.findViewById(R.id.cancelButton)

        // Retrieve assignments from arguments
        val assignments = arguments?.getSerializable("assignments") as? ArrayList<Assignment> ?: arrayListOf()

        // Initialize the adapter and set it to the RecyclerView
        assignmentAdapter = AssignmentAdapter().apply {
            setAssignment(assignments)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = assignmentAdapter

        setupButtons()

        return view
    }

    private fun setupButtons() {
        doneButton.setOnClickListener { handleDoneAction() }
        deleteButton.setOnClickListener { handleDeleteAction() }
        cancelButton.setOnClickListener { dismiss() }
    }

    private fun handleDoneAction() {
        val selectedAssignments = assignmentAdapter.getSelectedAssignments()
       dismiss()
    }

    private fun handleDeleteAction() {
        val selectedAssignments = assignmentAdapter.getSelectedAssignments()
        val deletedIds = selectedAssignments.map { it.id }

        selectedAssignments.forEach { assignment ->
            lifecycleScope.launch {
                assignmentRepository.delete(assignment)
            }
        }

        // Notify the listener about deleted assignments
        assignmentsDeletedListener?.onAssignmentsDeleted(deletedIds)

        // Show a toast message
        Toast.makeText(context, "Assignment deleted", Toast.LENGTH_SHORT).show()

        // Close the fragment
        dismiss()
    }

}
