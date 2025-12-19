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

        // Setup Book 1 Button
        val btnBook1 = view.findViewById<Button>(R.id.btnBook1)
        btnBook1.setOnClickListener {
            // Navigate to BookFragment
            // Note: BookFragment will show in red until you create it in Step 3.
            (activity as MainActivity).loadFragment(BookFragment())
        }

        // You can add listeners for other books here later (btnBook2, btnBook3, etc.)

        return view
    }
}
