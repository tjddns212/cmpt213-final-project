package group6.learnlock.ui.calender

import java.time.LocalDate
import java.time.LocalTime

class Classes(private var name: String, private var date: LocalDate, private var time: LocalTime) {
    companion object  {
        val classList: ArrayList<Classes> = ArrayList()

        fun classesOnDate(date: LocalDate): ArrayList<Classes> {
            val classes = ArrayList<Classes>()
            for (currentClass in classList) {
                if (currentClass.getDate() == date) {
                    classes.add(currentClass)
                }
            }
            return classes
        }
    }

    fun getName(): String {
        return name
    }

    fun setName(input: String) {
        this.name = input
    }

    fun getDate(): LocalDate {
        return date
    }

    fun setDate(input: LocalDate) {
        this.date = input
    }

    fun getTime(): LocalTime {
        return time
    }
    fun setTime(input: LocalTime) {
        this.time = input
    }
}