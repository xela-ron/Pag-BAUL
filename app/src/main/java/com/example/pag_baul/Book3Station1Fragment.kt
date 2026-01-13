package com.example.pag_baul

import android.app.AlertDialog
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Bundle


import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class Book3Station1Fragment : Fragment() {

    private val wordToTextViewMap = mutableMapOf<String, TextView>()
    private lateinit var btnDone: Button
    private lateinit var btnBack: ImageView
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book3_station1, container, false)

        val wordSearchView = view.findViewById<WordSearchView>(R.id.wordSearchView)
        val wordsContainer = view.findViewById<GridLayout>(R.id.words_to_find_container)
        btnBack = view.findViewById(R.id.btnBack)
        btnDone = view.findViewById(R.id.btnDone)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        btnDone.setOnClickListener {
            showFeedbackDialog(true)
        }

        wordToTextViewMap.clear()

        val grid = listOf(
            "JEFFMABAITLK", "APQRTYUIOPAS", "SAROBARUMBAD", "ASDFGHJKLZXC",
            "HELIGTASWQER", "EBATAMATANMN", "OLICEKALSADA", "SRESPONSABLE",
            "JEEPBATASTRA", "UMALINISQWER", "MABUTINGJEEP", "PASAHEROSYUI"
        )

        val wordsToFind = listOf(
            "JEFF", "MABAIT", "PASAHERO", "RESPONSABLE", "LIGTAS",
            "BATAS", "KALSADA", "JEEP", "SARO", "MALINIS"
        )

        wordsToFind.forEach { word ->
            val textView = TextView(requireContext()).apply {
                text = "• $word"
                textSize = 16f
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            }
            wordsContainer.addView(textView)
            wordToTextViewMap[word.uppercase()] = textView
        }

        wordSearchView.setGrid(grid, wordsToFind)

        wordSearchView.onWordFound = { foundWord ->
            val textView = wordToTextViewMap[foundWord.uppercase()]
            textView?.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                alpha = 0.5f
            }
            checkIfAllWordsFound(wordSearchView)
        }

        return view
    }

    private fun checkIfAllWordsFound(wordSearchView: WordSearchView) {
        if (wordToTextViewMap.keys.all { wordSearchView.foundWords.contains(it.uppercase()) }) {
            btnBack.isEnabled = false
            btnBack.alpha = 0.5f
            btnDone.visibility = View.VISIBLE
        }
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
            playSound(R.raw.clapping)
            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(context, "Station Completed!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
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

    private fun playSound(soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
