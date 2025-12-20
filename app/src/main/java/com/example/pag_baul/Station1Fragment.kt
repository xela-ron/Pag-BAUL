package com.example.pag_baul

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class Station1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station1, container, false)

        val btnBackIcon = view.findViewById<ImageView>(R.id.btnBackIcon)
        val btnRoll = view.findViewById<CardView>(R.id.btnRoll)
        val ivDice = view.findViewById<ImageView>(R.id.ivDice)
        val ivStoryResult = view.findViewById<ImageView>(R.id.ivStoryResult)

        btnBackIcon.setOnClickListener {
            (activity as MainActivity).loadFragment(BookFragment())
        }

        btnRoll.setOnClickListener {
            // Hide previous result while rolling
            ivStoryResult.visibility = View.GONE
            rollDice(ivDice, ivStoryResult)
        }

        return view
    }

    private fun rollDice(diceImage: ImageView, storyImage: ImageView) {
        val handler = Handler(Looper.getMainLooper())
        var counter = 0
        var finalRandomInt = 1

        val runnable = object : Runnable {
            override fun run() {
                // Random 1-6
                finalRandomInt = Random.nextInt(6) + 1

                // Set dice face image
                val diceDrawable = when (finalRandomInt) {
                    1 -> R.drawable.dice1
                    2 -> R.drawable.dice2
                    3 -> R.drawable.dice3
                    4 -> R.drawable.dice4
                    5 -> R.drawable.dice5
                    else -> R.drawable.dice6
                }
                diceImage.setImageResource(diceDrawable)

                counter++
                if (counter < 10) {
                    // Keep rolling rapidly
                    handler.postDelayed(this, 100)
                } else {
                    // STOPPED! Show the story image now
                    showStoryResult(finalRandomInt, storyImage)
                }
            }
        }
        handler.post(runnable)
    }

    private fun showStoryResult(number: Int, storyImageView: ImageView) {
        // Here we select the specific story image
        // NOTE: Replace R.drawable.ic_launcher_foreground with R.drawable.dice1story later

        val storyDrawable = when (number) {
            1 -> R.drawable.ic_launcher_foreground // Change to R.drawable.dice1story later
            2 -> R.drawable.ic_launcher_foreground // Change to R.drawable.dice2story later
            3 -> R.drawable.ic_launcher_foreground // Change to R.drawable.dice3story later
            4 -> R.drawable.ic_launcher_foreground // Change to R.drawable.dice4story later
            5 -> R.drawable.ic_launcher_foreground // Change to R.drawable.dice5story later
            else -> R.drawable.ic_launcher_foreground // Change to R.drawable.dice6story later
        }

        storyImageView.setImageResource(storyDrawable)
        storyImageView.visibility = View.VISIBLE
    }
}
