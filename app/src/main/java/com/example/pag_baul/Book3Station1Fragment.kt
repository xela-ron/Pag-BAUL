package com.example.pag_baul

import android.graphics.Paint
import android.os.Bundle
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
             Toast.makeText(requireContext(), "Great Job!", Toast.LENGTH_SHORT).show()
             parentFragmentManager.popBackStack()
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
            Toast.makeText(requireContext(), "Congratulations! You found all the words!", Toast.LENGTH_LONG).show()
            // Disable back button and enable done button
            btnBack.isEnabled = false
            btnBack.alpha = 0.5f
            btnDone.visibility = View.VISIBLE
        }
    }
}
