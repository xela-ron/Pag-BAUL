package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.FragmentHomeBinding // Import the binding class for your home fragment

class HomeFragment : Fragment() {

    // Setup ViewBinding for safety and convenience
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup Book 1
        binding.btnBook1.setOnClickListener {
            openBook(1)
        }

        // Setup Book 2
        binding.btnBook2.setOnClickListener {
            openBook(2)
        }

        // Setup Book 3
        binding.btnBook3.setOnClickListener {
            openBook(3)
        }

        // Setup Book 4
        binding.btnBook4.setOnClickListener {
            openBook(4)
        }

        // Setup Book 5 (Make sure you have a view with id 'btnBook5' in your fragment_home.xml)
        binding.btnBook5.setOnClickListener {
            openBook(5)
        }

        return binding.root
    }

    private fun openBook(bookId: Int) {
        val bookFragment = BookFragment()
        val bundle = Bundle()

        // 1. Use the public constant from BookFragment to ensure the keys ALWAYS match.
        //    This requires you to have added the companion object to BookFragment.
        bundle.putInt(BookFragment.BOOK_ID_KEY, bookId)
        bookFragment.arguments = bundle

        // 2. Perform the fragment transaction directly here using .replace().
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, bookFragment)
            // --- START: REVISED CODE ---
            // Give this transaction a unique name. This creates an "anchor" we can
            // reliably return to from any station or game.
            .addToBackStack("book_map")
            // --- END: REVISED CODE ---
            .commit()
    }

    // This is a standard lifecycle method to prevent memory leaks with ViewBinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
