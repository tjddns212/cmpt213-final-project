package group6.learnlock.ui.calender

import android.util.Log
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
        Log.d("test", "in OnBind")
        var currentAssignment: Assignment = assignments[position]
        holder.courseTitleTextView.text=currentAssignment.course
        holder.descriptionTextView.text=currentAssignment.description
        holder.cardView.setCardBackgroundColor(currentAssignment.color)

        val formatterDate = SimpleDateFormat("MM-dd", Locale.getDefault())
        val dateString = formatterDate.format(Date(currentAssignment.dueDateTime))
        holder.dueDateTextView.text = dateString

        val formatterTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = formatterTime.format(Date(currentAssignment.dueDateTime))
        holder.dueTimeTextView.text = timeString
    }

    fun setAssignment(myAssignment: List<Assignment>){
        this.assignments=myAssignment
        notifyDataSetChanged()
    }

}