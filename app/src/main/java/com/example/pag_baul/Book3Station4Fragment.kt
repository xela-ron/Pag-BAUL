package com.example.pag_baul

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Book3Station4Fragment : Fragment() {

    private lateinit var lightRed: TextView
    private lateinit var lightYellow: TextView
    private lateinit var lightGreen: TextView
    private lateinit var btnStop: Button
    
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var currentState = 0 // 0 = Start/Red, 1 = Yellow, 2 = Green

    // The logic:
    // When Running:
    // - Red lights up, others off.
    // - After 1 sec, Yellow lights up, others off.
    // - After 1 sec, Green lights up, others off.
    // - After 1 sec, Back to Red.
    // Question text only appears on stop.

    private val trafficLightRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return

            resetLights()
            // When running, clear all text
            lightRed.text = ""
            lightYellow.text = ""
            lightGreen.text = ""

            when (currentState) {
                0 -> { // Red
                    lightRed.setBackgroundResource(R.drawable.circle_red)
                    currentState = 1
                    handler.postDelayed(this, 1000)
                }
                1 -> { // Yellow
                    lightYellow.setBackgroundResource(R.drawable.circle_yellow)
                    currentState = 2
                    handler.postDelayed(this, 1000)
                }
                2 -> { // Green
                    lightGreen.setBackgroundResource(R.drawable.circle_green)
                    currentState = 0 // Back to Red next
                    handler.postDelayed(this, 1000)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book3_station4, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        lightRed = view.findViewById(R.id.lightRed)
        lightYellow = view.findViewById(R.id.lightYellow)
        lightGreen = view.findViewById(R.id.lightGreen)
        btnStop = view.findViewById(R.id.btnStop)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        
        // Initial state
        lightRed.text = ""
        lightYellow.text = ""
        lightGreen.text = ""

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnStop.setOnClickListener {
            if (isRunning) {
                stopTrafficLight()
            } else {
                startTrafficLight()
            }
        }

        btnNext.setOnClickListener {
             (activity as MainActivity).loadFragment(Book3Station4Game2Fragment())
        }

        return view
    }
    
    private fun startTrafficLight() {
        isRunning = true
        btnStop.text = "Stop"
        // Reset state to Red to start sequence nicely
        currentState = 0 
        trafficLightRunnable.run()
    }

    private fun stopTrafficLight() {
        isRunning = false
        handler.removeCallbacks(trafficLightRunnable)
        btnStop.text = "Start"
        
        lightRed.text = ""
        lightYellow.text = ""
        lightGreen.text = ""

        if (currentState == 1) {
            // RED is lit
            lightRed.text = "Ano ang mangyayari kapag huminto si Jeff sa pulang ilaw trapiko?"
        } else if (currentState == 2) {
            // YELLOW is lit
            lightYellow.text = "Ano ang mangyayari kapag nakipagkarera si Saro sa ibang jeep?"
        } else if (currentState == 0) {
            // GREEN is lit
            lightGreen.text = "Ano ang mangyayari kapag pinapaalalahanan ni Jeff ang mga bata na mag-ingat sa pagsakay?"
        }
    }

    private fun resetLights() {
        lightRed.setBackgroundResource(R.drawable.circle_black)
        lightYellow.setBackgroundResource(R.drawable.circle_black)
        lightGreen.setBackgroundResource(R.drawable.circle_black)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isRunning = false
        handler.removeCallbacks(trafficLightRunnable)
    }
}
