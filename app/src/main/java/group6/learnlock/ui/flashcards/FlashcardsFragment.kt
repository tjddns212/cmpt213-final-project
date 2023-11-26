package group6.learnlock.ui.flashcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import group6.learnlock.R
import group6.learnlock.databinding.FragmentFlashcardsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Flashcard(
    val title: String,
    val description: String,
    val backgroundColor: Int // Use color resource IDs (e.g., R.color.blue)
)

class FlashcardsFragment : Fragment() {

    private var _binding: FragmentFlashcardsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashcardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentDate = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
        binding.textDate.text = currentDate


        val flashcardsData = listOf(
            Flashcard("CMPT 354", "A database is an organized collection of structured information, or data, typically stored electronically in a computer system.", R.color.colorBlue),
            Flashcard("CMPT 362", "MVVM stands for Model-View-ViewModel, it's a software architectural pattern.", R.color.colorRed),
            Flashcard("CMPT 376W", "LaTeX is a high-quality typesetting system; it includes features designed for the production of technical and scientific documentation.", R.color.colorPurple),
            Flashcard("CMPT 376W", "LaTeX is a high-quality typesetting system; it includes features designed for the production of technical and scientific documentation.", R.color.colorPink)
            // Add more flashcards as needed
        )

        val adapter = FlashcardsAdapter(flashcardsData)
        binding.flashcardsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.flashcardsRecyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
