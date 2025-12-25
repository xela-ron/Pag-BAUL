package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Station4Game2Fragment : Fragment() {

    private var selectedView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station4_game2, container, false)

        val drag1 = view.findViewById<ImageView>(R.id.drag1)
        val drag2 = view.findViewById<ImageView>(R.id.drag2)
        val drag3 = view.findViewById<ImageView>(R.id.drag3)
        val drag4 = view.findViewById<ImageView>(R.id.drag4)

        val target1 = view.findViewById<ImageView>(R.id.target1)
        val target2 = view.findViewById<ImageView>(R.id.target2)
        val target3 = view.findViewById<ImageView>(R.id.target3)
        val target4 = view.findViewById<ImageView>(R.id.target4)

        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnReset = view.findViewById<Button>(R.id.btnReset) // Find the new Reset button
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)

        val draggableViews = listOf(drag1, drag2, drag3, drag4)
        val dropTargets = listOf(target1, target2, target3, target4)

        val originalDrawables = draggableViews.map { it.drawable }

        val selectListener = View.OnClickListener { v ->
            if (v.alpha < 1.0f) { // Already used
                Toast.makeText(context, "This item has already been placed.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            selectedView = v
            Toast.makeText(context, "Item Selected! Now tap a Drop Zone.", Toast.LENGTH_SHORT).show()

            draggableViews.forEach { if (it.alpha == 1.0f) it.alpha = 1.0f } // Reset others
            v.alpha = 0.5f // Dim the selected one as feedback
        }

        draggableViews.forEach { it.setOnClickListener(selectListener) }

        val dropListener = View.OnClickListener { v ->
            val targetImageView = v as ImageView
            val selectedImageView = selectedView as? ImageView

            if (selectedImageView != null) {
                targetImageView.setImageDrawable(selectedImageView.drawable)
                selectedImageView.alpha = 0.2f // Mark as used, but keep visible
                selectedView = null

            } else {
                Toast.makeText(context, "Tap an image first!", Toast.LENGTH_SHORT).show()
            }
        }

        dropTargets.forEach { it.setOnClickListener(dropListener) }

        btnDone.setOnClickListener {
            // Add your logic here to check if the answer is correct
            Toast.makeText(context, "Checking answers...", Toast.LENGTH_SHORT).show()
        }

        // Set the OnClickListener for the Reset button
        btnReset.setOnClickListener {
            Toast.makeText(context, "Game Reset!", Toast.LENGTH_SHORT).show()

            draggableViews.forEachIndexed { index, imageView ->
                imageView.setImageDrawable(originalDrawables[index])
                imageView.alpha = 1.0f
            }

            dropTargets.forEach { 
                it.setImageResource(0) // Clear image
            }
        }

        btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
