package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class QuizFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the new single-scroll layout
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        // Find the interactive buttons
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)
        val btnDone = view.findViewById<Button>(R.id.btnDoneQuiz)

        // Set listener for the Back Icon to close the screen
        btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Set listener for the Done Button to close the screen
        // (Later, you can add logic here to check answers if you want)
        btnDone.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
