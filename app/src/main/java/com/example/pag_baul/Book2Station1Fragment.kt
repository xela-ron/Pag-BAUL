package com.example.pag_baul

import android.app.AlertDialog
import android.graphics.Color
import android.media.MediaPlayer // Import MediaPlayer
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

    // --- CHANGE 1: Add MediaPlayer variable ---
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book2_station1, container, false)

        // --- Find Back Button and Set Listener ---
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
                showFeedbackDialog(true)
                // Mark letters green
                for (slot in answerSlots) slot.setTextColor(Color.GREEN)
            } else {
                showFeedbackDialog(false)
                // Mark letters red
                for (slot in answerSlots) slot.setTextColor(Color.RED)
            }
        }

        return view
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)
        val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)

        btnDialogNext.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            // --- CHANGE 2: Play clapping sound ---
            playSound(R.raw.clapping)

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                // --- THE FIX: Stop the sound first ---
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(context, "Station Completed!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }, 2000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            // --- CHANGE 3: Play "aww" sound ---
            playSound(R.raw.awww)

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                // --- THE FIX: Stop the sound first ---
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
            }, 2000)
        }
    }

    // --- CHANGE 4: Add the playSound function ---
    private fun playSound(soundResId: Int) {
        // Stop and release any previous media player
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        // Create and start a new media player
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    // --- CHANGE 5: Add onStop to release the media player ---
    override fun onStop() {
        super.onStop()
        // Release the media player when the fragment is not visible
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
