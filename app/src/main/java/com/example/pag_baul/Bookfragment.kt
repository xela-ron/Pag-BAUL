package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class BookFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book, container, false)

        // 1. Setup Station 1 Button
        val btnStation1 = view.findViewById<Button>(R.id.btnStation1)
        btnStation1?.setOnClickListener {
            openStation(1)
        }

        // 2. Setup Station 2 Button
        val btnStation2 = view.findViewById<Button>(R.id.btnStation2)
        btnStation2?.setOnClickListener {
            openStation(2)
        }

        // You can add more buttons here (btnStation3, btnStation4...)

        return view
    }

    // Helper function to open the Station Screen
    private fun openStation(stationNumber: Int) {
        val fragment = StationFragment()

        // Pass the station number (so the next screen knows which one to show)
        val bundle = Bundle()
        bundle.putInt("STATION_NUMBER", stationNumber)
        fragment.arguments = bundle

        // Navigate
        (activity as MainActivity).loadFragment(fragment)
    }
}
