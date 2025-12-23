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
        color = Color.GREEN
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
        alpha = 100
    }

    private val foundPaths = mutableListOf<Path>()
    private val currentPath = Path()
    private var startCell: Pair<Int, Int>? = null
    private var currentCell: Pair<Int, Int>? = null

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
        foundPaths.clear()
        invalidate() // Redraw the view
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (numColumns > 0 && numRows > 0) {
            val size = w.coerceAtMost(h)
            cellSize = (size / numColumns).toFloat()
            textPaint.textSize = cellSize * 0.6f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Add this check to prevent drawing before the cell size is calculated
        if (letters.isEmpty() || cellSize == 0f) return


        // Draw letters
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

        // Draw lines for found words and the current selection
        foundPaths.forEach { canvas.drawPath(it, linePaint) }
        canvas.drawPath(currentPath, linePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (cellSize == 0f) return false

        val col = floor(event.x / cellSize).toInt().coerceIn(0, numColumns - 1)
        val row = floor(event.y / cellSize).toInt().coerceIn(0, numRows - 1)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startCell = Pair(row, col)
                currentPath.reset()
                val startX = col * cellSize + cellSize / 2
                val startY = row * cellSize + cellSize / 2
                currentPath.moveTo(startX, startY)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentCell = Pair(row, col)
                currentPath.reset()
                startCell?.let {
                    val startX = it.second * cellSize + cellSize / 2
                    val startY = it.first * cellSize + cellSize / 2
                    val currentX = col * cellSize + cellSize / 2
                    val currentY = row * cellSize + cellSize / 2
                    currentPath.moveTo(startX, startY)
                    currentPath.lineTo(currentX, currentY)
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
        // FIXED: Return true to ensure we keep receiving touch events (dragging)
        return true
    }

    private fun checkWord(start: Pair<Int, Int>?, end: Pair<Int, Int>?) {
        if (start == null || end == null) return

        val (startRow, startCol) = start
        val (endRow, endCol) = end

        // Only check for straight lines (horizontal, vertical, diagonal)
        val isStraightLine = startRow == endRow || startCol == endCol || (kotlin.math.abs(startRow - endRow) == kotlin.math.abs(startCol - endCol))
        if (!isStraightLine) return

        val selectedWord = getWordFromCells(start, end).uppercase()
        val reversedWord = selectedWord.reversed()

        if ((allWords.contains(selectedWord) && !foundWords.contains(selectedWord)) ||
            (allWords.contains(reversedWord) && !foundWords.contains(reversedWord))) {

            val wordToAdd = if (allWords.contains(selectedWord)) selectedWord else reversedWord
            foundWords.add(wordToAdd)

            // Make the path permanent
            val path = Path()
            path.moveTo(startCol * cellSize + cellSize / 2, startRow * cellSize + cellSize / 2)
            path.lineTo(endCol * cellSize + cellSize / 2, endRow * cellSize + cellSize / 2)
            foundPaths.add(path)

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
