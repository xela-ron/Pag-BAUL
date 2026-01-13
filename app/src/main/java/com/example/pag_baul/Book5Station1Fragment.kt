package com.example.pag_baul

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Book5Station1Fragment : Fragment() {

    private lateinit var playerToken: ImageView
    private lateinit var mazeContainer: FrameLayout
    private lateinit var pathDrawingView: PathDrawingView
    private lateinit var ivMaze: ImageView
    private var mediaPlayer: MediaPlayer? = null

    private var dX = 0f
    private var dY = 0f
    private var startX = 0f
    private var startY = 0f
    private var mazeBitmap: Bitmap? = null

    // New: Matrix to help with coordinate transformation
    private val imageMatrix = Matrix()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book5_station1, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        playerToken = view.findViewById(R.id.playerToken)
        mazeContainer = view.findViewById(R.id.mazeContainer)
        pathDrawingView = view.findViewById(R.id.pathDrawingView)
        ivMaze = view.findViewById(R.id.ivMaze)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        setupDragListener()

        mazeContainer.post {
            startX = mazeContainer.width * 0.20f
            startY = mazeContainer.height * 0.60f
            resetPosition()

            val drawable = ivMaze.drawable
            if (drawable is BitmapDrawable) {
                mazeBitmap = drawable.bitmap
            }
            // CRITICAL: Get the matrix that describes how the image is scaled and positioned
            imageMatrix.set(ivMaze.imageMatrix)
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
                    pathDrawingView.startPath(view.x + view.width / 2, view.y + view.height / 2)
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY
                    val tokenCenterX = newX + view.width / 2
                    val tokenCenterY = newY + view.height / 2

                    // Use the NEW isWall function for accurate collision detection
                    if (isWall(tokenCenterX, tokenCenterY)) {
                        return@setOnTouchListener true
                    }

                    val maxWidth = mazeContainer.width - view.width
                    val maxHeight = mazeContainer.height - view.height
                    view.x = newX.coerceIn(0f, maxWidth.toFloat())
                    view.y = newY.coerceIn(0f, maxHeight.toFloat())
                    pathDrawingView.extendPath(view.x + view.width / 2, view.y + view.height / 2)
                }
                MotionEvent.ACTION_UP -> {
                    checkIfFinished(view.x, view.y)
                }
            }
            true
        }
    }

    /**
     * NEW & IMPROVED isWall function.
     * This function correctly translates touch coordinates from the ImageView
     * to the actual scaled Bitmap, accounting for any letterboxing from scaleType="fitCenter".
     */
    private fun isWall(x: Float, y: Float): Boolean {
        val currentBitmap = mazeBitmap ?: return false

        // Invert the transformation matrix to map view coordinates back to bitmap coordinates.
        val inverseMatrix = Matrix()
        imageMatrix.invert(inverseMatrix)

        // Create an array for the coordinates to be transformed.
        val touchPoint = floatArrayOf(x, y)

        // Apply the inverse transformation.
        inverseMatrix.mapPoints(touchPoint)

        // touchPoint now holds the coordinates on the original, unscaled bitmap.
        val bitmapX = touchPoint[0].toInt()
        val bitmapY = touchPoint[1].toInt()

        // Proceed with the same boundary and color check as before.
        if (bitmapX >= 0 && bitmapX < currentBitmap.width && bitmapY >= 0 && bitmapY < currentBitmap.height) {
            val pixelColor = currentBitmap.getPixel(bitmapX, bitmapY)
            // A threshold of < 100 should work well for the dark gray lines in findtheway.png
            if (Color.red(pixelColor) < 100 && Color.green(pixelColor) < 100 && Color.blue(pixelColor) < 100) {
                return true // It's a wall.
            }
        }
        return false // Not a wall.
    }

    private fun checkIfFinished(x: Float, y: Float) {
        val finishXThreshold = mazeContainer.width * 0.60f
        val finishYThreshold = mazeContainer.height * 0.40f

        if (x > finishXThreshold && y < finishYThreshold) {
            if (!isWall(x + playerToken.width / 2, y + playerToken.height / 2)) {
                showFeedbackDialog()
            } else {
                resetPosition()
            }
        } else {
            resetPosition()
        }
    }

    private fun showFeedbackDialog() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.clapping)
        mediaPlayer?.start()

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_feedback, null)
        val ivEmoji = dialogView.findViewById<ImageView>(R.id.ivEmoji)
        val tvFeedback = dialogView.findViewById<TextView>(R.id.tvFeedback)

        // CORRECTED: Use android.app.AlertDialog
        val dialog = android.app.AlertDialog.Builder(requireContext())
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
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            parentFragmentManager.popBackStack()
        }, 3000)
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
