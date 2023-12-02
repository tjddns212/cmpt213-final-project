package group6.learnlock.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity(tableName = "classes")
data class Class (
    @PrimaryKey
    val className: String,
    @ColumnInfo(name = "start_time_column")
    val startTime: String,
    @ColumnInfo(name = "end_time_column")
    val endTime: String,
    @ColumnInfo(name = "days_of_week_column")
    val daysOfWeek: List<String>,
    @ColumnInfo(name = "start_date_column")
    val startDate: Date,
    @ColumnInfo(name = "end_date_column")
    val endDate: Date,
    @ColumnInfo(name = "assignment_column")
    val assignments: List<Assignment>
)