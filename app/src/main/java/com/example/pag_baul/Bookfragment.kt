package com.example.pag_baul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class BookFragment : Fragment() {

    private var currentBookId = 1 // Default to Book 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book, container, false)

        // 1. Get the Book ID passed from HomeFragment
        currentBookId = arguments?.getInt("BOOK_ID") ?: 1

        // 2. Setup ALL 10 station buttons
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
        val button = view.findViewById<Button>(buttonId)

        button?.setOnClickListener {

            // ============================================================
            // LOGIC FOR BOOK 1 (KEPT EXACTLY AS IS)
            // ============================================================
            if (currentBookId == 1) {
                when (stationNumber) {
                    1 -> {
                        // Book 1, Station 1: Story Dice
                        val station1Fragment = Station1Fragment()
                        (activity as MainActivity).loadFragment(station1Fragment)
                    }
                    2 -> {
                        // Book 1, Station 2: Story (Willy & Arlyn)
                        val storyFragment = StoryFragment()
                        (activity as MainActivity).loadFragment(storyFragment)
                    }
                    3 -> {
                        // Book 1, Station 3: Quiz
                        val quizFragment = QuizFragment()
                        (activity as MainActivity).loadFragment(quizFragment)
                    }
                    4 -> {
                        // Book 1, Station 4: Game 1 (Flip Cards)
                        val game1Fragment = Station4Game1Fragment()
                        (activity as MainActivity).loadFragment(game1Fragment)
                    }
                    5 -> {
                        // Book 1, Station 5: Emotion Chart
                        val station5Fragment = Station5Fragment()
                        (activity as MainActivity).loadFragment(station5Fragment)
                    }
                    6 -> openEssay("Station 6.", "Kung ikaw si Willy, ano ang gagawin mo upang matulungan ang iyong ina?")
                    7 -> openEssay("Station 7.", "Ano ang mga hamon na kinakaharap ng pamilya ni Gng. Ferrer at paano nila ito hinaharap?")
                    8 -> openEssay("Station 8.", "Paano ipinakita ng kwento ang kahalagahan ng pagmamahal at pagtutulungan sa pamilya?")
                    9 -> openEssay("Station 9.", "Kung ikaw ang may akda, ano ang gusto mong mangyari kina Willy at Arlyn sa katapusan ng kwento?")
                    10 -> openEssay("Station 10.", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?")
                    else -> openStation(stationNumber)
                }
            }

            // ============================================================
            // LOGIC FOR BOOK 2, BOOK 3, etc. (ALL BLANK FOR NOW)
            // ============================================================
            else {
                // Currently, all stations for other books open the generic placeholder.
                // You will add specific logic here later.
                openStation(stationNumber)
            }
        }
    }

    private fun openEssay(title: String, question: String) {
        val essayFragment = EssayFragment()
        val bundle = Bundle()
        bundle.putString("ESSAY_TITLE", title)
        bundle.putString("ESSAY_QUESTION", question)
        essayFragment.arguments = bundle
        (activity as MainActivity).loadFragment(essayFragment)
    }

    private fun openStation(stationNumber: Int) {
        val fragment = StationFragment()
        val bundle = Bundle()
        bundle.putInt("STATION_NUMBER", stationNumber)
        fragment.arguments = bundle
        (activity as MainActivity).loadFragment(fragment)
    }
}
