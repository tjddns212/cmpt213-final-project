package group6.learnlock.ui.calender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.R

class AssignmentAdapter
    :RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {


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
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
    }

}