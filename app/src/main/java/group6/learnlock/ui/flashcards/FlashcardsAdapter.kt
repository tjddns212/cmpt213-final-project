package group6.learnlock.ui.flashcards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import group6.learnlock.databinding.FlashcardItemBinding

class FlashcardsAdapter(
    private val flashcards: MutableList<Flashcard>,
    private val onItemClicked: (Flashcard, Int) -> Unit,
    private val onItemLongClicked: (Flashcard, Int) -> Unit
) : RecyclerView.Adapter<FlashcardsAdapter.FlashcardViewHolder>() {
    private val originalList = ArrayList(flashcards)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val binding = FlashcardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashcardViewHolder(binding, onItemClicked, onItemLongClicked)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val flashcard = flashcards[position]
        holder.bind(flashcard)
    }

    override fun getItemCount(): Int = flashcards.size

    class FlashcardViewHolder(
        val binding: FlashcardItemBinding,
        private val onItemClicked: (Flashcard, Int) -> Unit,
        private val onItemLongClicked: (Flashcard, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(flashcard: Flashcard) {
            with(binding) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, flashcard.backgroundColor))
                flashcardTitle.text = flashcard.title
                flashcardDescription.text = flashcard.description

                // Set up the click listener to open the edit dialog
                root.setOnClickListener {
                    onItemClicked(flashcard, adapterPosition)
                }

                // Set up the long click listener for deletion
                root.setOnLongClickListener {
                    onItemLongClicked(flashcard, adapterPosition)
                    true
                }
            }
        }
    }

    fun addFlashcard(flashcard: Flashcard) {
        flashcards.add(flashcard)
        notifyItemInserted(flashcards.size - 1)
    }

    fun editFlashcard(position: Int, newFlashcard: Flashcard) {
        flashcards[position] = newFlashcard
        notifyItemChanged(position)
    }

    fun removeFlashcard(position: Int) {
        flashcards.removeAt(position)
        notifyItemRemoved(position)
    }
    fun filterFlashcards(query: String) {
        val filteredList = originalList.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        flashcards.clear()
        flashcards.addAll(filteredList)
        notifyDataSetChanged()
    }
}
