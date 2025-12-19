package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment

class StationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_station, container, false)

        // Get the station number passed from BookFragment
        val stationNumber = arguments?.getInt("STATION_NUMBER") ?: 1

        // Update the Title Text
        val tvTitle = view.findViewById<TextView>(R.id.tvStationTitle)
        tvTitle.text = "Station $stationNumber"

        // Setup Done Button (Go back)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        btnDone.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
