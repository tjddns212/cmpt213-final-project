package group6.learnlock.ui.calender

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarUtils {
    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        public fun monthYear(date: LocalDate): String {
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun thisWeek(selectedDate: LocalDate): ArrayList<LocalDate> {
            val days = ArrayList<LocalDate>()
            var current = sundayForDate(selectedDate)
            val end = current!!.plusWeeks(1)

            while (current!!.isBefore(end)) {
                days.add(current)
                current = current.plusDays(1)
            }
            return days
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun sundayForDate(current: LocalDate): LocalDate? {
            var temp = current
            var weekAgo = current.minusWeeks(1)

            while(temp.isAfter(weekAgo)) {
                if (temp.dayOfWeek == DayOfWeek.SUNDAY) {
                    return temp
                }
                temp = temp.minusDays(1)
            }
            return null
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formattedTime(time: LocalTime): String {
            val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
            return time.format(formatter)
        }


        var selectedDate: LocalDate? = null
    }
    //val selectedDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun formattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun monthArray(date: LocalDate): ArrayList<LocalDate?> {
        val days = ArrayList<LocalDate?>()
        val yearMonth = YearMonth.from(date)

        val monthLength = yearMonth.lengthOfMonth()

        val firstOfMonth = selectedDate?.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth?.dayOfWeek?.value ?:0

        for (index in 1..42) {
            if (index <= dayOfWeek || index > monthLength + dayOfWeek) {
                days.add(null)
            }
            else {
                days.add(LocalDate.of(selectedDate!!.year, selectedDate!!.month, index - dayOfWeek))
            }
        }
        return days
    }
}