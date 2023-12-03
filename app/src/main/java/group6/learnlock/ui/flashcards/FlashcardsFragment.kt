package group6.learnlock.ui.flashcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import group6.learnlock.R
import group6.learnlock.databinding.DialogAddEditFlashcardBinding
import group6.learnlock.databinding.FragmentFlashcardsBinding
import java.text.SimpleDateFormat
import androidx.appcompat.app.AlertDialog
import java.util.*

class FlashcardsFragment : Fragment() {

    private var _binding: FragmentFlashcardsBinding? = null
    private val binding get() = _binding!!
    private val flashcardsData = mutableListOf<Flashcard>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFlashcardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentDate = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
        binding.textDate.text = currentDate

        val adapter = FlashcardsAdapter(flashcardsData) { flashcard, position ->
            showAddEditFlashcardDialog(flashcard, position)
        }

        binding.flashcardsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.flashcardsRecyclerView.adapter = adapter

        binding.fabAddFlashcard.setOnClickListener {
            showAddEditFlashcardDialog(null, -1)
        }

        return root
    }

    private fun showAddEditFlashcardDialog(flashcard: Flashcard?, position: Int) {
        val dialogBinding = DialogAddEditFlashcardBinding.inflate(layoutInflater)
        dialogBinding.editTextFlashcardTitle.setText(flashcard?.title ?: "")
        dialogBinding.editTextFlashcardDescription.setText(flashcard?.description ?: "")
        setupColorSpinner(dialogBinding, flashcard?.backgroundColor)

        AlertDialog.Builder(requireContext())
            .setTitle(if (flashcard == null) R.string.add_flashcard else R.string.edit_flashcard)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                val title = dialogBinding.editTextFlashcardTitle.text.toString()
                val description = dialogBinding.editTextFlashcardDescription.text.toString()
                val selectedColorName = dialogBinding.spinnerFlashcardColor.selectedItem.toString()
                val colorResId = resources.getIdentifier(selectedColorName, "color", requireContext().packageName)

                if (flashcard == null) {
                    val newFlashcard = Flashcard(title, description, colorResId)
                    flashcardsData.add(newFlashcard)
                    binding.flashcardsRecyclerView.adapter?.notifyItemInserted(flashcardsData.size - 1)
                } else {
                    flashcard.title = title
                    flashcard.description = description
                    flashcard.backgroundColor = colorResId
                    binding.flashcardsRecyclerView.adapter?.notifyItemChanged(position)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun setupColorSpinner(dialogBinding: DialogAddEditFlashcardBinding, selectedColorResId: Int?) {
        val colorsMap = mapOf(
            "colorBlue" to R.color.colorBlue,
            "colorRed" to R.color.colorRed,
            "colorPurple" to R.color.colorPurple,
            "colorPink" to R.color.colorPink,
            "colorAccent" to R.color.colorAccent
        )

        val colorNames = colorsMap.keys.toList()
        val colorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, colorNames)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerFlashcardColor.adapter = colorAdapter

        // Preselect the spinner if editing
        selectedColorResId?.let { colorResId ->
            val colorName = colorsMap.entries.firstOrNull { it.value == colorResId }?.key
            val spinnerPosition = colorAdapter.getPosition(colorName)
            dialogBinding.spinnerFlashcardColor.setSelection(spinnerPosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}