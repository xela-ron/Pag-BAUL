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
import androidx.fragment.app.FragmentManager
import com.example.pag_baul.databinding.FragmentBook4Station4Game2Binding

class Book4Station4Game2Fragment : Fragment() {

    private var _binding: FragmentBook4Station4Game2Binding? = null
    private val binding get() = _binding!!

    // UPDATED: Now uses the new, simpler data class
    private var questions: List<TrueOrFalseQuestionData> = listOf()
    private var currentQuestionIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    // Companion object to create a new instance of the fragment
    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_QUESTIONS = "questions"

        /**
         * Creates a new instance of this fragment with the provided title and questions.
         * This is the recommended way to create this fragment.
         */
        // UPDATED: The newInstance function now expects an ArrayList of the new data class
        fun newInstance(title: String, questions: ArrayList<TrueOrFalseQuestionData>): Book4Station4Game2Fragment {
            val fragment = Book4Station4Game2Fragment()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putParcelableArrayList(ARG_QUESTIONS, questions)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBook4Station4Game2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve arguments using the constants from the companion object
        val gameTitle = arguments?.getString(ARG_TITLE) ?: "Tama o Mali"
        // UPDATED: Retrieves the new data class from the arguments
        val receivedQuestions = arguments?.getParcelableArrayList<TrueOrFalseQuestionData>(ARG_QUESTIONS)

        binding.tvTitle.text = gameTitle

        if (!receivedQuestions.isNullOrEmpty()) {
            questions = receivedQuestions
            binding.tvQuestionPlaceholder.setTypeface(null, android.graphics.Typeface.NORMAL)
            displayQuestion()
        } else {
            binding.tvQuestionPlaceholder.text = "Walang nahanap na tanong para sa estasyong ito."
            // Disable buttons if there are no questions to prevent clicks
            binding.ivCheck.isEnabled = false
            binding.ivX.isEnabled = false
        }

        binding.ivCheck.setOnClickListener {
            checkAnswer("Tama")
        }
        binding.ivX.setOnClickListener {
            checkAnswer("Mali")
        }

        // --- MODIFICATION START ---
        // This listener will now also navigate back to the station map directly.
        binding.btnBack.setOnClickListener {
            // This clears the back stack until the named transaction is found.
            parentFragmentManager.popBackStack("STATION_MAP_TRANSACTION", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        // --- MODIFICATION END ---
    }

    private fun displayQuestion() {
        if (currentQuestionIndex < questions.size) {
            binding.tvQuestionPlaceholder.text = questions[currentQuestionIndex].question
        }
    }

    private fun checkAnswer(userAnswer: String) {
        if (questions.isEmpty() || currentQuestionIndex >= questions.size) return

        val correctAnswer = questions[currentQuestionIndex].answer
        val isCorrect = userAnswer.equals(correctAnswer, ignoreCase = true)

        showFeedbackDialog(isCorrect)
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
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
                    // This logic is already correct for your requirement.
                    Toast.makeText(context, "Natapos mo na ang istasyon!", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack("STATION_MAP_TRANSACTION", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }
        }, 1500)
    }

    private fun playSound(soundId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundId)?.apply {
            setOnCompletionListener { it.release() }
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
