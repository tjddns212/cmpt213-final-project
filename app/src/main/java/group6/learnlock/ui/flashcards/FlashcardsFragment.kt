package group6.learnlock.ui.flashcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import group6.learnlock.databinding.FragmentFlashcardsBinding

class FlashcardsFragment : Fragment() {

    private var _binding: FragmentFlashcardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var flashcardsViewModel: FlashcardsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        flashcardsViewModel = ViewModelProvider(this)[FlashcardsViewModel::class.java]
        _binding = FragmentFlashcardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup RecyclerView
        binding.flashcardsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.flashcardsRecyclerView.adapter = FlashcardsAdapter(listOf()) // Add your data list here

        flashcardsViewModel.text.observe(viewLifecycleOwner) {
            // Update UI when your data changes
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
