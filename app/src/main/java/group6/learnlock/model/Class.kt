package group6.learnlock.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.time.DayOfWeek
import java.util.Date
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat

@Entity(tableName = "class_table")
@TypeConverters(Converters::class)
data class Class (
    @PrimaryKey
    val className: String,
    @ColumnInfo(name = "schedules_column")
    val schedules: List<DaySchedule>?,
    @ColumnInfo(name = "assignment_column")
    val assignments: List<Assignment>,
    @ColumnInfo(name = "start_column")
    val startDate: Long,
    @ColumnInfo(name = "end_column")
    val endDate: Long,
    @ColumnInfo(name = "mon_column")
    val mon: Boolean,
    @ColumnInfo(name = "tue_column")
    val tue: Boolean,
    @ColumnInfo(name = "wed_column")
    val wed: Boolean,
    @ColumnInfo(name = "thu_column")
    val thu: Boolean,
    @ColumnInfo(name = "fri_column")
    val fri: Boolean
)

data class DaySchedule(
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime:String,
    val startDate: Date,
    val endDate: Date
)

class Converters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    @TypeConverter
    fun fromString(value: String?): List<DaySchedule>? {
        if (value == null) {
            return null
        }
        val listType: Type = object : TypeToken<List<DaySchedule>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<DaySchedule>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromAssignmentString(value: String?): List<Assignment>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Assignment>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toAssignmentString(list: List<Assignment>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Assignment>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        return dateString?.let { dateFormat.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun timestampToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

}