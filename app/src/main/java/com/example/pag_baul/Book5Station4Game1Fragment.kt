package com.example.pag_baul

import android.app.AlertDialog
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
import androidx.fragment.app.Fragment

class Book5Station4Game1Fragment : Fragment() {

    private lateinit var ivOption1: ImageView
    private lateinit var ivOption2: ImageView
    private lateinit var ivOption3: ImageView
    private lateinit var tvQuestion: TextView
    private lateinit var btnDone: Button

    private var mediaPlayer: MediaPlayer? = null
    private var currentLevel = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book5_station4_game1, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        ivOption1 = view.findViewById(R.id.ivOption1)
        ivOption2 = view.findViewById(R.id.ivOption2)
        ivOption3 = view.findViewById(R.id.ivOption3)
        tvQuestion = view.findViewById(R.id.tvQuestion)
        btnDone = view.findViewById(R.id.btnDone)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        // Open Game 2 when NEXT is clicked
        btnDone.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(Book5Station4Game2Fragment())
        }

        setupLevel(1)

        return view
    }

    private fun setupLevel(level: Int) {
        currentLevel = level

        // Clear click listeners first
        ivOption1.setOnClickListener(null)
        ivOption2.setOnClickListener(null)
        ivOption3.setOnClickListener(null)

        // Set generic instructions
        tvQuestion.text = "Piliin ang tamang larawan."
        tvQuestion.textSize = 20f

        when (level) {
            1 -> {
                ivOption1.setImageResource(R.drawable.g1_pic1_1) // Tiger
                ivOption2.setImageResource(R.drawable.g1_pic1_2) // Cow
                ivOption3.setImageResource(R.drawable.g1_pic1_3) // Horse/Lion (Correct)

                ivOption1.setOnClickListener { showFeedbackDialog(false) }
                ivOption2.setOnClickListener { showFeedbackDialog(false) }
                ivOption3.setOnClickListener { showFeedbackDialog(true) }
            }
            2 -> {
                ivOption1.setImageResource(R.drawable.g1_pic2_1) // Jungle animals
                ivOption2.setImageResource(R.drawable.g1_pic2_2) // Horse kicking lion (Correct)
                ivOption3.setImageResource(R.drawable.g1_pic2_3) // Disney characters

                ivOption1.setOnClickListener { showFeedbackDialog(false) }
                ivOption2.setOnClickListener { showFeedbackDialog(true) }
                ivOption3.setOnClickListener { showFeedbackDialog(false) }
            }
            3 -> {
                ivOption1.setImageResource(R.drawable.g1_pic3_1) // Cartoon animals
                ivOption2.setImageResource(R.drawable.g1_pic3_2) // Horse and lion (Correct)
                ivOption3.setImageResource(R.drawable.g1_pic3_3) // Tom and Jerry

                ivOption1.setOnClickListener { showFeedbackDialog(false) }
                ivOption2.setOnClickListener { showFeedbackDialog(true) }
                ivOption3.setOnClickListener { showFeedbackDialog(false) }
            }
            else -> {
                // End of game
                showFinalScreen()
            }
        }
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
            // Start playing the clapping sound
            mediaPlayer?.release() // Release any existing player
            mediaPlayer = MediaPlayer.create(context, R.raw.clapping)
            mediaPlayer?.start()

            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"

            dialog.setOnDismissListener {
                // Stop and release the media player when the dialog is dismissed
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
                if (currentLevel < 3) {
                    setupLevel(currentLevel + 1)
                } else {
                    showFinalScreen()
                }
            }, 3000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
            }, 3000)
        }
    }

    private fun showFinalScreen() {
        // Change the UI to show the final question instead of a popup
        // Hide the images
        ivOption1.visibility = View.GONE
        ivOption2.visibility = View.GONE
        ivOption3.visibility = View.GONE

        // Update the question text to the final question
        tvQuestion.text = "1: Paano kung hindi napansin ng kabayo ang plano ng leon?"
        tvQuestion.textSize = 24f // Make it a bit bigger for better readability

        // Show the Done button
        btnDone.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        // Ensure media player is released when the fragment is stopped or paused
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
