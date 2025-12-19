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

        // Initial State (Page 1)
        page1.visibility = View.VISIBLE
        page2.visibility = View.GONE
        btnNext.visibility = View.VISIBLE
        btnDone.visibility = View.GONE
        btnPrev.visibility = View.INVISIBLE
        tvDots.text = "● ○" // First dot filled

        // Handle Next Click -> Go to Page 2
        btnNext.setOnClickListener {
            page1.visibility = View.GONE
            page2.visibility = View.VISIBLE

            // Switch buttons
            btnNext.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            btnPrev.visibility = View.VISIBLE // Show back arrow
            tvDots.text = "○ ●" // Second dot filled
        }

        // Handle Previous Click -> Go back to Page 1
        btnPrev.setOnClickListener {
            page2.visibility = View.GONE
            page1.visibility = View.VISIBLE

            // Switch buttons back
            btnNext.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnPrev.visibility = View.INVISIBLE
            tvDots.text = "● ○"
        }

        // Handle Done or Back Icon Click -> Exit
        val closeAction = View.OnClickListener {
            parentFragmentManager.popBackStack()
        }
        btnDone.setOnClickListener(closeAction)
        btnBackIcon.setOnClickListener(closeAction)

        return view
    }
}
