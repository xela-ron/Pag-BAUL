package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class QuizFragment : Fragment() {

    // Default questions
    private var questionList = ArrayList<QuestionData>()

    // UI Elements
    private lateinit var tvQuestion: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnCheck: Button
    private lateinit var btnBack: ImageView
    private var currentQuestionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_dynamic, container, false)

        // Initialize UI
        tvQuestion = view.findViewById(R.id.tvQuestion)
        radioGroup = view.findViewById(R.id.radioGroup)
        btnCheck = view.findViewById(R.id.btnCheck)
        btnBack = view.findViewById(R.id.btnBackIcon)

        // 1. Get Questions from Bundle
        val questionsArg = arguments?.getParcelableArrayList<QuestionData>("QUESTIONS")

        if (questionsArg != null && questionsArg.isNotEmpty()) {
            questionList = questionsArg
        } else {
            loadBook1Defaults()
        }

        // 2. Load First Question
        loadQuestion()

        // 3. Handle Submit Button
        btnCheck.setOnClickListener {
            checkAnswer()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun loadQuestion() {
        if (questionList.isEmpty()) return

        val q = questionList[currentQuestionIndex]

        // Set Question Text
        tvQuestion.text = "${currentQuestionIndex + 1}. ${q.question}"

        // Clear previous options
        radioGroup.removeAllViews()
        radioGroup.clearCheck()

        // Add Options Dynamically
        val options = ArrayList<String>()
        if (q.option1.isNotEmpty()) options.add(q.option1)
        if (q.option2.isNotEmpty()) options.add(q.option2)
        if (q.option3.isNotEmpty()) options.add(q.option3)
        if (q.option4.isNotEmpty()) options.add(q.option4)

        for (option in options) {
            val rb = RadioButton(context)
            rb.text = option
            rb.textSize = 16f
            rb.setPadding(16, 16, 16, 16)
            radioGroup.addView(rb)
        }

        // Update Button Text
        btnCheck.text = if (currentQuestionIndex == questionList.size - 1) "FINISH" else "NEXT"
    }

    private fun checkAnswer() {
        val selectedId = radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRb = view?.findViewById<RadioButton>(selectedId)
        val selectedText = selectedRb?.text.toString()
        val correctAnswer = questionList[currentQuestionIndex].answer

        if (selectedText == correctAnswer) {
            // Correct
            Toast.makeText(context, "Good Job! 😊", Toast.LENGTH_SHORT).show()

            if (currentQuestionIndex < questionList.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            } else {
                Toast.makeText(context, "Quiz Completed!", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            }
        } else {
            // Wrong
            Toast.makeText(context, "Try Again 😢", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadBook1Defaults() {
        // Fallback dummy question
        questionList.add(QuestionData("Sample Question?", "Yes", "No", "Maybe", "", "Yes"))
    }
}
