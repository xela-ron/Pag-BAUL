package com.example.pag_baul

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint // <--- Fixed typo here
import android.os.Bundle
import android.view.LayoutInflater // <--- Added missing import
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment

class MazeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create a FrameLayout to hold the MazeView
        val layout = FrameLayout(requireContext())
        layout.setBackgroundColor(Color.WHITE)

        val mazeView = MazeView(requireContext())
        layout.addView(mazeView)

        return layout
    }

    // Inner class for the Maze Logic
    class MazeView(context: Context) : View(context) {
        private val paint = Paint()
        private var playerX = 1
        private var playerY = 1
        private val cellSize = 100f

        // 1 = Wall, 0 = Path, 2 = Goal
        private val mazeGrid = arrayOf(
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
            intArrayOf(1, 0, 0, 0, 1, 0, 0, 1),
            intArrayOf(1, 0, 1, 0, 1, 0, 1, 1),
            intArrayOf(1, 0, 1, 0, 0, 0, 0, 1),
            intArrayOf(1, 0, 1, 1, 1, 1, 0, 1),
            intArrayOf(1, 0, 0, 0, 0, 1, 0, 1),
            intArrayOf(1, 1, 1, 1, 0, 0, 2, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1)
        )

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            for (row in mazeGrid.indices) {
                for (col in mazeGrid[row].indices) {
                    val left = col * cellSize
                    val top = row * cellSize
                    val right = left + cellSize
                    val bottom = top + cellSize

                    when (mazeGrid[row][col]) {
                        1 -> { // Wall
                            paint.color = Color.BLACK
                            canvas.drawRect(left, top, right, bottom, paint)
                        }
                        2 -> { // Goal
                            paint.color = Color.GREEN
                            canvas.drawRect(left, top, right, bottom, paint)
                        }
                        // Path is transparent/background color
                    }
                }
            }

            // Draw Player
            paint.color = Color.RED
            val playerLeft = playerX * cellSize + 10
            val playerTop = playerY * cellSize + 10
            val playerRight = (playerX + 1) * cellSize - 10
            val playerBottom = (playerY + 1) * cellSize - 10
            canvas.drawOval(playerLeft, playerTop, playerRight, playerBottom, paint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val touchX = event.x
                val touchY = event.y

                val touchedCol = (touchX / cellSize).toInt()
                val touchedRow = (touchY / cellSize).toInt()

                // Bounds check to prevent crash if touching outside grid
                if (touchedRow !in mazeGrid.indices || touchedCol !in mazeGrid[0].indices) return true

                val isAdjacent = (Math.abs(touchedCol - playerX) + Math.abs(touchedRow - playerY)) == 1

                if (isAdjacent) {
                    if (mazeGrid[touchedRow][touchedCol] != 1) {
                        playerX = touchedCol
                        playerY = touchedRow
                        invalidate()
                        checkWinCondition()
                    }
                }
                return true
            }
            return super.onTouchEvent(event)
        }

        private fun checkWinCondition() {
            if (mazeGrid[playerY][playerX] == 2) {
                Toast.makeText(context, "You escaped the maze!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
