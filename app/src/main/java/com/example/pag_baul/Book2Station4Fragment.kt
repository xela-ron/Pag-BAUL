package com.example.pag_baul

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Book2Station4Fragment : Fragment() {

    private val answerSlots = ArrayList<TextView>()
    private var currentSlotIndex = 0
    private val correctAnswer = "TAGAK"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book2_station4, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val btnReset = view.findViewById<Button>(R.id.btnReset)
        val btnDone = view.findViewById<Button>(R.id.btnDone)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        // 1. Collect Answer Slots from the layout
        answerSlots.add(view.findViewById(R.id.slot1))
        answerSlots.add(view.findViewById(R.id.slot2))
        answerSlots.add(view.findViewById(R.id.slot3))
        answerSlots.add(view.findViewById(R.id.slot4))
        answerSlots.add(view.findViewById(R.id.slot5))

        // 2. Setup Letter Buttons from the layout
        val choiceButtons = listOf<Button>(
            view.findViewById(R.id.btnChoice1), view.findViewById(R.id.btnChoice2),
            view.findViewById(R.id.btnChoice3), view.findViewById(R.id.btnChoice4),
            view.findViewById(R.id.btnChoice5), view.findViewById(R.id.btnChoice6),
            view.findViewById(R.id.btnChoice7), view.findViewById(R.id.btnChoice8),
            view.findViewById(R.id.btnChoice9), view.findViewById(R.id.btnChoice10),
            view.findViewById(R.id.btnChoice11), view.findViewById(R.id.btnChoice12)
        )

        for (btn in choiceButtons) {
            btn.setOnClickListener {
                if (currentSlotIndex < answerSlots.size) {
                    answerSlots[currentSlotIndex].text = btn.text
                    currentSlotIndex++
                    btn.isEnabled = false
                    btn.alpha = 0.5f
                }
            }
        }

        // 3. Reset Button
        btnReset.setOnClickListener {
            currentSlotIndex = 0
            for (slot in answerSlots) {
                slot.text = ""
                slot.setTextColor(Color.BLACK)
            }
            for (button in choiceButtons) {
                button.isEnabled = true
                button.alpha = 1.0f
            }
        }

        // 4. Done Button
        btnDone.setOnClickListener {
            checkAnswer()
        }

        return view
    }

    private fun checkAnswer() {
        val userAnswer = StringBuilder()
        for (slot in answerSlots) {
            userAnswer.append(slot.text)
        }

        if (userAnswer.toString().equals(correctAnswer, ignoreCase = true)) {
            showFeedbackDialog(true)
            for (slot in answerSlots) slot.setTextColor(Color.GREEN)
        } else {
            showFeedbackDialog(false)
            for (slot in answerSlots) slot.setTextColor(Color.RED)
        }
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)
        val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)

        // Hide the button (same style as Book 4 Station 4 Game 2)
        btnDialogNext.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            
            dialog.show()

            // Auto-advance/complete after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(context, "Station Completed!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() 
            }, 1500)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            
            dialog.show()

            // Auto-dismiss after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
            }, 1500)
        }
    }
}
