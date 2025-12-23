package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout // Changed from Button to LinearLayout
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Setup Book 1
        view.findViewById<LinearLayout>(R.id.btnBook1).setOnClickListener {
            openBook(1)
        }

        // Setup Book 2
        view.findViewById<LinearLayout>(R.id.btnBook2).setOnClickListener {
            openBook(2)
        }

        // Setup Book 3
        view.findViewById<LinearLayout>(R.id.btnBook3).setOnClickListener {
            openBook(3)
        }

        // Setup Book 4
        view.findViewById<LinearLayout>(R.id.btnBook4).setOnClickListener {
            openBook(4)
        }

        // Setup Book 5
        view.findViewById<LinearLayout>(R.id.btnBook5).setOnClickListener {
            openBook(5)
        }

        return view
    }

    private fun openBook(bookId: Int) {
        val bookFragment = BookFragment()
        val bundle = Bundle()
        bundle.putInt("BOOK_ID", bookId)
        bookFragment.arguments = bundle
        (activity as MainActivity).loadFragment(bookFragment)
    }
}
