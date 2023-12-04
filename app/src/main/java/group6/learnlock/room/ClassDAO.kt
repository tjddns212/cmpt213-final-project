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

    @Query("SELECT * FROM class_table WHERE (start_column <= :selectedDate AND end_column >= :selectedDate) AND" +
            "(:selectedDayOfWeek = 1 AND mon_column = 1 OR " +
            ":selectedDayOfWeek = 2 and tue_column = 1 OR " +
            ":selectedDayOfWeek = 3 and wed_column = 1 OR " +
            ":selectedDayOfWeek = 4 and thu_column = 1 OR " +
            ":selectedDayOfWeek = 5 and fri_column = 1)")
    fun getClassesForDate(selectedDate: Long, selectedDayOfWeek: Int): Flow<List<Class>>
}