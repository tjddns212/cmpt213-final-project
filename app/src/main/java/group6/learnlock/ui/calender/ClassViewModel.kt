package group6.learnlock.ui.calender

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import group6.learnlock.model.Assignment
import group6.learnlock.model.Class
import group6.learnlock.repository.ClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ClassViewModel(private val repository: ClassRepository) : ViewModel() {
    val allClasses : LiveData<List<Class>> =repository.allClasses.asLiveData()

    fun insert(cl: Class) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(cl)
    }

    fun delete(cl: Class) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(cl.className)
    }
    fun update(cl: Class) = viewModelScope.launch(Dispatchers.IO){
        repository.update(cl)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }
    fun getClassesForDate(selectedDate: Date): LiveData<List<Class>> {
        return repository.getClassesForDate(selectedDate.time).asLiveData()
    }
}