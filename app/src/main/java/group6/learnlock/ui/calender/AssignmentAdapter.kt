package group6.learnlock.ui.calender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.R
import group6.learnlock.model.Assignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssignmentAdapter
    :RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {
    var assignments : List<Assignment> = ArrayList()
    private val selectedPositions = mutableSetOf<Int>()


    class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val courseTitleTextView: TextView = itemView.findViewById(R.id.courseTitleTextView)
        val descriptionTextView:TextView=itemView.findViewById(R.id.descriptionTitleTextView)
        val dueTimeTextView:TextView=itemView.findViewById(R.id.dueTimeTextView)
        val dueDateTextView:TextView=itemView.findViewById(R.id.dueDateTextView)
        val cardView:CardView=itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.card_design,parent,false)
        return AssignmentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return assignments.size
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        var currentAssignment: Assignment = assignments[position]
        holder.courseTitleTextView.text=currentAssignment.course
        holder.descriptionTextView.text= "Assignment: " + currentAssignment.description
        holder.cardView.setCardBackgroundColor(currentAssignment.color)

        val formatterDate = SimpleDateFormat("MM-dd", Locale.getDefault())
        val dateString = formatterDate.format(Date(currentAssignment.dueDateTime))
        holder.dueDateTextView.text = dateString

        val formatterTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = formatterTime.format(Date(currentAssignment.dueDateTime))
        holder.dueTimeTextView.text = timeString

        holder.itemView.alpha = if (selectedPositions.contains(position)) 0.5f else 1.0f
        holder.itemView.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
                holder.itemView.alpha = 1.0f
            } else {
                selectedPositions.add(position)
                holder.itemView.alpha = 0.5f
            }
        }
    }

    fun getSelectedAssignments(): List<Assignment> {
        return assignments.filterIndexed { index, _ -> selectedPositions.contains(index) }
    }
    fun setAssignment(myAssignment: List<Assignment>){
        this.assignments=myAssignment
        notifyDataSetChanged()
    }
    fun clearSelection() {
        selectedPositions.clear()
        notifyDataSetChanged() // Refresh the UI to reflect the change
    }


}