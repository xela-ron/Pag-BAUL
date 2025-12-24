package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Station4Game1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station4_game1, container, false)

        setupFlipCard(view.findViewById(R.id.card1), view.findViewById(R.id.card1Front), view.findViewById(R.id.card1Back))
        setupFlipCard(view.findViewById(R.id.card2), view.findViewById(R.id.card2Front), view.findViewById(R.id.card2Back))
        setupFlipCard(view.findViewById(R.id.card3), view.findViewById(R.id.card3Front), view.findViewById(R.id.card3Back))
        setupFlipCard(view.findViewById(R.id.card4), view.findViewById(R.id.card4Front), view.findViewById(R.id.card4Back))

        // Navigate to Game 2
        view.findViewById<Button>(R.id.btnNextGame).setOnClickListener {
            (activity as MainActivity).loadFragment(Station4Game2Fragment())
        }

        // Handle Back Button
        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        btnBack.setOnClickListener {
            // Using activity's onBackPressed or popping stack directly
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                // Fallback if stack is empty, though unlikely given your flow
                requireActivity().onBackPressed()
            }
        }

        return view
    }

    private fun setupFlipCard(container: FrameLayout, front: TextView, back: TextView) {
        container.setOnClickListener {
            if (front.visibility == View.VISIBLE) {
                front.visibility = View.INVISIBLE
                back.visibility = View.VISIBLE
            } else {
                front.visibility = View.VISIBLE
                back.visibility = View.INVISIBLE
            }
        }
    }
}
