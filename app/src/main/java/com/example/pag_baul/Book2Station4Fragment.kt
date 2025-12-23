package com.example.pag_baul

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Book2Station4Fragment : Fragment() {

    // The correct answer for the puzzle
    private val correctAnswer = "TAGAK"

    // Arrays to track the state
    private val answerSlots = arrayOfNulls<TextView>(correctAnswer.length)
    private val userAnswer = arrayOfNulls<String>(correctAnswer.length)

    // UI Containers
    private lateinit var layoutAnswerSlots: LinearLayout
    private lateinit var layoutChoices: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book2_station4, container, false)

        layoutAnswerSlots = view.findViewById(R.id.layoutAnswerSlots)
        layoutChoices = view.findViewById(R.id.layoutChoices)
        val btnReset = view.findViewById<Button>(R.id.btnReset)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        setupGame()

        btnReset.setOnClickListener {
            resetGame()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun setupGame() {
        // 1. Create the Answer Slots (The empty boxes)
        layoutAnswerSlots.removeAllViews()

        for (i in correctAnswer.indices) {
            val textView = TextView(context)
            val params = LinearLayout.LayoutParams(120, 120) // Box size
            params.setMargins(8, 0, 8, 0)
            textView.layoutParams = params
            // Use a simple background color or drawable for the box
            textView.setBackgroundColor(Color.LTGRAY)
            textView.gravity = Gravity.CENTER
            textView.textSize = 24f
            textView.setTextColor(Color.BLACK)
            textView.isClickable = true

            // Allow user to remove a letter by clicking the box
            textView.setOnClickListener {
                removeLetterFromSlot(i)
            }

            answerSlots[i] = textView
            layoutAnswerSlots.addView(textView)
        }

        // 2. Create the Scrambled Letters (The keyboard)
        generateScrambledLetters()
    }

    private fun generateScrambledLetters() {
        layoutChoices.removeAllViews()

        // Combine answer letters with some random decoys
        val allLetters = ArrayList<Char>()
        for (char in correctAnswer) {
            allLetters.add(char)
        }
        // Add random letters to fill up to 12 choices
        val decoys = "BCDEFHILMNOPSU"
        while (allLetters.size < 12) {
            allLetters.add(decoys.random())
        }

        // Shuffle them
        allLetters.shuffle()

        // Create Buttons for each letter
        for (char in allLetters) {
            val button = Button(context)
            val params = GridLayout.LayoutParams()
            params.width = 130
            params.height = 130
            params.setMargins(8, 8, 8, 8)
            button.layoutParams = params
            button.text = char.toString()
            button.textSize = 18f

            button.setOnClickListener {
                addLetterToAnswer(char.toString(), button)
            }
            layoutChoices.addView(button)
        }
    }

    private fun addLetterToAnswer(letter: String, sourceButton: Button) {
        // Find the first empty slot
        for (i in userAnswer.indices) {
            if (userAnswer[i] == null) {
                // Fill the slot
                userAnswer[i] = letter
                answerSlots[i]?.text = letter

                // Disable the button so it can't be clicked again immediately
                sourceButton.isEnabled = false
                sourceButton.alpha = 0.5f

                // Check if full
                checkIfComplete()
                return
            }
        }
    }

    private fun removeLetterFromSlot(index: Int) {
        val letterToRemove = userAnswer[index] ?: return

        // 1. Find the button in the choices that matches this letter and is disabled
        for (i in 0 until layoutChoices.childCount) {
            val btn = layoutChoices.getChildAt(i) as Button
            if (btn.text == letterToRemove && !btn.isEnabled) {
                btn.isEnabled = true
                btn.alpha = 1.0f
                break
            }
        }

        // 2. Clear the slot
        userAnswer[index] = null
        answerSlots[index]?.text = ""
        answerSlots[index]?.setBackgroundColor(Color.LTGRAY) // Reset color
    }

    private fun checkIfComplete() {
        // Check if all slots are filled
        if (userAnswer.contains(null)) return

        // Construct the word
        val finalWord = userAnswer.joinToString("")

        if (finalWord == correctAnswer) {
            Toast.makeText(context, "GOOD JOB! Correct Answer: $correctAnswer", Toast.LENGTH_LONG).show()
            for (tv in answerSlots) {
                tv?.setBackgroundColor(Color.GREEN)
            }
        } else {
            Toast.makeText(context, "Try Again!", Toast.LENGTH_SHORT).show()
            for (tv in answerSlots) {
                tv?.setBackgroundColor(Color.RED)
            }
        }
    }

    private fun resetGame() {
        for (i in userAnswer.indices) {
            removeLetterFromSlot(i)
        }
    }
}
