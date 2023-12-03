package group6.learnlock.repository

import android.graphics.Color
import androidx.annotation.WorkerThread
import group6.learnlock.model.Assignment
import group6.learnlock.room.AssignmentDAO
import kotlinx.coroutines.flow.Flow
import java.util.Random

class AssignmentRepository(private val assignmentDao:AssignmentDAO) {

    val myAllAssignments: Flow<List<Assignment>> = assignmentDao.getAllAssignments()

    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
    @WorkerThread
    suspend fun insert(assignment: Assignment){
        assignment.color = getRandomColor()
        assignmentDao.insert(assignment)
    }
    @WorkerThread
    suspend fun update(assignment: Assignment){
        assignmentDao.update(assignment)
    }
    @WorkerThread
    suspend fun delete(assignment: Assignment){
        assignmentDao.delete(assignment)
    }
    @WorkerThread
    suspend fun deleteAllAssignments(){
        assignmentDao.deleteAllAssignments()
    }

    fun getAssignmentsByIds(ids: List<Int>): Flow<List<Assignment>> {
        return assignmentDao.getAssignmentsByIds(ids)
    }
    @WorkerThread
    suspend fun markAssignmentAsCompleted(assignmentId: Int) {
        assignmentDao.markAsCompleted(assignmentId)
    }





}