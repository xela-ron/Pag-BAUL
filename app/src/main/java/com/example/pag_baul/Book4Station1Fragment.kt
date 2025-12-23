package com.example.pag_baul

import android.content.ClipData
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

data class FishChallenge(
    val fishName: String,
    val items: List<String>, // The scrambled words
    val correctOrder: List<String> // The correct sequence
)

class Book4Station1Fragment : Fragment() {

    private lateinit var fishSelectionContainer: GridLayout
    private lateinit var gameContainer: LinearLayout
    private lateinit var slotsContainer: LinearLayout
    private lateinit var tvInstruction: TextView
    private lateinit var btnSubmit: Button

    private var currentChallenge: FishChallenge? = null

    // Define the challenges
    private val challenges = listOf(
        FishChallenge("Galit na Isda",
            listOf("Umiwas", "Sumigaw", "Humingi ng paumanhin", "Nakinig"), // Scrambled
            listOf("Sumigaw", "Umiwas", "Nakinig", "Humingi ng paumanhin") // Correct: Bad -> Good
        ),
        FishChallenge("Madamot na Isda",
            listOf("Nagbahagi", "Inagaw ang pagkain", "Nag-alok sa kaibigan", "Tinago ang pagkain"),
            listOf("Inagaw ang pagkain", "Tinago ang pagkain", "Nagbahagi", "Nag-alok sa kaibigan")
        ),
        FishChallenge("Isdang Ayaw Makisama",
            listOf("Kinausap nang maayos", "Nang-insulto", "Nakipagkaibigan", "Tinalikuran"),
            listOf("Nang-insulto", "Tinalikuran", "Kinausap nang maayos", "Nakipagkaibigan")
        ),
        FishChallenge("Isdang Hindi Sumusunod",
            listOf("Nagreklamo", "Tumulong sa iba", "Nilabag ang patakaran", "Sumunod"),
            listOf("Nilabag ang patakaran", "Nagreklamo", "Sumunod", "Tumulong sa iba")
        ),
        FishChallenge("Isdang Naiinggit",
            listOf("Tinanggap ang pagkatalo", "Naiinggit", "Bumati sa nanalo", "Nagalit"),
            listOf("Naiinggit", "Nagalit", "Tinanggap ang pagkatalo", "Bumati sa nanalo")
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book4_station1, container, false)

        // Bind Views
        fishSelectionContainer = view.findViewById(R.id.fishSelectionContainer)
        gameContainer = view.findViewById(R.id.gameContainer)
        slotsContainer = view.findViewById(R.id.slotsContainer)
        tvInstruction = view.findViewById(R.id.tvInstruction)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        val btnBack = view.findViewById<View>(R.id.btnBack)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        btnCancel.setOnClickListener {
            showFishSelection()
        }

        btnSubmit.setOnClickListener {
            checkAnswer()
        }

        setupFishButtons()

        return view
    }

    private fun setupFishButtons() {
        fishSelectionContainer.removeAllViews()

        challenges.forEach { challenge ->
            val button = Button(context)
            button.text = challenge.fishName
            val params = GridLayout.LayoutParams()
            params.width = 300
            params.height = 180
            params.setMargins(16, 16, 16, 16)
            button.layoutParams = params
            button.setBackgroundColor(Color.parseColor("#0288D1"))
            button.setTextColor(Color.WHITE)

            button.setOnClickListener {
                startChallenge(challenge)
            }
            fishSelectionContainer.addView(button)
        }
    }

    private fun startChallenge(challenge: FishChallenge) {
        currentChallenge = challenge

        // UI Updates
        fishSelectionContainer.visibility = View.GONE
        gameContainer.visibility = View.VISIBLE
        tvInstruction.text = "Ayusin ang pagkakasunod-sunod: ${challenge.fishName}"

        // Populate items
        slotsContainer.removeAllViews()
        val itemsToShow = ArrayList(challenge.items)
        itemsToShow.shuffle()

        itemsToShow.forEach { itemText ->
            val itemView = createDraggableItem(itemText)
            slotsContainer.addView(itemView)
        }
    }

    private fun createDraggableItem(text: String): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.textSize = 18f
        textView.setTextColor(Color.BLACK)
        textView.setPadding(32, 24, 32, 24)
        textView.setBackgroundColor(Color.WHITE)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 8, 0, 8)
        textView.layoutParams = params

        // Enable Dragging
        textView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(view)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, shadowBuilder, view, 0)
                } else {
                    @Suppress("DEPRECATION")
                    view.startDrag(data, shadowBuilder, view, 0)
                }
                view.visibility = View.INVISIBLE // Hide original while dragging
                true
            } else {
                false
            }
        }

        // Enable Dropping logic (swapping)
        textView.setOnDragListener(dragListener)

        return textView
    }

    private val dragListener = View.OnDragListener { v, event ->
        val receiverView = v as? View
        val droppedView = event.localState as? View

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                v.setBackgroundColor(Color.LTGRAY) // Highlight target
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                v.setBackgroundColor(Color.WHITE) // Reset color
                true
            }
            DragEvent.ACTION_DROP -> {
                v.setBackgroundColor(Color.WHITE)

                if (droppedView != null && receiverView != null && droppedView != receiverView) {
                    val owner = droppedView.parent as LinearLayout
                    val fromIndex = owner.indexOfChild(droppedView)
                    val toIndex = owner.indexOfChild(receiverView)

                    owner.removeView(droppedView)
                    owner.removeView(receiverView)

                    if (fromIndex < toIndex) {
                        owner.addView(receiverView, fromIndex)
                        owner.addView(droppedView, toIndex)
                    } else {
                        owner.addView(droppedView, toIndex)
                        // FIXED TYPO HERE
                        owner.addView(receiverView, fromIndex)
                    }
                }
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                droppedView?.visibility = View.VISIBLE // Show original again
                v.setBackgroundColor(Color.WHITE)
                true
            }
            else -> false
        }
    }

    private fun showFishSelection() {
        fishSelectionContainer.visibility = View.VISIBLE
        gameContainer.visibility = View.GONE
        tvInstruction.text = "Panuto: Pumili ng isang isda sa ibaba."
    }

    private fun checkAnswer() {
        val current = currentChallenge ?: return

        val userOrder = ArrayList<String>()
        for (i in 0 until slotsContainer.childCount) {
            val tv = slotsContainer.getChildAt(i) as TextView
            userOrder.add(tv.text.toString())
        }

        if (userOrder == current.correctOrder) {
            Toast.makeText(context, "TAMA! Ang galing mo!", Toast.LENGTH_LONG).show()
            showFishSelection()
        } else {
            Toast.makeText(context, "Mali ang pagkakasunod-sunod. Subukan muli!", Toast.LENGTH_SHORT).show()
        }
    }
}
