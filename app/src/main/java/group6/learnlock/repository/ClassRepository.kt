package group6.learnlock.repository

import androidx.annotation.WorkerThread
import group6.learnlock.room.ClassDao
import group6.learnlock.model.Class
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ClassRepository(private val classDao: ClassDao) {
    val allClasses: Flow<List<Class>> = classDao.getClasses()

    @WorkerThread
    suspend fun insert(newClass: Class) {
        classDao.insertClass(newClass)
    }

    @WorkerThread
    suspend fun update(cl: Class){
        classDao.update(cl)
    }

    @WorkerThread
    suspend fun delete(className: String) {
        classDao.deleteClass(className)
    }

    @WorkerThread
    suspend fun deleteAll() {
        classDao.deleteAllEntries()
    }

    @WorkerThread
    fun getClassesForDate(selectedDate: Long): Flow<List<Class>> {
        return classDao.getClassesForDate(selectedDate)
    }
}