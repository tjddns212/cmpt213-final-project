package group6.learnlock.ui.calender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.R
import group6.learnlock.model.Class
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClassAdapter: RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {
    fun getSelectedClasses(): List<Class> {
        return classes.filterIndexed { index, _ -> selectedPositions.contains(index) }
    }
    private var classes: List<Class> = ArrayList()
    private val selectedPositions = mutableSetOf<Int>()

    class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val classNameTextView: TextView = itemView.findViewById(R.id.courseTitleTextView)
        val startTimeTextView: TextView =itemView.findViewById(R.id.dueTimeTextView)
        val endTimeTextView: TextView =itemView.findViewById(R.id.dueDateTextView)
        val description: TextView =itemView.findViewById(R.id.descriptionTitleTextView)
        val cardView: CardView =itemView.findViewById(R.id.cardView)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ClassAdapter.ClassViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.card_design,parent,false)
        return ClassAdapter.ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassAdapter.ClassViewHolder, position: Int) {
        val currentClass = classes[position]
        holder.classNameTextView.text = currentClass.className

        val formatterTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formatterDate = SimpleDateFormat("dd MMMM yyy", Locale.getDefault())

        val firstSchedule = currentClass.schedules?.first()
        if (firstSchedule != null) {
            val startTime = firstSchedule.startTime
            val endTime = firstSchedule.endTime
            val startDate = formatterDate.format(firstSchedule.startDate)
            val endDate = formatterDate.format(firstSchedule.endDate)
            holder.description.text = "$startDate - $endDate"

            if (startTime != null && endTime != null) {
                //val startString = formatterTime.format(startTime)
                //val endString = formatterTime.format(endTime)
                holder.startTimeTextView.text = startTime
                holder.endTimeTextView.text = endTime
            } else {
                // Handle null start or end time
                holder.startTimeTextView.text = "N/A"
                holder.endTimeTextView.text = "N/A"
            }
        }
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

    override fun getItemCount(): Int {
        return classes.size
    }

    fun setClasses(classes: List<Class>) {
        this.classes = classes
        notifyDataSetChanged()
    }
    fun clearSelection() {
        selectedPositions.clear()
        notifyDataSetChanged() // Refresh the UI to reflect the change
    }

}