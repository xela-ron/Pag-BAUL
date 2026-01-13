package com.example.pag_baul

import android.app.AlertDialog
import android.graphics.Color
import android.media.MediaPlayer
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

class Book2Station1Fragment : Fragment() {

    private val answerSlots = ArrayList<TextView>()
    private var currentSlotIndex = 0
    private val correctAnswer = "KALABAW"
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book2_station1, container, false)

        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)
        btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

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

        for (btn in letterButtons) {
            btn.setOnClickListener {
                if (currentSlotIndex < 7) {
                    answerSlots[currentSlotIndex].text = btn.text
                    currentSlotIndex++
                    btn.isEnabled = false
                    btn.alpha = 0.5f
                }
            }
        }

        // 3. Reset Button
        view.findViewById<Button>(R.id.btnReset).setOnClickListener {
            for (slot in answerSlots) {
                slot.text = ""
                slot.setTextColor(Color.BLACK)
            }
            currentSlotIndex = 0

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
                showFeedbackDialog(true)
                for (slot in answerSlots) slot.setTextColor(Color.GREEN)
            } else {
                showFeedbackDialog(false)
                for (slot in answerSlots) slot.setTextColor(Color.RED)
            }
        }

        return view
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)

        // --- FIX: REMOVED THE LINES THAT REFERENCE THE NON-EXISTENT BUTTON ---
        // val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext) // This line is removed
        // btnDialogNext.visibility = View.GONE // This line is removed

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping)
            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(context, "Station Completed!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }, 3000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww)
            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
            }, 3000)
        }
    }

    private fun playSound(soundResId: Int) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
