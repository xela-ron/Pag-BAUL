package com.example.pag_baul

import android.app.AlertDialog
import android.media.MediaPlayer
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
    private var mediaPlayer: MediaPlayer? = null
    // New flag to track if the game is successfully completed
    private var isGameCompleted = false

    private val userAnswers = mutableMapOf<Int, Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station4_game2, container, false)

        val drag1 = view.findViewById<ImageView>(R.id.drag1)
        val drag2 = view.findViewById<ImageView>(R.id.drag2)
        val drag3 = view.findViewById<ImageView>(R.id.drag3)
        val drag4 = view.findViewById<ImageView>(R.id.drag4)

        val resources = listOf(
            R.drawable.b1s4game2_1,
            R.drawable.b1s4game2_2,
            R.drawable.b1s4game2_3,
            R.drawable.b1s4game2_4
        )

        val draggableViews = listOf(drag1, drag2, drag3, drag4)
        val shuffledResources = resources.shuffled()

        draggableViews.forEachIndexed { index, imageView ->
            val resId = shuffledResources[index]
            imageView.setImageResource(resId)
            imageView.tag = resId
            imageView.alpha = 1.0f
        }

        val target1 = view.findViewById<ImageView>(R.id.target1)
        val target2 = view.findViewById<ImageView>(R.id.target2)
        val target3 = view.findViewById<ImageView>(R.id.target3)
        val target4 = view.findViewById<ImageView>(R.id.target4)

        target1.tag = R.drawable.b1s4game2_1
        target2.tag = R.drawable.b1s4game2_2
        target3.tag = R.drawable.b1s4game2_3
        target4.tag = R.drawable.b1s4game2_4

        val dropTargets = listOf(target1, target2, target3, target4)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnReset = view.findViewById<Button>(R.id.btnReset)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)

        val selectListener = View.OnClickListener { v ->
            // If game is won, disable selecting new images.
            if (isGameCompleted) return@OnClickListener

            if (v.alpha < 1.0f) {
                Toast.makeText(context, "This item has already been placed.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            selectedView = v
            draggableViews.forEach { if (it.alpha == 1.0f) it.alpha = 1.0f }
            v.alpha = 0.5f
        }

        draggableViews.forEach { it.setOnClickListener(selectListener) }

        val dropListener = View.OnClickListener { v ->
            // If game is won, disable dropping images.
            if (isGameCompleted) return@OnClickListener

            val targetImageView = v as ImageView
            val selectedImageView = selectedView as? ImageView

            if (selectedImageView != null) {
                targetImageView.setImageDrawable(selectedImageView.drawable)
                targetImageView.setTag(R.id.tag_image_res, selectedImageView.tag)
                selectedImageView.alpha = 0.2f
                selectedView = null
            } else {
                Toast.makeText(context, "Tap an image first!", Toast.LENGTH_SHORT).show()
            }
        }

        dropTargets.forEach { it.setOnClickListener(dropListener) }

        // --- CHANGE 1: The "DONE" button now has two functions ---
        // --- CHANGE 1: The "DONE" button now has two functions ---
        btnDone.setOnClickListener {
            if (isGameCompleted) {
                // If game is won, navigate back to the stations list.
                parentFragmentManager.popBackStack() // <-- THIS IS THE LINE TO CHANGE
            } else {
                // If game is not won, check the answers.
                checkAnswers(dropTargets)
            }
        }


        btnReset.setOnClickListener {
            // Reset the game completion flag
            isGameCompleted = false
            btnDone.isEnabled = true // Ensure the button is enabled again

            val newShuffled = resources.shuffled()
            draggableViews.forEachIndexed { index, imageView ->
                val resId = newShuffled[index]
                imageView.setImageResource(resId)
                imageView.tag = resId
                imageView.alpha = 1.0f
            }

            dropTargets.forEach {
                it.setImageDrawable(null)
                it.setTag(R.id.tag_image_res, null)
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
            val expectedResId = target.tag as? Int

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
            // --- CHANGE 2: Set the flag to true when answers are correct ---
            isGameCompleted = true
            showFeedbackDialog(true)
        } else {
            showFeedbackDialog(false)
        }
    }

    private fun playSound(soundResId: Int) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun showFeedbackDialog(isCorrect: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)
        dialogView.findViewById<Button>(R.id.btnDialogNext).visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping)
            dialog.show()

            // This handler now ONLY dismisses the dialog and stops the sound.
            Handler(Looper.getMainLooper()).postDelayed({
                // --- THE FIX ---
                // Stop the sound before dismissing the dialog
                mediaPlayer?.stop()
                if (dialog.isShowing) {
                    dialog.dismiss()
                    // Optional toast to guide the user
                    Toast.makeText(context, "Click DONE to continue.", Toast.LENGTH_SHORT).show()
                }
            }, 3000) // Using 3 seconds to give the clap sound enough time

        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww)
            dialog.show()

            // Auto-dismiss after 1.5 seconds for the "try again" case.
            Handler(Looper.getMainLooper()).postDelayed({
                // --- THE FIX ---
                // Stop the sound before dismissing the dialog
                mediaPlayer?.stop()
                if (dialog.isShowing) dialog.dismiss()
            }, 1500)
        }
    }

}
