package com.example.pag_baul

import android.app.AlertDialog
import android.media.MediaPlayer // 1. Import MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

data class ImageQuestion(
    val imageName: String,
    val options: List<String>,
    val correctAnswer: String
)

class Book4Station4Game1Fragment : Fragment() {

    private lateinit var ivQuestionImage: ImageView
    private lateinit var rgChoices: RadioGroup
    private lateinit var btnNext: Button
    private lateinit var btnBack: ImageView

    private var currentQuestionIndex = 0
    private var mediaPlayer: MediaPlayer? = null // 2. Add mediaPlayer variable

    private val questions = listOf(
        ImageQuestion("b4s41", listOf("Masayang pamumuhay ang lahat ng isda sa kaharian ng Tagadtala", "Nag-aaway araw-araw ang mga isda", "Si Lucia ay tamad at selosa", "Dumating ang Diyosa ng Karagatan"), "Masayang pamumuhay ang lahat ng isda sa kaharian ng Tagadtala"),
        ImageQuestion("b4s42", listOf("Masipag ang lahat ng isda", "Katamaran ni Lucia", "Ang Diyosa ay nagalit kay Lucia", "Naglalaro ang mga isda"), "Katamaran ni Lucia"),
        ImageQuestion("b4s43", listOf("Masayang pagtutulungan ng mga isda", "Pagdalo ng Diyosa sa kaharian", "Galit ng Diyosa sa panlilinlang ni Lucia", "Pagiging masipag ni Lucia"), "Galit ng Diyosa sa panlilinlang ni Lucia"),
        ImageQuestion("b4s44", listOf("Sumpa kay Lucia, nagiging pangit ang anyo", "Masayang pamumuhay ng mga isda", "Paglilinis ng kaharian para sa Diyosa", "Paglalaro ng mga isda"), "Sumpa kay Lucia, nagiging pangit ang anyo")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book4_station4, container, false)

        ivQuestionImage = view.findViewById(R.id.ivQuestionImage)
        rgChoices = view.findViewById(R.id.rgChoices)
        btnNext = view.findViewById(R.id.btnNext)
        btnBack = view.findViewById(R.id.btnBack)

        loadQuestion(currentQuestionIndex)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        btnNext.setOnClickListener { checkAnswer() }

        return view
    }

    private fun loadQuestion(index: Int) {
        val question = questions[index]
        val imageId = resources.getIdentifier(question.imageName, "drawable", requireContext().packageName)
        ivQuestionImage.setImageResource(imageId)

        rgChoices.removeAllViews()
        rgChoices.clearCheck()
        question.options.forEach { option ->
            val radioButton = RadioButton(context)
            radioButton.text = option
            radioButton.textSize = 16f
            rgChoices.addView(radioButton)
        }
    }

    private fun checkAnswer() {
        val selectedRadioButtonId = rgChoices.checkedRadioButtonId
        if (selectedRadioButtonId == -1) {
            AlertDialog.Builder(requireContext())
                .setMessage("Please choose an answer first.")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val selectedRadioButton = view?.findViewById<RadioButton>(selectedRadioButtonId)
        val selectedAnswer = selectedRadioButton?.text.toString()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        if (selectedAnswer == correctAnswer) {
            showFeedbackDialog(true)
        } else {
            showFeedbackDialog(false)
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

        // 3. Set a listener to release the sound when the dialog is dismissed
        dialog.setOnDismissListener {
            releaseMediaPlayer()
        }

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping) // 4. Play clapping sound

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    loadQuestion(currentQuestionIndex)
                } else {
                    // Navigate to the next fragment when all questions are answered
                    (activity as? MainActivity)?.loadFragment(Book4Station4Game2Fragment())
                }
            }, 3000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww) // 5. Play "aww" sound

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }, 3000)
        }
    }

    // 6. Add the playSound function
    private fun playSound(soundId: Int) {
        releaseMediaPlayer() // Ensure any previous sound is stopped
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()
    }

    // 7. Add the releaseMediaPlayer function
    private fun releaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // 8. Add onStop to handle lifecycle changes
    override fun onStop() {
        super.onStop()
        releaseMediaPlayer() // Release player when the fragment is not visible
    }
}
