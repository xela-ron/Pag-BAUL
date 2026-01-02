package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class EssayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_essay, container, false)

        // 1. Get the data passed from BookFragment
        val titleText = arguments?.getString("ESSAY_TITLE") ?: "Station"
        val questionText = arguments?.getString("ESSAY_QUESTION") ?: "Question goes here."

        // 2. Find Views (USING THE NEW IDs FROM XML)
        val tvTitle = view.findViewById<TextView>(R.id.tvEssayTitle)       // Changed from tvTitle
        val tvQuestion = view.findViewById<TextView>(R.id.tvEssayQuestion) // Changed from tvQuestion
        val etAnswer = view.findViewById<EditText>(R.id.etEssayAnswer)     // Added EditText
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)
        val btnDone = view.findViewById<Button>(R.id.btnSubmitEssay)       // Changed from btnDoneEssay

        // 3. Set the text dynamically
        tvTitle.text = titleText
        tvQuestion.text = questionText

        // 4. Back Button Logic
        btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 5. Submit/Done Button Logic
        btnDone.setOnClickListener {
            val answer = etAnswer.text.toString()

            if (answer.isNotBlank()) {
                // In a real app, you would save the answer here
                Toast.makeText(context, "Mahusay! Ang iyong sagot ay naitala na.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Mangyaring isulat ang iyong sagot.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
