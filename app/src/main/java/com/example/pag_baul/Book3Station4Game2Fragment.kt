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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.FragmentBook3Station4Game2Binding

class Book3Station4Game2Fragment : Fragment() {

    private var _binding: FragmentBook3Station4Game2Binding? = null
    private val binding get() = _binding!!

    private var currentPassengerView: ImageView? = null
    private var correctAnswer: String? = null
    private var mediaPlayer: MediaPlayer? = null
    private var hasAnsweredCorrectly = false

    private val passengerViews by lazy {
        listOf(
            binding.pKalsada,
            binding.pJeef,
            binding.pPasahero,
            binding.pTulong,
            binding.pMabait,
            binding.pBarangay
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBook3Station4Game2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passengerViews.forEach { passenger ->
            passenger.setOnClickListener { onPassengerClick(it) }
        }

        binding.btnSubmit.setOnClickListener { checkAnswer() }
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun onPassengerClick(view: View) {
        if (view !is ImageView || binding.gamePopup.isVisible || hasAnsweredCorrectly) return

        currentPassengerView = view
        val tagData = view.tag.toString().split(',')
        correctAnswer = tagData.getOrNull(0)
        val jumbledWord = tagData.getOrNull(1)

        if (correctAnswer != null && jumbledWord != null) {
            showPopup(jumbledWord)
        }
    }

    private fun showPopup(jumbledWord: String) {
        setAllPassengersClickable(false)
        binding.gamePopup.isVisible = true
        binding.tvJumbledWord.text = jumbledWord
        binding.etAnswer.text.clear()
    }

    private fun checkAnswer() {
        val userAnswer = binding.etAnswer.text.toString().trim()

        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
            hasAnsweredCorrectly = true // Mark as answered
            showFeedbackDialog(true)
        } else {
            showFeedbackDialog(false)
        }
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)

        // --- FIX: REMOVED THE LINES THAT REFERENCE THE NON-EXISTENT BUTTON ---
        // val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)
        // btnDialogNext?.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            playSound(R.raw.clapping)
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                parentFragmentManager.popBackStack("book_map", 2)
            }, 2000)

        } else {
            playSound(R.raw.awww)
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"

            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                closePopup()
            }, 2000)
        }
    }

    // This function is no longer called on correct answer with the new logic.
    private fun movePassengerToJeep() {
        currentPassengerView?.let { passenger ->
            (passenger.parent as? ViewGroup)?.removeView(passenger)
            binding.jeepPassengersContainer.addView(passenger)
            passenger.layoutParams = LinearLayout.LayoutParams(
                100,
                100
            ).apply {
                marginEnd = 8
            }
        }
    }

    private fun playSound(soundId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()
    }

    private fun closePopup() {
        binding.gamePopup.isVisible = false
        currentPassengerView = null
        correctAnswer = null
        setAllPassengersClickable(true)
    }

    private fun setAllPassengersClickable(isClickable: Boolean) {
        passengerViews.forEach { view ->
            if (view.parent != binding.jeepPassengersContainer) {
                view.isClickable = isClickable
                view.alpha = if (isClickable) 1.0f else 0.5f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
