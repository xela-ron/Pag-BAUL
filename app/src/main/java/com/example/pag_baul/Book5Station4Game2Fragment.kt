package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Book5Station4Game2Fragment : Fragment() {

    private lateinit var tvQuestion: TextView
    private lateinit var ivChallenge: ImageView
    private lateinit var btnNext: Button
    private lateinit var btnDone: Button

    private var currentChallenge = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book5_station4_game2, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        tvQuestion = view.findViewById(R.id.tvQuestion)
        ivChallenge = view.findViewById(R.id.ivChallenge)
        btnNext = view.findViewById(R.id.btnNext)
        btnDone = view.findViewById(R.id.btnDone)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        setupChallenge(1)

        btnNext.setOnClickListener {
            setupChallenge(2)
        }

        btnDone.setOnClickListener {
            // Pop back stack twice to go back to the station list (skipping Game 1)
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun setupChallenge(challenge: Int) {
        currentChallenge = challenge
        when (challenge) {
            1 -> {
                tvQuestion.text = "Ano ang gagawin ng kabayo sa mga sangang nakaharang upang makalagpas siya sa kanyang dinadaanan ng maaliwalas?"
                ivChallenge.setImageResource(R.drawable.firstchallenge)
                btnNext.visibility = View.VISIBLE
                btnDone.visibility = View.GONE
            }
            2 -> {
                tvQuestion.text = "Ano ang gagawin ng kabayo upang hindi sya makagat ng ahas sa kanyang pagtawid?"
                ivChallenge.setImageResource(R.drawable.secondchallenge)
                btnNext.visibility = View.GONE
                btnDone.visibility = View.VISIBLE
            }
        }
    }
}
