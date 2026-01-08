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

class QuizFragment : Fragment() {

    // Default questions
    private var questionList = ArrayList<QuestionData>()

    // --- CHANGE 1: Add MediaPlayer variable ---
    private var mediaPlayer: MediaPlayer? = null

    // UI Elements
    private lateinit var tvQuestion: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnCheck: Button
    private lateinit var btnBack: ImageView
    private var currentQuestionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_dynamic, container, false)

        // Initialize UI
        tvQuestion = view.findViewById(R.id.tvQuestion)
        radioGroup = view.findViewById(R.id.radioGroup)
        btnCheck = view.findViewById(R.id.btnCheck)
        btnBack = view.findViewById(R.id.btnBackIcon)

        // 1. Get Questions from Bundle
        val questionsArg = arguments?.getParcelableArrayList<QuestionData>("QUESTIONS")

        if (questionsArg != null && questionsArg.isNotEmpty()) {
            questionList = questionsArg
        } else {
            loadBook1Defaults()
        }

        // 2. Load First Question
        loadQuestion()

        // 3. Handle Submit Button
        btnCheck.setOnClickListener {
            checkAnswer()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun loadQuestion() {
        if (questionList.isEmpty()) return

        val q = questionList[currentQuestionIndex]

        // Set Question Text
        tvQuestion.text = "${currentQuestionIndex + 1}. ${q.question}"

        // Clear previous options
        radioGroup.removeAllViews()
        radioGroup.clearCheck()

        // Add Options Dynamically
        val options = ArrayList<String>()
        if (q.option1.isNotEmpty()) options.add(q.option1)
        if (q.option2.isNotEmpty()) options.add(q.option2)
        if (q.option3.isNotEmpty()) options.add(q.option3)
        if (q.option4.isNotEmpty()) options.add(q.option4)

        for (option in options) {
            val rb = RadioButton(context)
            rb.text = option
            rb.textSize = 16f
            rb.setTextColor(Color.BLACK) // Set text color to BLACK
            rb.setPadding(16, 16, 16, 16)
            radioGroup.addView(rb)
        }

        // Update Button Text
        btnCheck.text = if (currentQuestionIndex == questionList.size - 1) "FINISH" else "NEXT"
    }

    private fun checkAnswer() {
        val selectedId = radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRb = view?.findViewById<RadioButton>(selectedId)
        val selectedText = selectedRb?.text.toString()
        val correctAnswer = questionList[currentQuestionIndex].answer

        if (selectedText == correctAnswer) {
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

        // Hide the button (same style as Book 4 Station 4 Game 2)
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

            // Auto-advance after 1.5 seconds
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
            // --- CHANGE 3: Play "aww" sound ---
            playSound(R.raw.awww)

            dialog.show()

            // Auto-dismiss after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
            }, 2000)
        }
    }

    private fun loadBook1Defaults() {
        // Fallback dummy question
        questionList.add(QuestionData("Sample Question?", "Yes", "No", "Maybe", "", "Yes"))
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
