package group6.learnlock.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import group6.learnlock.model.Class

@Dao
interface ClassDao {
    @Insert
    suspend fun insertClass(newClass: Class)

    @Query("SELECT * FROM classes")
    suspend fun getAllClasses(): List<Class>
}