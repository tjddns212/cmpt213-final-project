package group6.learnlock.repository

import androidx.annotation.WorkerThread
import group6.learnlock.model.Assignment
import group6.learnlock.room.AssignmentDAO
import kotlinx.coroutines.flow.Flow

class AssignmentRepository(private val assignmentDao:AssignmentDAO) {

    val myAllAssignments: Flow<List<Assignment>> = assignmentDao.getAllAssignments()

    @WorkerThread
    suspend fun insert(assignment: Assignment){
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


}