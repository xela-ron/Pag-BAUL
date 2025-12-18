package com.example.pag_baul

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class StationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stations)

        // Get the selected book from previous screen
        val selectedBook = intent.getStringExtra("SELECTED_BOOK") ?: "ALAMAT1"

        // Update the title
        findViewById<TextView>(R.id.title_text).text = selectedBook

        // You can add click listeners for stations here
        // Example:
        // findViewById<Button>(R.id.station1).setOnClickListener {
        //     // Handle station 1 click
        // }
    }
}