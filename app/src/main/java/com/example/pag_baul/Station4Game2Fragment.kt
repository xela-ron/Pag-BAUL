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
// --- IMPORT THE BOOKFRAGMENT ---

class Station4Game2Fragment : Fragment() {

    private var selectedView: View? = null
    private var mediaPlayer: MediaPlayer? = null
    // New flag to track if the game is successfully completed
    private var isGameCompleted = false

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
            R.drawable.b1s4_game2_1,
            R.drawable.b1s4_game2_2,
            R.drawable.b1s4_game2_3,
            R.drawable.b1s4_game2_4
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

        target1.tag = R.drawable.b1s4_game2_1
        target2.tag = R.drawable.b1s4_game2_2
        target3.tag = R.drawable.b1s4_game2_3
        target4.tag = R.drawable.b1s4_game2_4

        val dropTargets = listOf(target1, target2, target3, target4)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnReset = view.findViewById<Button>(R.id.btnReset)
        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)

        // --- HIDE THE DONE BUTTON, as it is no longer needed for automatic navigation ---
        btnDone.visibility = View.GONE

        val selectListener = View.OnClickListener { v ->
            if (isGameCompleted) return@OnClickListener

            if (v.alpha < 1.0f) {
                Toast.makeText(context, "This item has already been placed.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            selectedView = v
            // This loop is redundant, we can remove it.
            v.alpha = 0.5f
        }

        draggableViews.forEach { it.setOnClickListener(selectListener) }

        val dropListener = View.OnClickListener { v ->
            if (isGameCompleted) return@OnClickListener

            val targetImageView = v as ImageView
            val selectedImageView = selectedView as? ImageView

            if (selectedImageView != null) {
                targetImageView.setImageDrawable(selectedImageView.drawable)
                targetImageView.setTag(R.id.tag_image_res, selectedImageView.tag)
                selectedImageView.alpha = 0.2f
                selectedView = null
                // Show the "Check Answer" button (which is btnDone) after placing an item.
                btnDone.visibility = View.VISIBLE
                btnDone.text = "CHECK ANSWER"
            } else {
                Toast.makeText(context, "Tap an image first!", Toast.LENGTH_SHORT).show()
            }
        }

        dropTargets.forEach { it.setOnClickListener(dropListener) }

        // --- The "Check Answer" button ---
        btnDone.setOnClickListener {
            // This button now only checks the answers. Navigation is handled in the dialog.
            checkAnswers(dropTargets)
        }

        btnReset.setOnClickListener {
            isGameCompleted = false
            btnDone.visibility = View.GONE // Hide button on reset

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
            isGameCompleted = true
            showFeedbackDialog(true)
        } else {
            showFeedbackDialog(false)
        }
    }

    private fun navigateToBook1Stations() {
        val bookFragment = BookFragment()
        val bundle = Bundle()
        // Tell the BookFragment to load the data for Book 1
        bundle.putInt("BOOK_ID", 1)
        bookFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, bookFragment) // Use your main fragment container ID
            .commit()
    }

    private fun playSound(soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId)?.apply {
            setOnCompletionListener { mp ->
                mp.release()
                mediaPlayer = null
            }
            start()
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

        dialog.setOnDismissListener {
            mediaPlayer?.stop()
        }

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping)
            dialog.show()

            // --- FIX: This is the key change for automatic navigation ---
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                // Automatically navigate to Book 1's stations after the dialog closes.
                navigateToBook1Stations()
            }, 2000) // 2-second delay

        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww)
            dialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
            }, 2000) // 2-second delay
        }
    }
}
