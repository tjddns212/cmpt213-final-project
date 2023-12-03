package group6.learnlock.ui.flashcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import group6.learnlock.R
import group6.learnlock.databinding.DialogAddEditFlashcardBinding
import group6.learnlock.databinding.FragmentFlashcardsBinding
import java.text.SimpleDateFormat
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

class FlashcardsFragment : Fragment() {

    private var _binding: FragmentFlashcardsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardsViewModel by viewModels()
    private lateinit var adapter: FlashcardsAdapter // Declare adapter here

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFlashcardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentDate = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
        binding.textDate.text = currentDate

        // Initialize adapter here
        adapter = FlashcardsAdapter(viewModel.flashcardsData.value ?: mutableListOf(), { flashcard, position ->
            showAddEditFlashcardDialog(flashcard, position)
        }, { _, position ->
            confirmAndDeleteFlashcard(position)
        })

        binding.flashcardsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.flashcardsRecyclerView.adapter = adapter

        binding.fabAddFlashcard.setOnClickListener {
            showAddEditFlashcardDialog(null, -1)
        }

        setupSearchView()

        return root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterFlashcards(newText ?: "")
                return false
            }
        })
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

                val newFlashcard = Flashcard(title, description, colorResId)
                if (flashcard == null) {
                    viewModel.flashcardsData.value?.add(newFlashcard)
                } else {
                    viewModel.flashcardsData.value?.set(position, newFlashcard)
                }
                adapter.notifyDataSetChanged()
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

    private fun confirmAndDeleteFlashcard(position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                (binding.flashcardsRecyclerView.adapter as FlashcardsAdapter).removeFlashcard(position)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}