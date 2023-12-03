package group6.learnlock.repository

import android.graphics.Color
import androidx.annotation.WorkerThread
import androidx.room.Insert
import group6.learnlock.model.Assignment
import group6.learnlock.room.ClassDao
import group6.learnlock.model.Class
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Random

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