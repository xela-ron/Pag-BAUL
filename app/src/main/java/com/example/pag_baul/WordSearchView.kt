package com.example.pag_baul

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.floor

// FIXED: Added @JvmOverloads constructor. This prevents the app from crashing on start.
class WordSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var numColumns = 0
    private var numRows = 0
    private var cellSize = 0f
    private val letters = mutableListOf<Char>()

    // 'foundWords' is public so Fragments can read it
    val foundWords = mutableSetOf<String>()
    private val allWords = mutableListOf<String>()

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.GREEN
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        alpha = 150
    }

    // Store found words as (StartCell, EndCell, Color) to handle resizing
    private val foundWordEntries = mutableListOf<Triple<Pair<Int, Int>, Pair<Int, Int>, Int>>()
    
    private val currentPath = Path()
    private var startCell: Pair<Int, Int>? = null
    private var currentCell: Pair<Int, Int>? = null

    private val highlightColors = listOf(
        Color.RED,
        Color.BLUE,
        Color.CYAN,
        Color.MAGENTA,
        Color.YELLOW,
        Color.parseColor("#FFA500"), // Orange
        Color.parseColor("#800080"), // Purple
        Color.parseColor("#006400")  // Dark Green
    )

    var onWordFound: ((String) -> Unit)? = null

    fun setGrid(grid: List<String>, words: List<String>) {
        if (grid.isEmpty()) return
        numRows = grid.size
        numColumns = grid[0].length
        letters.clear()
        grid.forEach { row ->
            row.forEach { char ->
                letters.add(char)
            }
        }
        allWords.clear()
        allWords.addAll(words.map { it.uppercase() })
        foundWords.clear()
        foundWordEntries.clear()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (numColumns > 0 && numRows > 0) {
            val size = w.coerceAtMost(h)
            cellSize = (size / numColumns).toFloat()
            textPaint.textSize = cellSize * 0.6f
            // Update stroke width relative to cell size
            linePaint.strokeWidth = cellSize * 0.7f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (letters.isEmpty() || cellSize == 0f) return

        // 1. Draw HIGHLIGHTS (Background) first
        // Reconstruct paths on draw to ensure they match current cellSize
        foundWordEntries.forEach { (start, end, color) ->
            val path = Path()
            val startX = start.second * cellSize + cellSize / 2
            val startY = start.first * cellSize + cellSize / 2
            val endX = end.second * cellSize + cellSize / 2
            val endY = end.first * cellSize + cellSize / 2
            path.moveTo(startX, startY)
            path.lineTo(endX, endY)
            
            linePaint.color = color
            linePaint.alpha = 100 // Semi-transparent
            canvas.drawPath(path, linePaint)
        }

        // Draw current dragging line
        if (!currentPath.isEmpty) {
            linePaint.color = Color.GREEN
            linePaint.alpha = 100
            canvas.drawPath(currentPath, linePaint)
        }

        // 2. Draw TEXT (Foreground) on top
        for (row in 0 until numRows) {
            for (col in 0 until numColumns) {
                val index = row * numColumns + col
                if (index < letters.size) {
                    val char = letters[index]
                    val x = col * cellSize + cellSize / 2
                    val y = row * cellSize + cellSize / 2 - (textPaint.descent() + textPaint.ascent()) / 2
                    canvas.drawText(char.toString(), x, y, textPaint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (cellSize == 0f) return false

        val col = floor(event.x / cellSize).toInt().coerceIn(0, numColumns - 1)
        val row = floor(event.y / cellSize).toInt().coerceIn(0, numRows - 1)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startCell = Pair(row, col)
                currentPath.reset()
                updateCurrentPath(startCell!!, startCell!!)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentCell = Pair(row, col)
                startCell?.let {
                    updateCurrentPath(it, currentCell!!)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val endCell = Pair(row, col)
                checkWord(startCell, endCell)
                currentPath.reset()
                invalidate()
            }
        }
        return true
    }

    private fun updateCurrentPath(start: Pair<Int, Int>, end: Pair<Int, Int>) {
        currentPath.reset()
        val startX = start.second * cellSize + cellSize / 2
        val startY = start.first * cellSize + cellSize / 2
        val currentX = end.second * cellSize + cellSize / 2
        val currentY = end.first * cellSize + cellSize / 2
        currentPath.moveTo(startX, startY)
        currentPath.lineTo(currentX, currentY)
    }

    private fun checkWord(start: Pair<Int, Int>?, end: Pair<Int, Int>?) {
        if (start == null || end == null) return

        val (startRow, startCol) = start
        val (endRow, endCol) = end

        // Only check for straight lines
        val isStraightLine = startRow == endRow || startCol == endCol || (kotlin.math.abs(startRow - endRow) == kotlin.math.abs(startCol - endCol))
        if (!isStraightLine) return

        val selectedWord = getWordFromCells(start, end).uppercase()
        val reversedWord = selectedWord.reversed()

        if ((allWords.contains(selectedWord) && !foundWords.contains(selectedWord)) ||
            (allWords.contains(reversedWord) && !foundWords.contains(reversedWord))) {

            val wordToAdd = if (allWords.contains(selectedWord)) selectedWord else reversedWord
            foundWords.add(wordToAdd)
            
            val color = highlightColors[foundWordEntries.size % highlightColors.size]
            // Store indices instead of path so it scales properly
            foundWordEntries.add(Triple(start, end, color))

            onWordFound?.invoke(wordToAdd)
        }
    }

    private fun getWordFromCells(start: Pair<Int, Int>, end: Pair<Int, Int>): String {
        val (startRow, startCol) = start
        val (endRow, endCol) = end
        val wordBuilder = StringBuilder()

        val dr = (endRow - startRow).coerceIn(-1, 1)
        val dc = (endCol - startCol).coerceIn(-1, 1)

        var r = startRow
        var c = startCol

        while (true) {
            if (r in 0 until numRows && c in 0 until numColumns) {
                val index = r * numColumns + c
                if (index < letters.size) {
                    wordBuilder.append(letters[index])
                }
            }
            if (r == endRow && c == endCol) break
            r += dr
            c += dc
        }
        return wordBuilder.toString()
    }
}
