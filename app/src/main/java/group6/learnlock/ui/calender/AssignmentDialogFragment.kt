package group6.learnlock.ui.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import group6.learnlock.R
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.model.Assignment

class AssignmentsDialogFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var assignmentAdapter: AssignmentAdapter
    private lateinit var doneButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cancelButton: Button


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
        // Retrieve selected assignments and mark them as done
        val selectedAssignments = assignmentAdapter.getSelectedAssignments()
        // TODO: Implement the logic to mark these assignments as done
        dismiss()
    }

    private fun handleDeleteAction() {
        // Retrieve selected assignments and delete them
        val selectedAssignments = assignmentAdapter.getSelectedAssignments()
        // TODO: Implement the logic to delete these assignments
        dismiss()
    }

}
