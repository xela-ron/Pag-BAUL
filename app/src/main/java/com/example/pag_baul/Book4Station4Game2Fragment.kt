package com.example.pag_baul

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

data class TrueFalseQuestion(
    val question: String,
    val isTrue: Boolean
)

class Book4Station4Game2Fragment : Fragment() {

    private lateinit var tvQuestion: TextView
    private lateinit var ivCheck: ImageView
    private lateinit var ivX: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var btnDone: Button
    private lateinit var buttonsContainer: LinearLayout

    private var currentQuestionIndex = 0
    private var mediaPlayer: MediaPlayer? = null // For sound effects

    private val questions = listOf(
        TrueFalseQuestion("Masipag at nagtutulungan ang lahat ng isda sa Tagadtala.", true),
        TrueFalseQuestion("Si Lucia ay masipag at palaging tumutulong sa paglilinis.", false),
        TrueFalseQuestion("Galit ang Diyosa sa panlilinlang ni Lucia.", true),
        TrueFalseQuestion("Isinumpa si Lucia na maging pinakamagandang isda sa mundo.", false),
        TrueFalseQuestion("Ang mga janitor fish ay kilala sa kanilang pagiging pangit at tamad.", true) // <-- FIX IS HERE
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book4_station4_game2, container, false)

        // Initialize Views
        tvQuestion = view.findViewById(R.id.tvQuestionPlaceholder)
        ivCheck = view.findViewById(R.id.ivCheck)
        ivX = view.findViewById(R.id.ivX)
        btnBack = view.findViewById(R.id.btnBack)
        btnDone = view.findViewById(R.id.btnDone)
        buttonsContainer = view.findViewById(R.id.buttons_container)

        // Load the first question
        loadQuestion(currentQuestionIndex)

        // Set Click Listeners
        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        ivCheck.setOnClickListener { checkAnswer(true) }
        ivX.setOnClickListener { checkAnswer(false) }

        // Hide the "Done" button as navigation is automatic
        btnDone.visibility = View.GONE

        return view
    }

    private fun loadQuestion(index: Int) {
        if (index < questions.size) {
            tvQuestion.text = questions[index].question
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questions[currentQuestionIndex].isTrue
        showFeedbackDialog(userAnswer == correctAnswer)
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        // Prevent interaction while the dialog is showing
        ivCheck.isClickable = false
        ivX.isClickable = false

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)
        val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)

        btnDialogNext.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.setOnDismissListener {
            releaseMediaPlayer()
            // Re-enable interaction after dialog is gone
            ivCheck.isClickable = true
            ivX.isClickable = true
        }

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping)

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }

                val isLastQuestion = currentQuestionIndex >= questions.size - 1

                if (isLastQuestion) {
                    val bookFragment = BookFragment()
                    val bundle = Bundle()
                    bundle.putInt("BOOK_ID", 4)
                    bookFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, bookFragment)
                        .commit()
                } else {
                    // Otherwise, just load the next question
                    currentQuestionIndex++
                    loadQuestion(currentQuestionIndex)
                }
            }, 3000) // 1.5-second delay

        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww)

            dialog.show()

            // After a delay, just dismiss the dialog so the user can try again
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }, 3000)
        }
    }

    private fun playSound(soundId: Int) {
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }
}
