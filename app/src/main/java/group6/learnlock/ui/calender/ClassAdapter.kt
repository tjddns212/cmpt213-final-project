package group6.learnlock.ui.calender

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import group6.learnlock.R

class ClassAdapter(context: Context, classes: List<Classes>): ArrayAdapter<Classes>(context, 0, classes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val selectedClass = getItem(position)
        var convertedView = convertView

        if (convertedView == null) {
            convertedView = LayoutInflater.from(context).inflate(R.layout.class_cell, parent, false)
        }

        val classCellTV: TextView = convertedView!!.findViewById(R.id.classCellTV)
        val className = "${selectedClass!!.getName()} ${CalendarUtils.formattedTime(selectedClass.getTime())}"
        classCellTV.text = className

        return convertedView
    }

}