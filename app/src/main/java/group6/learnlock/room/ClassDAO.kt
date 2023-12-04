package group6.learnlock.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import group6.learnlock.model.Class
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ClassDao {
    @Insert
    suspend fun insertClass(newClass: Class)

    @Update
    suspend fun update(cl: Class)

    @Query("DELETE FROM class_table WHERE className = :key")
    suspend fun deleteClass(key: String)

    @Query("DELETE FROM class_table")
    suspend fun deleteAllEntries()

    @Query("SELECT * FROM class_table")
    fun getClasses(): Flow<List<Class>>

    @Query("SELECT * FROM class_table WHERE start_column <= :selectedDate AND end_column >= :selectedDate")
    fun getClassesForDate(selectedDate: Long): Flow<List<Class>>
}