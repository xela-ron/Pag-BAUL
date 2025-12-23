package com.example.pag_baul

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Book2Station1Fragment : Fragment() {

    private val answerSlots = ArrayList<TextView>()
    private var currentSlotIndex = 0
    private val correctAnswer = "KALABAW"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book2_station1, container, false)

        // 1. Collect Answer Slots
        answerSlots.add(view.findViewById(R.id.slot1))
        answerSlots.add(view.findViewById(R.id.slot2))
        answerSlots.add(view.findViewById(R.id.slot3))
        answerSlots.add(view.findViewById(R.id.slot4))
        answerSlots.add(view.findViewById(R.id.slot5))
        answerSlots.add(view.findViewById(R.id.slot6))
        answerSlots.add(view.findViewById(R.id.slot7))

        // 2. Setup Letter Buttons
        val letterButtons = listOf<Button>(
            view.findViewById(R.id.btnChoice1), view.findViewById(R.id.btnChoice2),
            view.findViewById(R.id.btnChoice3), view.findViewById(R.id.btnChoice4),
            view.findViewById(R.id.btnChoice5), view.findViewById(R.id.btnChoice6),
            view.findViewById(R.id.btnChoice7), view.findViewById(R.id.btnChoice8),
            view.findViewById(R.id.btnChoice9), view.findViewById(R.id.btnChoice10)
        )

        // Add Click Listener to all letter buttons
        for (btn in letterButtons) {
            btn.setOnClickListener {
                if (currentSlotIndex < 7) {
                    // Put the letter in the current slot
                    answerSlots[currentSlotIndex].text = btn.text
                    currentSlotIndex++

                    // Hide the button so it can't be clicked again
                    btn.isEnabled = false
                    btn.alpha = 0.5f
                }
            }
        }

        // 3. Reset Button
        view.findViewById<Button>(R.id.btnReset).setOnClickListener {
            // Clear all slots
            for (slot in answerSlots) {
                slot.text = ""
                slot.setTextColor(Color.BLACK)
            }
            currentSlotIndex = 0

            // Re-enable all buttons
            for (btn in letterButtons) {
                btn.isEnabled = true
                btn.alpha = 1.0f
            }
        }

        // 4. Done Button (Check Answer)
        view.findViewById<Button>(R.id.btnDone).setOnClickListener {
            val userAnswer = StringBuilder()
            for (slot in answerSlots) {
                userAnswer.append(slot.text)
            }

            if (userAnswer.toString() == correctAnswer) {
                Toast.makeText(context, "CORRECT! Ang sagot ay KALABAW.", Toast.LENGTH_LONG).show()
                // Mark letters green
                for (slot in answerSlots) slot.setTextColor(Color.GREEN)

                // Navigate back after a delay or just close
                // (activity as MainActivity).loadFragment(BookFragment())
            } else {
                Toast.makeText(context, "Wrong Answer. Try again!", Toast.LENGTH_SHORT).show()
                // Mark letters red
                for (slot in answerSlots) slot.setTextColor(Color.RED)
            }
        }

        return view
    }
}
