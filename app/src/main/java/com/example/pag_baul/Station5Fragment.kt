package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Station5Fragment : Fragment() {

    private var currentQuestionIndex = 0

    // The list of 3 questions based on your second screenshot
    private val questions = listOf(
        "Ano ang nararamdaman ni Gng. Ferrer nang makitang nagkukusang naglilinis ang kaniyang mga anak?",
        "Ano ang nararamdaman nila Arlyn at Willy habang sinasabi ng kanilang ina ang katagang \"Hindi sapat ang pera para sa itlog at prutas anak\"?",
        "Ano ang nararamdaman ni Gng. Ferrer habang binibigkas ng kaniyang anak ang katagang \"Hayaan po ninyo, Nanay, pag nakatapos po ako ng pag aaral...\""
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station5, container, false)

        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val btnHappy = view.findViewById<LinearLayout>(R.id.btnHappy)
        val btnAngry = view.findViewById<LinearLayout>(R.id.btnAngry)
        val btnSad = view.findViewById<LinearLayout>(R.id.btnSad)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnBackIcon = view.findViewById<View>(R.id.btnBackIcon)

        // Initialize First Question
        updateQuestion(tvQuestion)

        // Click Listener for ANY emotion
        val emotionListener = View.OnClickListener {
            // When an emotion is clicked, go to next question
            nextQuestion(tvQuestion, btnHappy, btnAngry, btnSad, btnDone)
        }

        btnHappy.setOnClickListener(emotionListener)
        btnAngry.setOnClickListener(emotionListener)
        btnSad.setOnClickListener(emotionListener)

        // Back / Done Button Logic
        val closeAction = View.OnClickListener {
            (activity as MainActivity).loadFragment(BookFragment())
        }
        btnDone.setOnClickListener(closeAction)
        btnBackIcon.setOnClickListener(closeAction)

        return view
    }

    private fun nextQuestion(
        tvQuestion: TextView,
        btn1: View, btn2: View, btn3: View,
        btnDone: Button
    ) {
        if (currentQuestionIndex < questions.size - 1) {
            // Move to next question
            currentQuestionIndex++
            updateQuestion(tvQuestion)
        } else {
            // Finished all 3 questions
            Toast.makeText(context, "All questions answered!", Toast.LENGTH_SHORT).show()
            tvQuestion.text = "Great Job! You have finished the Emotion Chart."

            // Hide emotion buttons and show Done button
            btn1.visibility = View.GONE
            btn2.visibility = View.GONE
            btn3.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
        }
    }

    private fun updateQuestion(tvQuestion: TextView) {
        tvQuestion.text = questions[currentQuestionIndex]
    }
}
