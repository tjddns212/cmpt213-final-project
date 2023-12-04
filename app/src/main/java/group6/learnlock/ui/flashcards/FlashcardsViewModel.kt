package group6.learnlock.ui.flashcards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlashcardsViewModel : ViewModel() {

    val flashcardsData = MutableLiveData<MutableList<Flashcard>>(mutableListOf())

    // Add other LiveData and functions to handle your data
}
