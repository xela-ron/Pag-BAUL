package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import kotlin.math.abs

class StoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_story, container, false)

        // 1. Get Arguments
        // Note: arguments?.getString returns null if key is missing, so default to "" handles empty stories
        val storyText = arguments?.getString("STORY_PAGE1") ?: ""
        val bookId = arguments?.getInt("BOOK_ID") ?: 1
        val stationTitle = arguments?.getString("STATION_TITLE") ?: "Story"

        // 2. Setup UI Elements
        val tvStoryText = view.findViewById<TextView>(R.id.tvStoryText)
        val tvPageNumber = view.findViewById<TextView>(R.id.tvPageNumber)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val scrollViewStory = view.findViewById<ScrollView>(R.id.scrollViewStory)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        val ivBackground = view.findViewById<ImageView>(R.id.ivStoryBackground)

        // Find the Maze Container
        val mazeContainer = view.findViewById<FrameLayout>(R.id.mazeContainer)

        // Set Title if available
        if (tvTitle != null) tvTitle.text = stationTitle

        ivBackground.scaleType = ImageView.ScaleType.CENTER_CROP

        // 3. LOGIC HANDLING
        // Condition: Book 5 AND empty text means it's the Maze Station
        if (bookId == 5 && storyText.isEmpty()) {
            // ============================================================
            // BOOK 5 STATION 1: MAZE MODE
            // ============================================================

            // Hide standard story elements
            if (scrollViewStory != null) scrollViewStory.visibility = View.GONE
            if (btnNext != null) btnNext.visibility = View.GONE
            if (btnDone != null) btnDone.visibility = View.GONE

            // Hide the background image
            ivBackground.visibility = View.GONE

            // FORCE ROOT BACKGROUND TO WHITE to prevent black background issues
            view.setBackgroundColor(android.graphics.Color.WHITE)

            // Prepare the Compose View for the Maze
            val composeMazeView = ComposeView(requireContext()).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MazeGameScreen(
                        onWin = {
                            Toast.makeText(context, "Mahusay! Natapos mo ang maze!", Toast.LENGTH_LONG).show()
                            parentFragmentManager.popBackStack()
                        }
                    )
                }
            }

            // Define Layout Parameters to ensure the view FILLS the screen
            // Without this, the view might have 0 width/height = Black Screen
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            // Add the Maze View
            if (mazeContainer != null) {
                mazeContainer.visibility = View.VISIBLE
                mazeContainer.removeAllViews() // Clean up any previous views
                mazeContainer.addView(composeMazeView, layoutParams)
            } else {
                // Fallback: Add to root layout if container is missing
                val rootLayout = view as ViewGroup
                rootLayout.addView(composeMazeView, layoutParams)
            }

        } else {
            // ============================================================
            // STANDARD STORY MODE
            // ============================================================

            if (mazeContainer != null) {
                mazeContainer.visibility = View.GONE
            }

            // Set Background Cover
            when (bookId) {
                1 -> ivBackground.setImageResource(R.drawable.book1cover)
                2 -> ivBackground.setImageResource(R.drawable.book2cover)
                3 -> ivBackground.setImageResource(R.drawable.book3cover)
                4 -> ivBackground.setImageResource(R.drawable.book4cover)
                else -> ivBackground.setImageResource(R.drawable.book1cover)
            }

            ivBackground.visibility = View.VISIBLE
            if (scrollViewStory != null) scrollViewStory.visibility = View.VISIBLE

            if (tvPageNumber != null) tvPageNumber.visibility = View.GONE
            if (btnNext != null) btnNext.visibility = View.GONE
            if (btnDone != null) btnDone.visibility = View.VISIBLE

            if (tvStoryText != null) tvStoryText.text = storyText
        }

        // 4. Button Listeners
        btnDone?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}

// ============================================================
// COMPOSE MAZE LOGIC
// ============================================================

@Composable
fun MazeGameScreen(onWin: () -> Unit) {
    // 1=Wall, 0=Path, 2=Goal, 3=Start
    val mazeGrid = remember {
        listOf(
            listOf(1, 1, 1, 1, 1, 1, 1, 1),
            listOf(1, 3, 0, 0, 1, 0, 0, 1),
            listOf(1, 1, 1, 0, 1, 0, 1, 1),
            listOf(1, 0, 0, 0, 0, 0, 0, 1),
            listOf(1, 0, 1, 1, 1, 1, 0, 1),
            listOf(1, 0, 0, 0, 0, 1, 0, 1),
            listOf(1, 1, 1, 1, 0, 0, 2, 1),
            listOf(1, 1, 1, 1, 1, 1, 1, 1)
        )
    }

    var playerPos by remember { mutableStateOf(Pair(1, 1)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Important: Sets background of the game area to White
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val rows = mazeGrid.size
                        val cols = mazeGrid[0].size

                        // Avoid division by zero if size is not yet calculated
                        if (size.width > 0 && size.height > 0) {
                            val cellWidth = size.width / cols
                            val cellHeight = size.height / rows

                            val tappedCol = (offset.x / cellWidth).toInt()
                            val tappedRow = (offset.y / cellHeight).toInt()

                            if (tappedRow in 0 until rows && tappedCol in 0 until cols) {
                                val currentRow = playerPos.first
                                val currentCol = playerPos.second
                                val isAdjacent = (abs(tappedRow - currentRow) + abs(tappedCol - currentCol)) == 1

                                if (isAdjacent) {
                                    val cellType = mazeGrid[tappedRow][tappedCol]
                                    if (cellType != 1) { // Not a wall
                                        playerPos = Pair(tappedRow, tappedCol)
                                        if (cellType == 2) onWin()
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            val rows = mazeGrid.size
            val cols = mazeGrid[0].size

            // Safety check for size
            if (size.width > 0 && size.height > 0) {
                val cellWidth = size.width / cols
                val cellHeight = size.height / rows

                for (row in 0 until rows) {
                    for (col in 0 until cols) {
                        val color = when (mazeGrid[row][col]) {
                            1 -> Color(0xFF4E342E) // Wall (Brown)
                            2 -> Color(0xFFC19A6B) // Goal (Light Brown)
                            3 -> Color(0xFFFFD700) // Start (Gold)
                            else -> Color(0xFFFFF3E0) // Path (Cream)
                        }
                        drawRect(
                            color = color,
                            topLeft = Offset(col * cellWidth, row * cellHeight),
                            size = Size(cellWidth, cellHeight)
                        )
                    }
                }

                // Draw Player
                val playerRadius = (minOf(cellWidth, cellHeight) / 2) - 10f

                drawCircle(
                    color = Color.Red,
                    radius = playerRadius,
                    center = Offset(
                        x = (playerPos.second * cellWidth) + (cellWidth / 2),
                        y = (playerPos.first * cellHeight) + (cellHeight / 2)
                    )
                )
            }
        }
    }
}
