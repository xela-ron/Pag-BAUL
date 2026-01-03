package com.example.pag_baul

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Station4Game2Fragment : Fragment() {

    private var selectedView: View? = null
    // Maps Drop Target ID -> Index of Correct Source Image (0-based from draggableViews)
    // The user said: "the correct placements are already based on the name of the image after the underscore"
    // b1s4game2_1.png should go to target 1? 
    // Let's assume draggableViews are initialized with specific drawables. 
    // We need to track which drawable ended up in which target.
    // Instead of complex tracking, we can store tags on the ImageViews.

    private val userAnswers = mutableMapOf<Int, Int>() // TargetViewId -> SourceViewId (or some identifier)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station4_game2, container, false)

        val drag1 = view.findViewById<ImageView>(R.id.drag1)
        val drag2 = view.findViewById<ImageView>(R.id.drag2)
        val drag3 = view.findViewById<ImageView>(R.id.drag3)
        val drag4 = view.findViewById<ImageView>(R.id.drag4)

        // Set tags to identify the correct order/content
        // The user says "correct placements are already based on the name of the image after the underscore".
        // b1s4game2_1.png -> Should be 1st
        // b1s4game2_2.png -> Should be 2nd
        // b1s4game2_3.png -> Should be 3rd
        // b1s4game2_4.png -> Should be 4th
        
        // We need to make sure the "draggableViews" list is shuffled or "jumbled" as the user requested.
        // But first, let's assign the correct tags based on what they hold initially.
        // Assuming the layout XML loads them in order, or we set them programmatically.
        // If the XML has them in order, we should shuffle them here.
        
        val resources = listOf(
            R.drawable.b1s4game2_1,
            R.drawable.b1s4game2_2,
            R.drawable.b1s4game2_3,
            R.drawable.b1s4game2_4
        )

        val draggableViews = listOf(drag1, drag2, drag3, drag4)
        
        // Jumble the images on the left
        val shuffledResources = resources.shuffled()
        
        draggableViews.forEachIndexed { index, imageView ->
            val resId = shuffledResources[index]
            imageView.setImageResource(resId)
            imageView.tag = resId // Store the resource ID as the tag to identify it later
            imageView.alpha = 1.0f
        }

        val target1 = view.findViewById<ImageView>(R.id.target1)
        val target2 = view.findViewById<ImageView>(R.id.target2)
        val target3 = view.findViewById<ImageView>(R.id.target3)
        val target4 = view.findViewById<ImageView>(R.id.target4)

        // Map targets to the expected correct resource ID
        // Target 1 expects b1s4game2_1, etc.
        target1.tag = R.drawable.b1s4game2_1
        target2.tag = R.drawable.b1s4game2_2
        target3.tag = R.drawable.b1s4game2_3
        target4.tag = R.drawable.b1s4game2_4

        val dropTargets = listOf(target1, target2, target3, target4)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnReset = view.findViewById<Button>(R.id.btnReset)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)

        val selectListener = View.OnClickListener { v ->
            if (v.alpha < 1.0f) { // Already used
                Toast.makeText(context, "This item has already been placed.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            selectedView = v
            // Optional: Visual feedback for selection
             draggableViews.forEach { if (it.alpha == 1.0f) it.alpha = 1.0f } // Reset others
            v.alpha = 0.5f 
        }

        draggableViews.forEach { it.setOnClickListener(selectListener) }

        val dropListener = View.OnClickListener { v ->
            val targetImageView = v as ImageView
            val selectedImageView = selectedView as? ImageView

            if (selectedImageView != null) {
                targetImageView.setImageDrawable(selectedImageView.drawable)
                // Store which resource is now in this target (from the source's tag)
                targetImageView.setTag(R.id.tag_image_res, selectedImageView.tag) 
                
                selectedImageView.alpha = 0.2f // Mark as used
                selectedView = null
            } else {
                Toast.makeText(context, "Tap an image first!", Toast.LENGTH_SHORT).show()
            }
        }

        dropTargets.forEach { it.setOnClickListener(dropListener) }

        btnDone.setOnClickListener {
            checkAnswers(dropTargets)
        }

        btnReset.setOnClickListener {
            // Re-shuffle and reset
            val newShuffled = resources.shuffled()
            draggableViews.forEachIndexed { index, imageView ->
                val resId = newShuffled[index]
                imageView.setImageResource(resId)
                imageView.tag = resId
                imageView.alpha = 1.0f
            }
            
            dropTargets.forEach { 
                it.setImageDrawable(null)
                it.setTag(R.id.tag_image_res, null) // Clear the answer tag
            }
        }

        btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun checkAnswers(targets: List<ImageView>) {
        var allCorrect = true
        var allFilled = true

        for (target in targets) {
            val currentContentResId = target.getTag(R.id.tag_image_res) as? Int
            val expectedResId = target.tag as? Int // The correct answer stored in standard tag

            if (currentContentResId == null) {
                allFilled = false
                break
            }

            if (currentContentResId != expectedResId) {
                allCorrect = false
            }
        }

        if (!allFilled) {
             Toast.makeText(context, "Please place all images first.", Toast.LENGTH_SHORT).show()
             return
        }

        if (allCorrect) {
            showFeedbackDialog(true)
        } else {
             showFeedbackDialog(false)
        }
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)
        val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)

        // Hide the button as per similar behavior in Book 4 Station 4 Game 2
        btnDialogNext.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            
            dialog.show()

            // Auto-dismiss/navigate after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(context, "Game Completed!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() 
            }, 1500)
        } else {
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
