package group6.learnlock.ui.calender

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
    fun getClassesForDate(selectedDate: Long, selectedDayOfWeek: Int): LiveData<List<Class>> {
        return repository.getClassesForDate(selectedDate, selectedDayOfWeek).asLiveData()
    }
}

class ClassViewModelFactory(private var classRepository: ClassRepository): ViewModelProvider.Factory{
    override fun <T:ViewModel> create(modelClass: java.lang.Class<T>):T{
        if(modelClass.isAssignableFrom(ClassViewModel::class.java)){
            return ClassViewModel(classRepository) as T
        }else{
            throw IllegalArgumentException("Unkown view model")
        }
    }

}