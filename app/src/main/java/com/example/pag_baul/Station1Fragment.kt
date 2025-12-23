package com.example.pag_baul

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class Station1Fragment : Fragment() {

    // Views from your new layout
    private lateinit var diceImageView: ImageView
    private lateinit var rollButton: Button
    private lateinit var btnBackIcon: ImageView

    // List of dice images
    private val diceImages = listOf(
        R.drawable.dice1,
        R.drawable.dice2,
        R.drawable.dice3,
        R.drawable.dice4,
        R.drawable.dice5,
        R.drawable.dice6
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station1, container, false)

        // Initialize views using the correct IDs from your new layout
        diceImageView = view.findViewById(R.id.diceImageView)
        rollButton = view.findViewById(R.id.rollButton)
        btnBackIcon = view.findViewById(R.id.btnBackIcon)

        // Set the initial image
        diceImageView.setImageResource(diceImages[0])

        // Set click listeners
        btnBackIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        rollButton.setOnClickListener {
            rollDice()
        }

        return view
    }

    private fun rollDice() {
        // Disable the button to prevent multiple clicks during the roll
        rollButton.isEnabled = false

        val handler = Handler(Looper.getMainLooper())
        var rollCount = 0
        val maxRolls = 10 // How many times it "shuffles" the image
        val finalImageRes = diceImages.random() // Pick the final result now

        // Create a runnable to change the image rapidly
        val runnable = object : Runnable {
            override fun run() {
                if (rollCount < maxRolls) {
                    // Show a random dice face for the shuffle effect
                    diceImageView.setImageResource(diceImages.random())
                    rollCount++
                    // Repeat after a short delay
                    handler.postDelayed(this, 80)
                } else {
                    // After shuffling, show the final result
                    diceImageView.setImageResource(finalImageRes)
                    // Re-enable the button
                    rollButton.isEnabled = true
                }
            }
        }
        // Start the animation
        handler.post(runnable)
    }
}
