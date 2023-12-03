package group6.learnlock.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="assignment_table")
class Assignment(val course:String, val description:String, val dueDateTime: Long, var color: Int, var isCompleted: Boolean = false) {

    @PrimaryKey(autoGenerate = true)
    var id=0

}