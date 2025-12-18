package com.example.pag_baul

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // When any book card is clicked, go to stations screen
        findViewById<CardView>(R.id.book1_card).setOnClickListener {
            goToStationsScreen("BOOK 1")
        }
        findViewById<CardView>(R.id.book2_card).setOnClickListener {
            goToStationsScreen("BOOK 2")
        }
        findViewById<CardView>(R.id.book3_card).setOnClickListener {
            goToStationsScreen("BOOK 3")
        }
        findViewById<CardView>(R.id.book4_card).setOnClickListener {
            goToStationsScreen("BOOK 4")
        }
        findViewById<CardView>(R.id.book5_card).setOnClickListener {
            goToStationsScreen("BOOK 5")
        }
    }

    private fun goToStationsScreen(bookTitle: String) {
        val intent = Intent(this, StationsActivity::class.java)
        intent.putExtra("SELECTED_BOOK", bookTitle)
        startActivity(intent)
    }
}