package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class Book5Station1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_book5_station1, container, false)

        // Setup the back button to navigate to the previous screen
        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
