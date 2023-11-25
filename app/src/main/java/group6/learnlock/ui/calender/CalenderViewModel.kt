package group6.learnlock.ui.calender

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import group6.learnlock.model.Assignment
import group6.learnlock.repository.AssignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalenderViewModel(private val repository: AssignmentRepository) : ViewModel() {

    val myAllAssignments : LiveData<List<Assignment>> =repository.myAllAssignments.asLiveData()

    fun insert(assignment: Assignment) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(assignment)
    }
    fun update(assignment: Assignment) = viewModelScope.launch(Dispatchers.IO){
        repository.update(assignment)
    }
    fun delete(assignment: Assignment) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(assignment)
    }
    fun deleteAllAssignments() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAllAssignments()
    }

}