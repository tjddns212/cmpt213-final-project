package group6.learnlock.ui.flashcards

data class Flashcard(
    var title: String,
    var description: String,
    var backgroundColor: Int // Use color resource IDs (e.g., R.color.blue)
)