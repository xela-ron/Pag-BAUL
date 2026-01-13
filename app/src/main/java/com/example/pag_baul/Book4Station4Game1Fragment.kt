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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.FragmentBook4Station4Binding

data class ImageQuestion(
    val imageName: String,
    val options: List<String>,
    val correctAnswer: String
)

class Book4Station4Game1Fragment : Fragment() {

    private var _binding: FragmentBook4Station4Binding? = null
    private val binding get() = _binding!!

    private var containerId: Int = 0
    private var currentQuestionIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    private val questions = listOf(
        ImageQuestion("b4s41", listOf("Masayang pamumuhay ang lahat ng isda sa kaharian ng Tagadtala", "Nag-aaway araw-araw ang mga isda", "Si Lucia ay tamad at selosa", "Dumating ang Diyosa ng Karagatan"), "Masayang pamumuhay ang lahat ng isda sa kaharian ng Tagadtala"),
        ImageQuestion("b4s42", listOf("Masipag ang lahat ng isda", "Katamaran ni Lucia", "Ang Diyosa ay nagalit kay Lucia", "Naglalaro ang mga isda"), "Katamaran ni Lucia"),
        ImageQuestion("b4s43", listOf("Masayang pagtutulungan ng mga isda", "Pagdalo ng Diyosa sa kaharian", "Galit ng Diyosa sa panlilinlang ni Lucia", "Pagiging masipag ni Lucia"), "Galit ng Diyosa sa panlilinlang ni Lucia"),
        ImageQuestion("b4s44", listOf("Sumpa kay Lucia, nagiging pangit ang anyo", "Masayang pamumuhay ng mga isda", "Paglilinis ng kaharian para sa Diyosa", "Paglalaro ng mga isda"), "Sumpa kay Lucia, nagiging pangit ang anyo")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBook4Station4Binding.inflate(inflater, container, false)
        container?.let { containerId = it.id }

        loadQuestion(currentQuestionIndex)

        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.btnNext.setOnClickListener { checkAnswer() }

        return binding.root
    }

    private fun loadQuestion(index: Int) {
        val question = questions[index]
        val imageId = resources.getIdentifier(question.imageName, "drawable", requireContext().packageName)
        binding.ivQuestionImage.setImageResource(imageId)

        binding.rgChoices.removeAllViews()
        binding.rgChoices.clearCheck()
        question.options.forEach { option ->
            val radioButton = RadioButton(context).apply {
                text = option
                textSize = 16f
            }
            binding.rgChoices.addView(radioButton)
        }
    }

    private fun checkAnswer() {
        val selectedRadioButtonId = binding.rgChoices.checkedRadioButtonId
        if (selectedRadioButtonId == -1) {
            AlertDialog.Builder(requireContext())
                .setMessage("Please choose an answer first.")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val selectedRadioButton = binding.rgChoices.findViewById<RadioButton>(selectedRadioButtonId)
        val selectedAnswer = selectedRadioButton?.text.toString()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        showFeedbackDialog(selectedAnswer == correctAnswer)
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)


        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Add a listener that stops the sound when the dialog is dismissed
        dialog.setOnDismissListener {
            releaseMediaPlayer()
        }

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping) // Plays the clapping sound

            dialog.show()

            // This handler will automatically dismiss the dialog after 3 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss() // This will trigger the OnDismissListener
                }
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    loadQuestion(currentQuestionIndex)
                } else {
                    // --- CORRECTED CODE TO LAUNCH GAME 2 ---
                    // 1. Define the questions for the "Tama o Mali" game
                    val questionsForGame2 = ArrayList<TrueOrFalseQuestionData>().apply {
                        add(TrueOrFalseQuestionData("Masipag at nagtutulungan ang lahat ng isda sa Tagadtala.", "Tama"))
                        add(TrueOrFalseQuestionData("Si Lucia ay masipag at palaging tumutulong sa paglilinis.", "Mali"))
                        add(TrueOrFalseQuestionData("Galit ang Diyosa sa panlilinlang ni Lucia.", "Tama"))
                        add(TrueOrFalseQuestionData("Isinumpa si Lucia na maging pinakamagandang isda sa mundo.", "Mali"))
                        add(TrueOrFalseQuestionData("Ang mga janitor fish ay kilala sa kanilang pagiging pangit at tamad.", "Tama"))
                    }

                    // 2. Create the fragment instance using the newInstance method
                    val game2Fragment = Book4Station4Game2Fragment.newInstance(
                        title = "Station 4: Tama o Mali",
                        questions = questionsForGame2
                    )

                    // 3. Navigate to Game 2
                    parentFragmentManager.beginTransaction()
                        .replace(containerId, game2Fragment)
                        .addToBackStack(null)
                        .commit()
                    // --- END OF CORRECTED CODE ---
                }
            }, 3000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww) // Plays the "awww" sound

            dialog.show()

            // This handler will automatically dismiss the dialog after 3 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss() // This will trigger the OnDismissListener
                }
            }, 3000)
        }
    }

    private fun playSound(soundId: Int) {
        releaseMediaPlayer() // Stop any currently playing sound first
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()
    }

    private fun releaseMediaPlayer() {
        // Safely stop and release the media player
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer() // Important: release resources when the fragment is destroyed
        _binding = null
    }
}
