package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.FragmentEssayBinding // Import the binding class

class EssayFragment : Fragment() {

    // Setup ViewBinding for safety and convenience
    private var _binding: FragmentEssayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = FragmentEssayBinding.inflate(inflater, container, false)

        // --- START: CORRECTED CODE ---
        // 1. Get the data passed from BookFragment using the CORRECT keys.
        val titleText = arguments?.getString("title") ?: "Station"
        val questionText = arguments?.getString("question") ?: "Question goes here."
        // --- END: CORRECTED CODE ---

        // 2. Set the text dynamically using the binding object.
        binding.tvEssayTitle.text = titleText
        binding.tvEssayQuestion.text = questionText

        // 3. Back Button Logic
        binding.btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 4. Submit/Done Button Logic
        binding.btnSubmitEssay.setOnClickListener {
            val answer = binding.etEssayAnswer.text.toString()

            if (answer.isNotBlank()) {
                // In a real app, you would save the answer here
                Toast.makeText(context, "Mahusay! Ang iyong sagot ay naitala na.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Mangyaring isulat ang iyong sagot.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // This is a standard lifecycle method to prevent memory leaks with ViewBinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
