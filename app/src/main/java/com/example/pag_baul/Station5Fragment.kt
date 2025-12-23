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

        tvQuestion = view.findViewById(R.id.tvQuestion)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)
        btnDone = view.findViewById(R.id.btnDone)

        val btnHappy = view.findViewById<LinearLayout>(R.id.btnHappy)
        val btnAngry = view.findViewById<LinearLayout>(R.id.btnAngry)
        val btnSad = view.findViewById<LinearLayout>(R.id.btnSad)

        allEmotionButtons = listOf(btnHappy, btnAngry, btnSad)

        // --- DYNAMIC QUESTION LOGIC ---
        val args = arguments
        if (args != null) {
            val list = args.getStringArrayList("QUESTION_LIST")
            if (list != null && list.isNotEmpty()) {
                questionList = list
            }
        }

        if (questionList.isNotEmpty()) {
            currentQuestionIndex = 0
            showCurrentQuestion()
        }

        btnHappy.setOnClickListener { selectEmotion(btnHappy, "Masaya (Happy)") }
        btnAngry.setOnClickListener { selectEmotion(btnAngry, "Galit (Angry)") }
        btnSad.setOnClickListener { selectEmotion(btnSad, "Malungkot (Sad)") }

        btnDone.setOnClickListener {
            if (selectedEmotion != null) {
                if (questionList.isNotEmpty() && currentQuestionIndex < questionList.size - 1) {
                    Toast.makeText(context, "Sagot naitala! Susunod na tanong...", Toast.LENGTH_SHORT).show()
                    currentQuestionIndex++
                    showCurrentQuestion()
                } else {
                    Toast.makeText(context, "Magaling! Natapos mo na ang station na ito.", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }

        btnBackIcon.setOnClickListener { parentFragmentManager.popBackStack() }

        return view
    }

    private fun showCurrentQuestion() {
        tvQuestion.text = questionList[currentQuestionIndex]
        resetSelection()
    }

    private fun selectEmotion(selectedButton: LinearLayout, emotionName: String) {
        allEmotionButtons.forEach { it.setBackgroundResource(R.drawable.rounded_border) }
        selectedButton.setBackgroundColor(Color.parseColor("#E0E0E0"))
        selectedEmotion = emotionName

        if (questionList.isNotEmpty() && currentQuestionIndex < questionList.size - 1) {
            btnDone.text = "NEXT"
        } else {
            btnDone.text = "DONE"
        }
        btnDone.visibility = View.VISIBLE
    }

    private fun resetSelection() {
        selectedEmotion = null
        btnDone.visibility = View.GONE
        allEmotionButtons.forEach { it.setBackgroundResource(R.drawable.rounded_border) }
    }
}
