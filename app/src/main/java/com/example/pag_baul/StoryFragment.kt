package com.example.pag_baul

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class StoryFragment : Fragment() {

    private var text1: String? = null
    private var text2: String? = null
    private var isPage1 = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_story, container, false)

        val tvPageNumber = view.findViewById<TextView>(R.id.tvPageNumber)
        val tvStoryText = view.findViewById<TextView>(R.id.tvStoryText)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)

        // Enable scrolling for the text view
        tvStoryText.movementMethod = ScrollingMovementMethod()

        // 1. Get text passed from BookFragment
        text1 = arguments?.getString("STORY_PAGE1")
        text2 = arguments?.getString("STORY_PAGE2")

        // Initial UI Update
        updateUI(tvStoryText, tvPageNumber, btnNext, btnDone)

        // Handle Next Click
        btnNext.setOnClickListener {
            if (isPage1) {
                isPage1 = false
                updateUI(tvStoryText, tvPageNumber, btnNext, btnDone)
            }
        }

        // Handle Close
        val closeAction = View.OnClickListener {
            parentFragmentManager.popBackStack()
        }
        btnDone.setOnClickListener(closeAction)
        btnBackIcon.setOnClickListener(closeAction)

        return view
    }

    private fun updateUI(
        tvStoryText: TextView,
        tvPageNumber: TextView,
        btnNext: Button,
        btnDone: Button
    ) {
        if (isPage1) {
            tvStoryText.text = text1
            tvPageNumber.text = "Page 1 of 2"
            btnNext.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            // Scroll back to top when changing page
            tvStoryText.scrollTo(0, 0)
        } else {
            tvStoryText.text = text2
            tvPageNumber.text = "Page 2 of 2"
            btnNext.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            // Scroll back to top when changing page
            tvStoryText.scrollTo(0, 0)
        }
    }
}