package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button // Fixed the import typo here
import androidx.fragment.app.Fragment

class BookFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book, container, false)

        // Setup all 10 station buttons
        setupStationButton(view, R.id.btnStation1, 1)
        setupStationButton(view, R.id.btnStation2, 2)
        setupStationButton(view, R.id.btnStation3, 3)
        setupStationButton(view, R.id.btnStation4, 4)
        setupStationButton(view, R.id.btnStation5, 5)
        setupStationButton(view, R.id.btnStation6, 6)
        setupStationButton(view, R.id.btnStation7, 7)
        setupStationButton(view, R.id.btnStation8, 8)
        setupStationButton(view, R.id.btnStation9, 9)
        setupStationButton(view, R.id.btnStation10, 10)

        return view
    }

    private fun setupStationButton(view: View, buttonId: Int, stationNumber: Int) {
        // FIXED: Changed <androidx.compose.material3.Button> to just <Button>
        // because we are using XML layouts, not Jetpack Compose here.
        val button = view.findViewById<Button>(buttonId)

        button?.setOnClickListener {

            if (stationNumber == 2) {
                // Station 2 -> Open the Story Fragment
                val storyFragment = StoryFragment()
                (activity as MainActivity).loadFragment(storyFragment)

            } else if (stationNumber == 3) {
                // Station 3 -> Open the Quiz Fragment
                val quizFragment = QuizFragment()
                (activity as MainActivity).loadFragment(quizFragment)

            } else if (stationNumber == 4) {
                // Station 4 -> Open Game 1 (Flip Cards)
                val game1Fragment = Station4Game1Fragment()
                (activity as MainActivity).loadFragment(game1Fragment)

            } else if (stationNumber == 5) {
                // Station 5 -> Open Emotion Chart (NEW)
                val station5Fragment = Station5Fragment()
                (activity as MainActivity).loadFragment(station5Fragment)

            } else if (stationNumber == 6) {
                // Station 6 -> Essay
                openEssay("Station 6.", "Kung ikaw si Willy, ano ang gagawin mo upang matulungan ang iyong ina?")

            } else if (stationNumber == 7) {
                // Station 7 -> Essay
                openEssay("Station 7.", "Ano ang mga hamon na kinakaharap ng pamilya ni Gng. Ferrer at paano nila ito hinaharap?")

            } else if (stationNumber == 8) {
                // Station 8 -> Essay
                openEssay("Station 8.", "Paano ipinakita ng kwento ang kahalagahan ng pagmamahal at pagtutulungan sa pamilya?")

            } else if (stationNumber == 9) {
                // Station 9 -> Essay
                openEssay("Station 9.", "Kung ikaw ang may akda, ano ang gusto mong mangyari kina Willy at Arlyn sa katapusan ng kwento?")

            } else if (stationNumber == 10) {
                // Station 10 -> Essay
                openEssay("Station 10.", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?")

            } else {
                // All other stations (1) -> Open the normal Station Detail
                openStation(stationNumber)
            }
        }
    }

    // Helper to open the Essay Fragment with a specific question
    private fun openEssay(title: String, question: String) {
        val essayFragment = EssayFragment()
        val bundle = Bundle()
        bundle.putString("ESSAY_TITLE", title)
        bundle.putString("ESSAY_QUESTION", question)
        essayFragment.arguments = bundle
        (activity as MainActivity).loadFragment(essayFragment)
    }

    // Helper to open the generic Station Fragment
    private fun openStation(stationNumber: Int) {
        val fragment = StationFragment()
        val bundle = Bundle()
        bundle.putInt("STATION_NUMBER", stationNumber)
        fragment.arguments = bundle
        (activity as MainActivity).loadFragment(fragment)
    }
}
