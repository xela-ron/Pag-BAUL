package com.example.pag_baul

import android.app.AlertDialog
import android.media.MediaPlayer // 1. Import MediaPlayer
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
import com.example.pag_baul.databinding.FragmentBook4Station4Game2Binding

class Book4Station4Game2Fragment : Fragment() {

    private var _binding: FragmentBook4Station4Game2Binding? = null
    private val binding get() = _binding!!

    private var questions: ArrayList<QuestionData> = ArrayList()
    private var currentQuestionIndex = 0

    // 2. Add a MediaPlayer variable
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBook4Station4Game2Binding.inflate(inflater, container, false)

        val gameTitle = arguments?.getString("title") ?: "Tama o Mali"
        val receivedQuestions = arguments?.getParcelableArrayList<QuestionData>("questions")

        binding.tvTitle.text = gameTitle

        if (receivedQuestions != null && receivedQuestions.isNotEmpty()) {
            questions = receivedQuestions
            displayQuestion()
        } else {
            binding.tvQuestionPlaceholder.text = "Walang nahanap na tanong para sa estasyong ito."
        }

        binding.ivCheck.setOnClickListener {
            checkAnswer("Tama")
        }
        binding.ivX.setOnClickListener {
            checkAnswer("Mali")
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun displayQuestion() {
        if (currentQuestionIndex < questions.size) {
            binding.tvQuestionPlaceholder.text = questions[currentQuestionIndex].question
        }
    }

    private fun checkAnswer(userAnswer: String) {
        if (questions.isEmpty()) return

        val correctAnswer = questions[currentQuestionIndex].answer
        val isCorrect = userAnswer.equals(correctAnswer, ignoreCase = true)

        showFeedbackDialog(isCorrect)
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)

        // --- FIX: REMOVED THE LINES THAT REFERENCE THE NON-EXISTENT BUTTON ---
        // val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)
        // btnDialogNext.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            // 3. Play clapping sound
            playSound(R.raw.clapping)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            // 3. Play "aww" sound
            playSound(R.raw.awww)
        }

        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
            if (isCorrect) {
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    displayQuestion()
                } else {
                    Toast.makeText(context, "Natapos mo na ang laro!", Toast.LENGTH_LONG).show()
                    // This is a more standard way to pop back to the previous screen
                    parentFragmentManager.popBackStack()
                }
            }
        }, 1500)
    }

    // 4. Add the playSound function
    private fun playSound(soundId: Int) {
        // Stop any previously playing sound
        mediaPlayer?.release()
        mediaPlayer = null

        // Create and start a new media player
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 5. Release media player resources to prevent memory leaks
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
