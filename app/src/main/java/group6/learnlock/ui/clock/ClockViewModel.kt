package group6.learnlock.ui.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClockViewModel : ViewModel() {

    // Holds the remaining time in milliseconds
    var timeLeftInMillis: Long = 3600000 // 60 minutes by default

    // Indicates if the timer is currently running
    var isTimerRunning: Boolean = false

    // Saves the system time when the timer was paused/stopped
    var lastPauseTimeMillis: Long = 0L
}