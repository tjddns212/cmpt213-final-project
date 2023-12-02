package group6.learnlock.repository

import androidx.room.Insert
import group6.learnlock.room.ClassDao
import group6.learnlock.model.Class
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ClassRepository(private val classDao: ClassDao) {
    val allClasses: Flow<List<Class>> = classDao.getClasses()

    fun insert(newClass: Class) {
        CoroutineScope(Dispatchers.IO).launch{
            classDao.insertClass(newClass)
        }
    }

    fun delete(className: String) {
        CoroutineScope(Dispatchers.IO).launch{
            classDao.deleteClass(className)
        }
    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch{
            classDao.deleteAllEntries()
        }
    }
}