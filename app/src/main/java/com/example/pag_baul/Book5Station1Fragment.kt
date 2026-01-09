package com.example.pag_baul

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Book5Station1Fragment : Fragment() {

    private lateinit var playerToken: ImageView
    private lateinit var mazeContainer: FrameLayout
    private lateinit var pathDrawingView: PathDrawingView
    private var mediaPlayer: MediaPlayer? = null

    private var dX = 0f
    private var dY = 0f

    // Starting position relative to container
    private var startX = 0f
    private var startY = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book5_station1, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        playerToken = view.findViewById(R.id.playerToken)
        mazeContainer = view.findViewById(R.id.mazeContainer)
        pathDrawingView = view.findViewById(R.id.pathDrawingView)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        // Setup touch listener for dragging
        setupDragListener()

        // Set initial position (Place in front of the horse)
        mazeContainer.post {
            // Adjust these coordinates to place the circle right in front of the horse
            // Moving Y up significantly as previous 0.75 was too low (in the whitespace)
            // Moving X slightly left
            startX = mazeContainer.width * 0.20f
            startY = mazeContainer.height * 0.60f

            resetPosition()
        }

        return view
    }

    private fun resetPosition() {
        playerToken.x = startX
        playerToken.y = startY
        pathDrawingView.clearPath()
        pathDrawingView.startPath(playerToken.x + playerToken.width / 2, playerToken.y + playerToken.height / 2)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDragListener() {
        playerToken.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY

                    // Start drawing path from center of token
                    pathDrawingView.startPath(view.x + view.width / 2, view.y + view.height / 2)
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY

                    val maxWidth = mazeContainer.width - view.width
                    val maxHeight = mazeContainer.height - view.height

                    view.x = newX.coerceIn(0f, maxWidth.toFloat())
                    view.y = newY.coerceIn(0f, maxHeight.toFloat())

                    // Update path trace
                    pathDrawingView.extendPath(view.x + view.width / 2, view.y + view.height / 2)
                }
                MotionEvent.ACTION_UP -> {
                    checkIfFinished(view.x, view.y)
                }
            }
            true
        }
    }

    private fun checkIfFinished(x: Float, y: Float) {
        // Define finish area (Top Right - Cave)
        // Widen the finish area to ensure it's easier to hit
        val finishXThreshold = mazeContainer.width * 0.60f // Right side
        val finishYThreshold = mazeContainer.height * 0.40f // Top side

        // Check if finished (Cave area)
        if (x > finishXThreshold && y < finishYThreshold) {
            showFeedbackDialog()
        } else {
            // If not finished, just reset silently as requested
            resetPosition()
        }
    }

    private fun showFeedbackDialog() {
        // Start playing the clapping sound
        mediaPlayer?.release() // Release any existing player
        mediaPlayer = MediaPlayer.create(context, R.raw.clapping)
        mediaPlayer?.start()

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)
        val btnDialogNext = dialogView.findViewById<Button>(R.id.btnDialogNext)

        btnDialogNext.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        ivEmoji.setImageResource(R.drawable.happy)
        tvFeedback.text = "Magaling! Nakaligtas ka!"

        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
            // Stop and release the media player when the dialog is dismissed
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            parentFragmentManager.popBackStack()
        }, 3000)
    }

    override fun onStop() {
        super.onStop()
        // Ensure media player is released when the fragment is stopped
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
