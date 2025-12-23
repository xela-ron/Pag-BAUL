package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment

class StoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_story, container, false)

        val page1 = view.findViewById<ScrollView>(R.id.layoutPage1)
        val page2 = view.findViewById<ScrollView>(R.id.layoutPage2)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnPrev = view.findViewById<Button>(R.id.btnPrev)
        val btnDone = view.findViewById<Button>(R.id.btnDoneStory)
        val tvDots = view.findViewById<TextView>(R.id.tvDots)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)

        // --- KEY FIX: FIND THE TEXT VIEWS AND UPDATE THEM ---
        val tvPage1 = view.findViewById<TextView>(R.id.tvStoryPage1)
        val tvPage2 = view.findViewById<TextView>(R.id.tvStoryPage2)

        // 1. Get text passed from BookFragment
        val text1 = arguments?.getString("STORY_PAGE1")
        val text2 = arguments?.getString("STORY_PAGE2")

        // 2. If text was passed (like for Book 2), use it.
        if (text1 != null) {
            tvPage1.text = text1
        }
        if (text2 != null) {
            tvPage2.text = text2
        }
        // ----------------------------------------------------

        // Initial State (Page 1)
        page1.visibility = View.VISIBLE
        page2.visibility = View.GONE
        btnNext.visibility = View.VISIBLE
        btnDone.visibility = View.GONE
        btnPrev.visibility = View.INVISIBLE
        tvDots.text = "● ○"

        // Handle Next Click
        btnNext.setOnClickListener {
            page1.visibility = View.GONE
            page2.visibility = View.VISIBLE
            btnNext.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            btnPrev.visibility = View.VISIBLE
            tvDots.text = "○ ●"
        }

        // Handle Previous Click
        btnPrev.setOnClickListener {
            page2.visibility = View.GONE
            page1.visibility = View.VISIBLE
            btnNext.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnPrev.visibility = View.INVISIBLE
            tvDots.text = "● ○"
        }

        // Handle Close
        val closeAction = View.OnClickListener {
            parentFragmentManager.popBackStack()
        }
        btnDone.setOnClickListener(closeAction)
        btnBackIcon.setOnClickListener(closeAction)

        return view
    }
}
