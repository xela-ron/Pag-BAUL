package com.example.pag_baul

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Book3Station1Fragment : Fragment() {

    private val wordToTextViewMap = mutableMapOf<String, TextView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book3_station1, container, false)

        val wordSearchView = view.findViewById<WordSearchView>(R.id.wordSearchView)
        // UPDATED: Find the GridLayout
        val wordsContainer = view.findViewById<GridLayout>(R.id.words_to_find_container)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        val grid = listOf(
            "JEFFMABAITLKS",
            "APQRTYUIOPAS",
            "SAROBARUMBAD",
            "ASDFGHJKLZXC",
            "HELIGTASWQER",
            "EBATAMATANMN",
            "RPOLICEKALSAA",
            "OSRESPONSABL",
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
            val textView = TextView(context).apply {
                text = "• $word" // Add a bullet point
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
                // Set layout params for GridLayout
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // Make columns equal width
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
            if (wordToTextViewMap.keys.all { it in wordSearchView.foundWords }) {
                Toast.makeText(context, "Congratulations! You found all the words!", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }
}
