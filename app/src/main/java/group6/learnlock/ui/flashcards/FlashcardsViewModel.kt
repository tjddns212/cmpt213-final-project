package group6.learnlock.ui.flashcards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlashcardsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is flash cards Fragment"
    }
    val text: LiveData<String> = _text
}