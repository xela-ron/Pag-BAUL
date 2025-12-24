package com.example.pag_baul

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Station5Fragment : Fragment() {

    private var selectedEmotion: String? = null
    private var questionList: ArrayList<String> = ArrayList()
    private var currentQuestionIndex = 0

    private lateinit var tvQuestion: TextView
    private lateinit var btnDone: Button
    private lateinit var allEmotionButtons: List<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station5, container, false)

        // --- Bind Views ---
        tvQuestion = view.findViewById(R.id.tvQuestion)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)
        btnDone = view.findViewById(R.id.btnDone)

        // Find all 5 emotion layouts
        val btnHappy = view.findViewById<LinearLayout>(R.id.btnHappy)
        val btnProud = view.findViewById<LinearLayout>(R.id.btnProud)
        val btnSad = view.findViewById<LinearLayout>(R.id.btnSad)
        val btnAngry = view.findViewById<LinearLayout>(R.id.btnAngry)
        val btnDisappointed = view.findViewById<LinearLayout>(R.id.btnDisappointed)

        // Group all buttons into a list for easier management
        allEmotionButtons = listOf(btnHappy, btnProud, btnSad, btnAngry, btnDisappointed)

        // --- Dynamic Question Logic ---
        val args = arguments
        if (args != null) {
            val list = args.getStringArrayList("QUESTION_LIST")
            if (list != null && list.isNotEmpty()) {
                questionList = list
            }
        }

        // Show the first question if we have a list
        if (questionList.isNotEmpty()) {
            currentQuestionIndex = 0
            showCurrentQuestion()
        }

        // --- Set Click Listeners for all emotion buttons ---
        btnHappy.setOnClickListener { selectEmotion(btnHappy, "Masaya (Happy)") }
        btnProud.setOnClickListener { selectEmotion(btnProud, "Proud (Proud)") }
        btnSad.setOnClickListener { selectEmotion(btnSad, "Malungkot (Sad)") }
        btnAngry.setOnClickListener { selectEmotion(btnAngry, "Galit (Angry)") }
        btnDisappointed.setOnClickListener { selectEmotion(btnDisappointed, "Dismayado (Disappointed)") }

        // --- Logic for the Next/Done button ---
        btnDone.setOnClickListener {
            // Check if an emotion has been selected
            if (selectedEmotion == null) {
                Toast.makeText(context, "Pumili muna ng isang emosyon.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if there are more questions
            if (currentQuestionIndex < questionList.size - 1) {
                // Go to the next question
                currentQuestionIndex++
                showCurrentQuestion()
                Toast.makeText(context, "Sagot naitala! Susunod na tanong...", Toast.LENGTH_SHORT).show()
            } else {
                // This was the last question, so finish.
                Toast.makeText(context, "Magaling! Natapos mo na ang station na ito.", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            }
        }

        // --- Back Button Functionality ---
        btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun showCurrentQuestion() {
        tvQuestion.text = questionList[currentQuestionIndex]
        // Reset the selection for the new question
        resetSelection()
    }

    // This function now correctly handles the selection
    private fun selectEmotion(selectedButton: LinearLayout, emotionName: String) {
        // 1. Reset all buttons to their default state
        allEmotionButtons.forEach {
            it.setBackgroundResource(R.drawable.rounded_border)
            it.alpha = 0.7f // Make unselected ones slightly transparent
        }

        // 2. Highlight the selected button
        selectedButton.setBackgroundColor(Color.parseColor("#A5D6A7")) // A light green highlight
        selectedButton.alpha = 1.0f // Make it fully opaque

        // 3. Store the selection
        selectedEmotion = emotionName

        // 4. Show and update the Next/Done button
        if (currentQuestionIndex < questionList.size - 1) {
            btnDone.text = "NEXT"
        } else {
            btnDone.text = "DONE"
        }
        btnDone.visibility = View.VISIBLE
    }

    // This function is called to prepare for a new question
    private fun resetSelection() {
        selectedEmotion = null
        btnDone.visibility = View.GONE // Hide the Next/Done button
        // Reset all emotion buttons to their default look
        allEmotionButtons.forEach {
            it.setBackgroundResource(R.drawable.rounded_border)
            it.alpha = 1.0f
        }
    }
}
