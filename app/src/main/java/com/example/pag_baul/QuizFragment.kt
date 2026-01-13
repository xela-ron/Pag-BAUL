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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.FragmentQuizDynamicBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizDynamicBinding? = null
    private val binding get() = _binding!!

    private var questionList = ArrayList<QuestionData>()
    private var mediaPlayer: MediaPlayer? = null
    private var currentQuestionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizDynamicBinding.inflate(inflater, container, false)

        val questionsArg = arguments?.getParcelableArrayList<QuestionData>("questions")
        val stationTitle = arguments?.getString("title") ?: "Quiz"

        // binding.tvQuizTitle.text = stationTitle // Uncomment if you add a title TextView

        if (questionsArg != null && questionsArg.isNotEmpty()) {
            questionList = questionsArg
        } else {
            loadBook1Defaults()
        }

        loadQuestion()

        binding.btnCheck.setOnClickListener {
            checkAnswer()
        }

        binding.btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun loadQuestion() {
        if (questionList.isEmpty()) {
            binding.tvQuestion.text = "No questions found for this station."
            return
        }

        val q = questionList[currentQuestionIndex]
        binding.tvQuestion.text = "${currentQuestionIndex + 1}. ${q.question}"

        binding.radioGroup.removeAllViews()
        binding.radioGroup.clearCheck()

        val options = ArrayList<String>()
        if (q.option1.isNotEmpty()) options.add(q.option1)
        if (q.option2.isNotEmpty()) options.add(q.option2)
        if (q.option3.isNotEmpty()) options.add(q.option3)
        if (q.option4.isNotEmpty()) options.add(q.option4)

        for (option in options) {
            val rb = RadioButton(context).apply {
                text = option
                textSize = 16f
                setTextColor(Color.BLACK)
                setPadding(16, 16, 16, 16)
            }
            binding.radioGroup.addView(rb)
        }

        binding.btnCheck.text = if (currentQuestionIndex == questionList.size - 1) "FINISH" else "NEXT"
    }

    private fun checkAnswer() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRb = binding.radioGroup.findViewById<RadioButton>(selectedId)
        val selectedText = selectedRb?.text.toString()

        // --- START: CORRECTED CODE ---
        // Use the confirmed property name 'answer' from your LATEST QuestionData.kt file
        val correctAnswer = questionList[currentQuestionIndex].answer
        // --- END: CORRECTED CODE ---

        if (selectedText.equals(correctAnswer, ignoreCase = true)) {
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

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping)

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()

                if (currentQuestionIndex < questionList.size - 1) {
                    currentQuestionIndex++
                    loadQuestion()
                } else {
                    Toast.makeText(context, "Quiz Completed!", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                }
            }, 2000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww)

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
            }, 2000)
        }
    }

    private fun loadBook1Defaults() {
        questionList.clear()
        // Corrected to match the QuestionData structure (assuming 3 options, 1 empty, and the answer)
        questionList.add(QuestionData("Sample Question: Is this a fallback?", "Yes", "No", "Maybe", "", "Yes"))
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

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
