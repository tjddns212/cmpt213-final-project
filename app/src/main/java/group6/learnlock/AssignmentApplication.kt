package group6.learnlock

import android.app.Application
import group6.learnlock.model.Assignment
import group6.learnlock.repository.AssignmentRepository
import group6.learnlock.room.AssignmentDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AssignmentApplication:Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database  by lazy { AssignmentDatabase.getDatabase(this,applicationScope)}
    val repository by lazy {AssignmentRepository(database.getAssignmentDao())}


}