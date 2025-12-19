package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Setup Book 1
        val btnBook1 = view.findViewById<Button>(R.id.btnBook1)
        btnBook1.setOnClickListener {
            openBook()
        }

        // Setup Book 2
        val btnBook2 = view.findViewById<Button>(R.id.btnBook2)
        btnBook2.setOnClickListener {
            openBook()
        }

        // Setup Book 3
        val btnBook3 = view.findViewById<Button>(R.id.btnBook3)
        btnBook3.setOnClickListener {
            openBook()
        }

        // Setup Book 4
        val btnBook4 = view.findViewById<Button>(R.id.btnBook4)
        btnBook4.setOnClickListener {
            openBook()
        }

        // Setup Book 5
        val btnBook5 = view.findViewById<Button>(R.id.btnBook5)
        btnBook5.setOnClickListener {
            openBook()
        }

        return view
    }

    // Helper function to avoid repeating the navigation code 5 times
    private fun openBook() {
        (activity as MainActivity).loadFragment(BookFragment())
    }
}
