package com.example.pag_baul

import android.app.AlertDialog
import android.graphics.Paint
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
        
        // Done button listener
        btnDone.setOnClickListener {
             showFeedbackDialog(true)
        }

        // Clear map to avoid duplicates if view is recreated
        wordToTextViewMap.clear()

        // Grid matching the 12x12 screenshot exactly
        val grid = listOf(
            "JEFFMABAITLK",
            "APQRTYUIOPAS",
            "SAROBARUMBAD",
            "ASDFGHJKLZXC",
            "HELIGTASWQER",
            "EBATAMATANMN",
            "OLICEKALSADA",
            "SRESPONSABLE",
            "JEEPBATASTRA",
            "UMALINISQWER",
            "MABUTINGJEEP",
            "PASAHEROSYUI"
        )

        val wordsToFind = listOf(
            "JEFF", "MABAIT", "PASAHERO", "RESPONSABLE", "LIGTAS",
            "BATAS", "KALSADA", "JEEP", "SARO", "MALINIS"
        )

        // Populate the "Words to Find" list
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
            // Disable back button and enable done button
            btnBack.isEnabled = false
            btnBack.alpha = 0.5f
            btnDone.visibility = View.VISIBLE
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
            
            dialog.show()

            // Auto-advance/complete after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(context, "Station Completed!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() 
            }, 1500)
        } else {
            // Usually not reachable for Word Search "Done" button, but handled just in case
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            
            dialog.show()

            // Auto-dismiss after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
            }, 1500)
        }
    }
}
