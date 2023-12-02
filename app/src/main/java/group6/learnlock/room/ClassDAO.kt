package group6.learnlock.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import group6.learnlock.model.Class
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {
    @Insert
    suspend fun insertClass(newClass: Class)

    @Query("DELETE FROM classes WHERE className = :key")
    suspend fun deleteClass(key: String)

    @Query("DELETE FROM classes")
    suspend fun deleteAllEntries()

    @Query("SELECT * FROM classes")
    fun getClasses(): Flow<List<Class>>
}