package group6.learnlock.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import group6.learnlock.model.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDAO {

    @Insert
    suspend fun insert(assignment: Assignment)

    @Update
    suspend fun update(assignment: Assignment)

    @Delete
    suspend fun delete(assignment: Assignment)

    @Query("DELETE FROM assignment_table")
    suspend fun deleteAllAssignments()

    @Query("SELECT * FROM assignment_table ORDER BY id ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

}