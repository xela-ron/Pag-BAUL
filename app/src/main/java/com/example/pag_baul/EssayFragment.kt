package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class EssayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_essay, container, false)

        // 1. Get the data passed from BookFragment
        // If no data is passed, use default text
        val titleText = arguments?.getString("ESSAY_TITLE") ?: "Station 6."
        val questionText = arguments?.getString("ESSAY_QUESTION") ?: "Kung ikaw si Willy, ano ang gagawin mo upang matulungan ang iyong ina?"

        // 2. Find Views
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)
        val btnDone = view.findViewById<Button>(R.id.btnDoneEssay)

        // 3. Set the text dynamically
        tvTitle.text = titleText
        tvQuestion.text = questionText

        // Close logic
        val closeAction = View.OnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnBackIcon.setOnClickListener(closeAction)
        btnDone.setOnClickListener(closeAction)

        return view
    }
}
