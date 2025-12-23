package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class QuizFragment : Fragment() {

    // Default questions (Book 1) - used as fallback
    private var questionList = ArrayList<QuestionData>()

    // UI Elements
    private lateinit var tvQuestion: TextView
    private lateinit var rbOption1: RadioButton
    private lateinit var rbOption2: RadioButton
    private lateinit var rbOption3: RadioButton
    private lateinit var rbOption4: RadioButton // Might be hidden if only 3 options
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnNext: Button
    private lateinit var tvProgress: TextView

    private var currentQuestionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_dynamic, container, false) // Note: We will create this layout next

        // Initialize UI
        tvQuestion = view.findViewById(R.id.tvQuestion)
        rbOption1 = view.findViewById(R.id.rbOption1)
        rbOption2 = view.findViewById(R.id.rbOption2)
        rbOption3 = view.findViewById(R.id.rbOption3)
        rbOption4 = view.findViewById(R.id.rbOption4)
        radioGroup = view.findViewById(R.id.radioGroup)
        btnNext = view.findViewById(R.id.btnNext)
        tvProgress = view.findViewById(R.id.tvProgress)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)

        // 1. Get Questions from Bundle
        val questionsArg = arguments?.getParcelableArrayList<QuestionData>("QUESTIONS")

        if (questionsArg != null && questionsArg.isNotEmpty()) {
            questionList = questionsArg
        } else {
            // Fallback: Load Book 1 Questions manually here if needed
            loadBook1Defaults()
        }

        // 2. Load First Question
        loadQuestion()

        // 3. Handle Next/Submit Button
        btnNext.setOnClickListener {
            checkAnswer()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun loadQuestion() {
        val q = questionList[currentQuestionIndex]

        tvQuestion.text = "${currentQuestionIndex + 1}. ${q.question}"
        rbOption1.text = q.option1
        rbOption2.text = q.option2
        rbOption3.text = q.option3

        // Handle optional 4th option
        if (q.option4.isNotEmpty()) {
            rbOption4.visibility = View.VISIBLE
            rbOption4.text = q.option4
        } else {
            rbOption4.visibility = View.GONE
        }

        // Reset Selection
        radioGroup.clearCheck()
        tvProgress.text = "${currentQuestionIndex + 1}/${questionList.size}"
        btnNext.text = if (currentQuestionIndex == questionList.size - 1) "FINISH" else "NEXT"
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

        // Logic: Show sad face if wrong, Good job if correct
        if (selectedText == correctAnswer) {
            // CORRECT!
            Toast.makeText(context, "Good Job! 😊", Toast.LENGTH_SHORT).show()

            if (currentQuestionIndex < questionList.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            } else {
                // Quiz Finished
                Toast.makeText(context, "Quiz Completed!", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            }
        } else {
            // WRONG!
            Toast.makeText(context, "Try Again 😢", Toast.LENGTH_SHORT).show()
            // Do not advance index, let them retry
        }
    }

    private fun loadBook1Defaults() {
        // Add your Book 1 questions here if the bundle is empty
        // For now, adding dummy data to prevent crash
        questionList.add(QuestionData("Book 1 Q1...", "A", "B", "C", "D", "A"))
    }
}
