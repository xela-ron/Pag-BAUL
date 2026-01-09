package com.example.pag_baul

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.graphics.Color as AndroidColor // Use an alias for android.graphics.Color
import androidx.compose.ui.graphics.Color as ComposeColor // Optional: alias for compose color if needed elsewhere


data class FishChallenge(
    val fishName: String,
    val items: List<String>,
    val correctOrder: List<String>
)

class Book4Station1Fragment : Fragment() {

    private lateinit var fishSelectionContainer: GridLayout
    private lateinit var gameContainer: LinearLayout
    private lateinit var slotsContainer: LinearLayout
    private lateinit var tvInstruction: TextView
    private lateinit var btnSubmit: Button

    private var currentChallenge: FishChallenge? = null
    private var selectedView: TextView? = null
    private var mediaPlayer: MediaPlayer? = null

    // UPDATED: Corrected data for all challenges as per your instructions
    private val challenges = listOf(
        FishChallenge("GALIT NA ISDA",
            listOf("Umiwas", "Sumigaw", "Humingi ng paumanhin", "Nakinig"),
            listOf("Sumigaw", "Umiwas", "Nakinig", "Humingi ng paumanhin")
        ),
        FishChallenge("MADAMOT NA ISDA",
            listOf("Nagbahagi", "Inagaw ang pagkain", "Nag-alok sa kaibigan", "Tinago ang pagkain"),
            listOf("Inagaw ang pagkain", "Tinago ang pagkain", "Nagbahagi", "Nag-alok sa kaibigan")
        ),
        FishChallenge("ISDANG AYAW MAKISAMA",
            listOf("Kinausap nang maayos", "Nang-insulto", "Nakipagkaibigan", "Tinalikuran"),
            listOf("Nang-insulto", "Tinalikuran", "Kinausap nang maayos", "Nakipagkaibigan")
        ),
        FishChallenge("ISDANG HINDI SUMUSUNOD",
            listOf("Nagreklamo", "Tumulong sa iba", "Nilabag ang patakaran", "Sumunod"),
            listOf("Nilabag ang patakaran", "Nagreklamo", "Sumunod", "Tumulong sa iba")
        ),
        FishChallenge("ISDANG NAIINGGIT",
            listOf("Tinanggap ang pagkatalo", "Naiinggit", "Bumati sa nanalo", "Nagalit"),
            listOf("Naiinggit", "Nagalit", "Tinanggap ang pagkatalo", "Bumati sa nanalo")
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book4_station1, container, false)

        fishSelectionContainer = view.findViewById(R.id.fishSelectionContainer)
        gameContainer = view.findViewById(R.id.gameContainer)
        slotsContainer = view.findViewById(R.id.slotsContainer)
        tvInstruction = view.findViewById(R.id.tvInstruction)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        val btnBack = view.findViewById<View>(R.id.btnBack)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        // Set the new instruction text when the game screen is shown
        showFishSelection() // Initial setup

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        btnCancel.setOnClickListener { showFishSelection() }
        btnSubmit.setOnClickListener { checkAnswer() }

        setupFishButtons()

        return view
    }

    private fun setupFishButtons() {
        fishSelectionContainer.removeAllViews()
        fishSelectionContainer.columnCount = 1
        fishSelectionContainer.alignmentMode = GridLayout.ALIGN_BOUNDS

        challenges.forEach { challenge ->
            val button = Button(context)
            button.text = challenge.fishName

            val params = GridLayout.LayoutParams()
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.setMargins(12, 12, 12, 12)

            button.layoutParams = params
            button.background.setTint(AndroidColor.parseColor("#C19A6B")) // Use the alias
            button.setTextColor(AndroidColor.WHITE) // Use the alias
            button.textSize = 14f
            button.setSingleLine(false)
            button.gravity = Gravity.CENTER
            button.setPadding(16, 24, 16, 24)

            button.setOnClickListener { startChallenge(challenge) }
            fishSelectionContainer.addView(button)
        }
    }

    private fun startChallenge(challenge: FishChallenge) {
        currentChallenge = challenge
        selectedView = null

        fishSelectionContainer.visibility = View.GONE
        gameContainer.visibility = View.VISIBLE

        // Set the detailed instruction text for the game view
        tvInstruction.text = "Ayusin ang pagkakasunod-sunod: ${challenge.fishName}"

        slotsContainer.removeAllViews()
        val itemsToShow = ArrayList(challenge.items)
        itemsToShow.shuffle()

        itemsToShow.forEach { itemText ->
            val itemView = createGameItem(itemText)
            slotsContainer.addView(itemView)
        }
    }

    private fun createGameItem(text: String): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.textSize = 18f
        textView.setTextColor(AndroidColor.BLACK) // Use the alias
        textView.setPadding(32, 24, 32, 24)
        textView.setBackgroundResource(R.drawable.draggable_item_background)

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 8, 0, 8)
        textView.layoutParams = params

        textView.setOnClickListener { view -> handleItemClick(view as TextView) }

        return textView
    }

    private fun handleItemClick(clickedView: TextView) {
        if (selectedView == null) {
            selectedView = clickedView
            clickedView.setBackgroundColor(AndroidColor.LTGRAY) // Use the alias
            // Removed pop up message on select
        } else {
            val firstView = selectedView!!
            if (firstView == clickedView) {
                firstView.setBackgroundResource(R.drawable.draggable_item_background)
                selectedView = null
            } else {
                val tempText = firstView.text.toString()
                firstView.text = clickedView.text.toString()
                clickedView.text = tempText
                firstView.setBackgroundResource(R.drawable.draggable_item_background)
                selectedView = null
                // Removed pop up message on swap
            }
        }
    }

    private fun showFishSelection() {
        fishSelectionContainer.visibility = View.VISIBLE
        gameContainer.visibility = View.GONE
        // Set the main instruction text for the selection screen
        tvInstruction.text = "Panuto: Pumili ng isang isda sa screen, pagkatapos ay i-click at swap ang mga salita upang maayos ang kilos mula sa pinaka-masama hanggang sa pinaka-mabuti. Kapag tapos na, isumite ang iyong sagot upang malaman kung ito ay tama."
    }

    private fun checkAnswer() {
        val current = currentChallenge ?: return

        val userOrder = ArrayList<String>()
        for (i in 0 until slotsContainer.childCount) {
            val tv = slotsContainer.getChildAt(i) as TextView
            userOrder.add(tv.text.toString())
        }

        if (userOrder == current.correctOrder) {
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

        // Hide the button
        btnDialogNext.visibility = View.GONE

        // Corrected: Using androidx.appcompat.app.AlertDialog for Fragments
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.setOnDismissListener {
            releaseMediaPlayer()
        }

        if (isCorrect) {
            ivEmoji.setImageResource(R.drawable.happy)
            tvFeedback.text = "Magaling!"
            playSound(R.raw.clapping)

            dialog.show()

            // Auto-advance after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
                showFishSelection() // Go back to selection menu on success
            }, 2000)
        } else {
            ivEmoji.setImageResource(R.drawable.sad)
            tvFeedback.text = "Subukan muli!"
            playSound(R.raw.awww)

            dialog.show()

            // Auto-dismiss after 1.5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog.isShowing) dialog.dismiss()
            }, 2000)
        }
    }

    private fun playSound(soundId: Int) {
        releaseMediaPlayer() // Release any existing player
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer() // Ensure sound stops when the fragment is not visible
    }
}
