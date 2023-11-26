package group6.learnlock.ui.flashcards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.databinding.FlashcardItemBinding



class FlashcardsAdapter(private val flashcards: List<Flashcard>) :
    RecyclerView.Adapter<FlashcardsAdapter.FlashcardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val binding = FlashcardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashcardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val flashcard = flashcards[position]
        with(holder.binding) {
            // Make sure you use the correct ID here
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, flashcard.backgroundColor))
            flashcardTitle.text = flashcard.title
            flashcardDescription.text = flashcard.description
        }
    }

    override fun getItemCount(): Int = flashcards.size

    class FlashcardViewHolder(val binding: FlashcardItemBinding) : RecyclerView.ViewHolder(binding.root)
}
