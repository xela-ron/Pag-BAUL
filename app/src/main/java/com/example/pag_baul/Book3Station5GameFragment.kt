package com.example.pag_baul

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.FragmentBook3Station5GameBinding

// A simple data class for our questions
data class ResponsableQuestion(val text: String, val isResponsable: Boolean)

class Book3Station5GameFragment : Fragment() {

    private var _binding: FragmentBook3Station5GameBinding? = null
    private val binding get() = _binding!!

    private var currentQuestionIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    // List of questions for this game
    private val questions = listOf(
        ResponsableQuestion("Humihinto si Jeff kapag pula ang ilaw trapiko.", isResponsable = true),
        ResponsableQuestion("Nakikipagkarera si Saro sa ibang jeep sa kalsada.", isResponsable = false),
        ResponsableQuestion("Pinapaalalahanan ni Jeff ang mga bata na mag-ingat sa pagsakay.", isResponsable = true),
        ResponsableQuestion("Uminom ng alak si Saro bago magmaneho.", isResponsable = false),
        ResponsableQuestion("Tinulungan ni Jeff si Saro kahit nasaktan siya.", isResponsable = true)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBook3Station5GameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadQuestion()

        // --- FIX: Correct the ID from btnBack to btnBackIcon ---
        binding.btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnResponsable.setOnClickListener {
            checkAnswer(isResponsable = true)
        }

        binding.btnHindiResponsable.setOnClickListener {
            checkAnswer(isResponsable = false)
        }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            binding.tvQuestionText.text = question.text
        }
    }

    private fun checkAnswer(isResponsable: Boolean) {
        // Prevent multiple clicks while feedback is showing
        binding.btnResponsable.isEnabled = false
        binding.btnHindiResponsable.isEnabled = false

        val correctAnswer = questions[currentQuestionIndex].isResponsable
        val isCorrect = (isResponsable == correctAnswer)
        showFeedbackDialog(isCorrect)
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.setOnDismissListener {
            releaseMediaPlayer()
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
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    loadQuestion()
                    // Re-enable buttons for the next question
                    binding.btnResponsable.isEnabled = true
                    binding.btnHindiResponsable.isEnabled = true
                } else {
                    // Game finished
                    Toast.makeText(context, "Natapos mo na ang laro!", Toast.LENGTH_LONG).show()
                    // Use popBackStack() without arguments to go to the previous screen
                    parentFragmentManager.popBackStack()
                }
            }, 2000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww)
            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                // Re-enable buttons so the user can try again
                binding.btnResponsable.isEnabled = true
                binding.btnHindiResponsable.isEnabled = true
            }, 2000)
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

    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer()
        _binding = null
    }
}
