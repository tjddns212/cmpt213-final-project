package group6.learnlock.ui.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClockViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is clock/app block Fragment"
    }
    val text: LiveData<String> = _text
}