package com.example.pag_baul

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Station4Game2Fragment : Fragment() {

    private var selectedView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_station4_game2, container, false)

        // FIND VIEWS SAFELY
        // We use '?' (nullable) just in case an ID is wrong, so the app won't crash instantly.
        val drag1 = view.findViewById<View>(R.id.drag1)
        val drag2 = view.findViewById<View>(R.id.drag2)
        val drag3 = view.findViewById<View>(R.id.drag3)
        val drag4 = view.findViewById<View>(R.id.drag4)

        val target1 = view.findViewById<TextView>(R.id.target1)
        val target2 = view.findViewById<TextView>(R.id.target2)
        val target3 = view.findViewById<TextView>(R.id.target3)
        val target4 = view.findViewById<TextView>(R.id.target4)
        val btnDone = view.findViewById<Button>(R.id.btnDone)

        // 1. SETUP SELECTION LOGIC
        // Only run this if the drag items actually exist
        if (drag1 != null && drag2 != null && drag3 != null && drag4 != null) {
            val selectListener = View.OnClickListener { v ->
                selectedView = v
                Toast.makeText(context, "Item Selected! Now tap a Drop Zone.", Toast.LENGTH_SHORT).show()

                // Visual feedback (reset others, dim selected)
                drag1.alpha = 1.0f
                drag2.alpha = 1.0f
                drag3.alpha = 1.0f
                drag4.alpha = 1.0f
                v.alpha = 0.5f
            }

            drag1.setOnClickListener(selectListener)
            drag2.setOnClickListener(selectListener)
            drag3.setOnClickListener(selectListener)
            drag4.setOnClickListener(selectListener)
        } else {
            // Log error if IDs are wrong
            println("ERROR: Could not find one of the drag views (drag1-4)")
        }

        // 2. SETUP DROP LOGIC
        if (target1 != null && target2 != null && target3 != null && target4 != null) {
            val dropListener = View.OnClickListener { v ->
                if (selectedView != null) {
                    // "Drop" the item
                    (v as TextView).setBackgroundColor(Color.BLACK) // Change target color
                    v.text = "Item Placed" // Change text
                    selectedView!!.visibility = View.INVISIBLE // Hide the original black box
                    selectedView = null // Reset selection
                } else {
                    Toast.makeText(context, "Tap a black box first!", Toast.LENGTH_SHORT).show()
                }
            }

            target1.setOnClickListener(dropListener)
            target2.setOnClickListener(dropListener)
            target3.setOnClickListener(dropListener)
            target4.setOnClickListener(dropListener)
        }

        // 3. SETUP DONE BUTTON
        btnDone?.setOnClickListener {
            // Go back to the Book list
            (activity as MainActivity).loadFragment(BookFragment())
        }

        return view
    }
}
