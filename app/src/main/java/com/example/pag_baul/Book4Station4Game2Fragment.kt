package com.example.pag_baul

import android.app.AlertDialog
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
    private val questions = listOf(
        TrueFalseQuestion("Masipag at nagtutulungan ang lahat ng isda sa Tagadtala.", true),
        TrueFalseQuestion("Si Lucia ay masipag at palaging tumutulong sa paglilinis.", false),
        TrueFalseQuestion("Galit ang Diyosa sa panlilinlang ni Lucia.", true),
        TrueFalseQuestion("Isinumpa si Lucia na maging pinakamagandang isda sa mundo.", false),
        TrueFalseQuestion("Ang mga janitor fish ay kilala sa kanilang pagiging pangit at tamad.", true)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book4_station4_game2, container, false)

        tvQuestion = view.findViewById(R.id.tvQuestionPlaceholder)
        ivCheck = view.findViewById(R.id.ivCheck)
        ivX = view.findViewById(R.id.ivX)
        btnBack = view.findViewById(R.id.btnBack)
        btnDone = view.findViewById(R.id.btnDone)
        buttonsContainer = view.findViewById(R.id.buttons_container)

        loadQuestion(currentQuestionIndex)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        
        ivCheck.setOnClickListener { checkAnswer(true) }
        ivX.setOnClickListener { checkAnswer(false) }

        btnDone.setOnClickListener { 
            parentFragmentManager.popBackStack() 
        }

        return view
    }

    private fun loadQuestion(index: Int) {
        tvQuestion.text = questions[index].question
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questions[currentQuestionIndex].isTrue
        showFeedbackDialog(userAnswer == correctAnswer)
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)
        val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)

        // Hide the button as requested
        btnDialogNext.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            
            dialog.show()

            // Auto-advance after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
                
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    loadQuestion(currentQuestionIndex)
                } else {
                    showGameComplete()
                }
            }, 1500)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            
            dialog.show()

            // Auto-dismiss after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
            }, 1500)
        }
    }

    private fun showGameComplete() {
        tvQuestion.text = "Mahusay! Natapos mo na ang laro."
        buttonsContainer.visibility = View.GONE
        btnDone.visibility = View.VISIBLE
    }
}
