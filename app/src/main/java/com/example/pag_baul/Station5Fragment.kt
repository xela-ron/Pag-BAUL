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
import com.example.pag_baul.databinding.FragmentStation5Binding // Import the binding class

class Station5Fragment : Fragment() {

    // --- View Binding Setup ---
    private var _binding: FragmentStation5Binding? = null
    private val binding get() = _binding!!

    private var selectedEmotion: String? = null
    private var questionList: ArrayList<String> = ArrayList()
    private var currentQuestionIndex = 0

    // List to hold the emotion buttons for easy management
    private lateinit var allEmotionButtons: List<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // --- Inflate layout using View Binding ---
        _binding = FragmentStation5Binding.inflate(inflater, container, false)

        // --- Group all buttons into a list for easier management ---
        allEmotionButtons = listOf(
            binding.btnHappy,
            binding.btnProud,
            binding.btnSad,
            binding.btnAngry,
            binding.btnDisappointed
        )

        // --- START: CORRECTED DYNAMIC QUESTION LOGIC ---
        // 1. Get the arguments from BookFragment using the CORRECT key "questions"
        val receivedQuestions = arguments?.getStringArrayList("questions")

        // 2. Check if the list is not null and not empty
        if (receivedQuestions != null && receivedQuestions.isNotEmpty()) {
            questionList = receivedQuestions
            currentQuestionIndex = 0
            showCurrentQuestion()
        } else {
            // Display a message if no questions are found
            binding.tvQuestion.text = "No questions found for this station."
        }
        // --- END: CORRECTED DYNAMIC QUESTION LOGIC ---


        // --- Set Click Listeners for all emotion buttons ---
        binding.btnHappy.setOnClickListener { selectEmotion(binding.btnHappy, "Masaya (Happy)") }
        binding.btnProud.setOnClickListener { selectEmotion(binding.btnProud, "Proud (Proud)") }
        binding.btnSad.setOnClickListener { selectEmotion(binding.btnSad, "Malungkot (Sad)") }
        binding.btnAngry.setOnClickListener { selectEmotion(binding.btnAngry, "Galit (Angry)") }
        binding.btnDisappointed.setOnClickListener { selectEmotion(binding.btnDisappointed, "Dismayado (Disappointed)") }

        // --- Logic for the Next/Done button ---
        binding.btnDone.setOnClickListener {
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
            } else {
                // This was the last question, so finish.
                Toast.makeText(context, "Magaling! Natapos mo na ang station na ito.", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            }
        }

        // --- Back Button Functionality ---
        binding.btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun showCurrentQuestion() {
        if (questionList.isNotEmpty()) {
            binding.tvQuestion.text = questionList[currentQuestionIndex]
            // Reset the selection for the new question
            resetSelection()
        }
    }

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
            binding.btnDone.text = "NEXT"
        } else {
            binding.btnDone.text = "DONE"
        }
        binding.btnDone.visibility = View.VISIBLE
    }

    private fun resetSelection() {
        selectedEmotion = null
        binding.btnDone.visibility = View.GONE // Hide the Next/Done button
        // Reset all emotion buttons to their default look
        allEmotionButtons.forEach {
            it.setBackgroundResource(R.drawable.rounded_border)
            it.alpha = 1.0f
        }
    }

    // Standard lifecycle method to prevent memory leaks with ViewBinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
