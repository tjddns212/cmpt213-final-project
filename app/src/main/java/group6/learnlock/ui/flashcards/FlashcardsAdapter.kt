package group6.learnlock.ui.flashcards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.databinding.FlashcardItemBinding
import group6.learnlock.databinding.FlashcardItemBinding.inflate as inflate1

class FlashcardsAdapter(private val flashcards: List<String>) :
    RecyclerView.Adapter<FlashcardsAdapter.FlashcardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val binding = inflate1(LayoutInflater.from(parent.context), parent, false)
        return FlashcardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val item = flashcards[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = flashcards.size

    inner class FlashcardViewHolder(private val binding: FlashcardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(text: String) {
            binding.flashcardText.text = text
            // Set your item data here
        }
    }
}
