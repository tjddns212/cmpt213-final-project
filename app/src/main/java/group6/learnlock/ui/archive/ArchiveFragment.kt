package group6.learnlock.ui.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import group6.learnlock.R
import group6.learnlock.databinding.FragmentArchiveBinding

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null


    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val galViewModel = ViewModelProvider(this)[ArchiveViewModel::class.java]

        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        val root : View = binding.root


        val textView: TextView = binding.archiveTitle
        galViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}