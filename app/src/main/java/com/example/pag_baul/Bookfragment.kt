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

        // 2. SETUP BACK BUTTON
        val btnBack = view.findViewById<Button>(R.id.btnBackToBooks)
        btnBack.setOnClickListener {
            (activity as MainActivity).loadFragment(HomeFragment())
        }

        // 3. Setup ALL 10 station buttons
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
            // BOOK 1 LOGIC
            // ============================================================
            if (currentBookId == 1) {
                when (stationNumber) {
                    1 -> openGenericFragment(Station1Fragment())
                    2 -> openGenericFragment(StoryFragment()) // Uses default XML text for Book 1

                    // BOOK 1 STATION 3 - NOW WITH QUESTIONS!
                    3 -> openQuiz(ArrayList<QuestionData>().apply {
                        add(QuestionData("Ano ang ginagawa ng magkapatid na sina Willy at Arlyn sa bahay?", "Naglalaro sila", "Nag-aaral sila", "Naglilinis sila at naglalaba", "Nag-aaway sila", "Naglilinis sila at naglalaba"))
                        add(QuestionData("Bakit hindi makapagtrabaho si Gng. Ferrer?", "Kasi may sakit siya", "Kasi walang trabaho", "Kasi ayaw niya", "Kasi may lakad siya", "Kasi may sakit siya"))
                        add(QuestionData("Ano ang inutos ni Gng. Ferrer sa kanyang mga anak?", "Pumunta sa sinehan", "Pumunta sa palengke", "Pumunta sa paaralan", "Pumunta sa parke", "Pumunta sa palengke"))
                        add(QuestionData("Ano ang pangako ni Willy sa kanyang ina?", "Ibibili niya ng itlog at prutas", "Ibibili niya ng damit", "Ibibili niya ng laruan", "Hindi niya gagawin ang anuman", "Ibibili niya ng itlog at prutas"))
                        add(QuestionData("Sino ang gumawa ng listahan ng mga bagay na kailangan sa araw na iyon?", "Willy", "Arlyn", "Gng. Ferrer", "Walang nabanggit", "Gng. Ferrer"))
                        add(QuestionData("Ano ang reaksyon ni Arlyn nang makita ang listahan?", "Masaya siya", "Malungkot siya", "Nag-alala siya", "Walang reaksyon", "Nag-alala siya"))
                        add(QuestionData("Ano ang sinabi ni Gng. Ferrer tungkol sa pera?", "May sapat sila", "Wala silang pera", "Hindi sapat ang pera para sa itlog at prutas", "Hindi niya sinabi", "Hindi sapat ang pera para sa itlog at prutas"))
                        add(QuestionData("Ano ang damdamin ni Gng. Ferrer sa kanyang mga anak?", "Proud", "Galit", "Malungkot", "Walang damdamin", "Proud"))
                        add(QuestionData("Ano ang pangarap ni Willy para sa kanyang ina?", "Makapag-aral siya", "Makapagtrabaho siya", "Mabigyan ng itlog at prutas", "Walang nabanggit", "Mabigyan ng itlog at prutas"))
                        add(QuestionData("Ano ang tema ng kwento?", "Pagmamahal at sakripisyo", "Pag-aaral at edukasyon", "Pagtutulungan at kooperasyon", "Walang tema", "Pagmamahal at sakripisyo"))
                    })

                    4 -> openGenericFragment(Station4Game1Fragment())
                    5 -> openGenericFragment(Station5Fragment()) // Uses default XML text for Book 1

                    // Essay Stations
                    6 -> openEssay("Station 6", "Kung ikaw si Willy, ano ang gagawin mo upang matulungan ang iyong ina?")
                    7 -> openEssay("Station 7", "Ano ang mga hamon na kinakaharap ng pamilya ni Gng. Ferrer at paano nila ito hinaharap?")
                    8 -> openEssay("Station 8", "Paano ipinakita ng kwento ang kahalagahan ng pagmamahal at pagtutulungan sa pamilya?")
                    9 -> openEssay("Station 9", "Kung ikaw ang may akda, ano ang gusto mong mangyari kina Willy at Arlyn sa katapusan ng kwento?")
                    10 -> openEssay("Station 10", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?")
                    else -> openGenericStation(stationNumber)
                }
            }

            // ============================================================
            // BOOK 2 LOGIC
            // ============================================================
            else if (currentBookId == 2) {
                when (stationNumber) {
                    1 -> openGenericFragment(Book2Station1Fragment())

                    2 -> openStory(
                        "Isang araw, nagtagpo sina tagak at kalabaw. Parehong uhaw na uhaw. \"Kumusta kaibigan,\" bati ni kalabaw. \"Heto malakas pa.\" Sagot ni tagak. \"Ano ang ginagawa mo rito?\" tanong ni kalabaw. \"Akin ang ilog na ito.\" Pagmamalaking sabi ni Tagak. \"Akin ito, ako ang may-ari nito,\" giit ni kalabaw.\n\n" +
                                "\"Pinagbabawalan mo ba ako,\" paniniyak ni Tagak. \"Ako ang hari ng ilog, lubluban ko ito maghapon.\" sabi ni kalabaw. \"Ako rin, akin ang mga isda.\" Binabantayan ko sila sa maghapon\", diin ni Tagak.\n\n" +
                                "\"Kung talagang hari ka inumin mo lahat ng tubig, kapag naubos mo yan saludo ako. Lahat ng utos mo'y susundin ko, kung matalo ka, akin ang tubig. Ako pa ang tatanghaling hari.\n" +
                                "\"Payag ako kaibigang tagak, kapag natalo ka, ika'y magiging alipin ko.",

                        "Sinimulan na ni kalabaw ang pag-inom ng malinamnam na tubig. Nagkataon noon na malaki ang tubig. Sa kakainom ni kalabaw siya'y lumobo na tila buntis. \"Suko na ako, ikaw naman, kaibigang tagak.\" Hindi alam ni kalabaw na nag-umpisa ng kumati ang tubig sa batis kaya't madaling komonti. Nang Makita ni kalabaw na nawawala ang tubig...\n\n" +
                                "Nanalo ka na kaibigang Tagak.\" Mula noon si Tagak ay nakasakay sa likod ni kalabaw. Siya ang hari at si kalabaw ang tusan niya."
                    )

                    3 -> openQuiz(ArrayList<QuestionData>().apply {
                        add(QuestionData("Sino ang nagtagpo isang araw na parehong uhaw na uhaw?", "Aso at pusa", "Tagak at kalabaw", "Unggoy at pagong", "", "Tagak at kalabaw"))
                        add(QuestionData("Ano ang sabi ni Tagak nang batiin siya ni Kalabaw?", "\"Heto, gutom na gutom.\"", "\"Heto, malakas pa.\"", "\"Heto, pagod na pagod.\"", "", "\"Heto, malakas pa.\""))
                        add(QuestionData("Ano ang inaangkin ni Kalabaw?", "Ang buong kagubatan", "Ang ilog", "Ang mga isda", "", "Ang ilog"))
                        add(QuestionData("Ano ang inaangkin naman ni Tagak?", "Ang mga puno sa paligid", "Ang mga ibon sa himpapawid", "Ang mga isda sa ilog", "", "Ang mga isda sa ilog"))
                        add(QuestionData("Ano ang hamon ni Tagak kay Kalabaw?", "Lumangoy sa pinakamalalim na bahagi ng ilog", "Inumin lahat ng tubig sa ilog", "Makipagbuno sa putikan", "", "Inumin lahat ng tubig sa ilog"))
                        add(QuestionData("Ano ang napala ni Kalabaw sa pag-inom ng maraming tubig?", "Sumakit ang tiyan", "Lumakas ang katawan", "Lumobo na tila buntis", "", "Lumobo na tila buntis"))
                        add(QuestionData("Bakit madaling naubos ang tubig sa batis?", "Dahil tag-init", "Dahil nag-umpisa nang kumati ang tubig", "Dahil may tumulong sa pag-inom kay Tagak", "", "Dahil nag-umpisa nang kumati ang tubig"))
                        add(QuestionData("Sino ang nanalo sa hamon?", "Kalabaw", "Tagak", "Walang nanalo", "", "Tagak"))
                        add(QuestionData("Ano ang naging papel ni Tagak pagkatapos manalo?", "Naging alipin ni Kalabaw", "Naging hari at nakasakay sa likod ni Kalabaw", "Umalis at naghanap ng ibang ilog", "", "Naging hari at nakasakay sa likod ni Kalabaw"))
                        add(QuestionData("Ano ang naging papel ni Kalabaw pagkatapos matalo?", "Naging hari", "Naging utusan ni Tagak", "Lumayas at nagtago sa gubat", "", "Naging utusan ni Tagak"))
                    })

                    4 -> openGenericFragment(Book2Station4Fragment())

                    5 -> openStation5WithQuestions(ArrayList<String>().apply {
                        add("1. Sa iyong palagay, ano ang naramdaman ni Tagak noong sinabi ni Kalabaw na siya ang hari ng ilog at may-ari nito?")
                        add("2. Paano kaya nagbago ang pakiramdam ni Kalabaw habang iniinom niya ang tubig at napansin niyang lumalaki ang kanyang tiyan?")
                        add("3. Anong emosyon ang nangingibabaw sa'yo sa huling bahagi ng kuwento kung saan si Tagak ay nakasakay na sa likod ni Kalabaw bilang hari?")
                    })

                    // Essays
                    6 -> openEssay("Station 6", "Sa iyong palagay, ano ang pinakamahalagang aral na makukuha sa kuwento ng Tagak at Kalabaw? Bakit ito mahalaga sa iyo?\n")
                    7 -> openEssay("Station 7", "Kung ikaw si Kalabaw, ano ang gagawin mong iba sa sitwasyon? Bakit?  \n")
                    8 -> openEssay("Station 8", "Sa anong mga sitwasyon sa iyong buhay mo naranasan ang pagiging tuso o pagkapanalo sa pamamagitan ng hindi inaasahang paraan?\n")
                    9 -> openEssay("Station 9", "Paano mo maiuugnay ang karakter ni Tagak o Kalabaw sa mga taong nakilala mo sa iyong buhay? Magbigay ng halimbawa. \n")
                    10 -> openEssay("Station 10", " Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?\n")
                    else -> openGenericStation(stationNumber)
                }
            }

            // ============================================================
            // BOOK 3 LOGIC
            // ============================================================
            else if (currentBookId == 3) {
                when (stationNumber) {
                    1 -> openGenericFragment(Book3Station1Fragment())

                    2 -> openStory(
                        "Si Jeff ay isang mabait na jeep. Magalang siya sa mga pasahero, bata man o matanda. Marami ang natutuwa sa kanya. \"Ligtas ang pakiramdam ko kapag kay Jeff ako nakasakay,\" anang isang nanay. Siyempre ang mga bata ay gusting gusto ring kay Jeff sumakay. \"Ingat kayo sa pag-akyat, mga bata!\" Sino ba naman ang hindi gustong sumakay sa isang mabait na jeep? Kaya naman lagging maraming pasahero si Jeff.Ito ang dahilan kaya asar sa kanya si Saro, ang jeep na barumbado. \"Umalis ka sa daraanan ko!\" sigaw niya kay Jeff. \"Ano ka ba naman Saro? Hindi iyo ang kalsada,\" sabi ni Jeff. \"Bakit, kakasa ka ba sa akin? Karera nalang tayo!\" ang hamon ni Saro. ",
                        "\"Alam mong hindi kita papatulan,\" ani Jeff.Responsableng sasakyan si Jeff. Ang lahat ng mga batas trapiko ay sinusunod niya. Hindi siya nakikipag-unahan kapag pula na ang ilaw. Tinitiyak din niyang nakatawid na ang lahat bago siya umandar. Bago mamasada ay naliligo pa siya at lagi siyang malinis. Lalong nainis at nainggit si Saro. Minsan ay nagyabang na mabuti si Saro. Uminom pa ito ng alak bago namasada. Napakabilis ng takbo nito. Iskriiits! Braaang! Ang malakas na pagsalpok ng dyip ni Saro sa puno. Marami ang tumulong para madala si Saro sa pagawaan ng sasakyan. Masakit ang lahat ng piyesa niya. Dinalaw siya ni Jeff. \"Magpagaling kang mabuti. Sana ay maging aral sa iyo iyan para hindi ka na muling mapahamak. Mula noon ay nagging responsable ng jeep si Saro.\n")

                    3 -> openQuiz(ArrayList<QuestionData>().apply {
                        add(QuestionData("Sino si Jeff sa kuwento?", "Isang drayber", "Isang bata", "Isang mabait na jeep", "Isang pulis", "Isang mabait na jeep"))
                        add(QuestionData("Ano ang ugali ni Jeff sa mga pasahero?", "Masungit", "Walang pakialam", "Mabait at magalang", "Palaging galit", "Mabait at magalang"))
                        add(QuestionData("Sino ang jeep na barumbado sa kuwento?\n?", "Jeff", "Saro", "Nanay", "Bata", "Saro"))
                        add(QuestionData("Bakit gustong-gusto ng mga bata na sumakay kay Jeff?\n", "Dahil mabilis siya", "Dahil libre ang sakay", " Dahil mabait siya", "Dahil bago ang jeep", "Dahil mabait siya"))
                        add(QuestionData("Ano ang palaging paalala ni Jeff sa mga bata?\n", "“Umupo kayo!”", "“Hawak nang mabuti!”", "“Ingat kayo sa pag-akyat!”", "“Bumaba na kayo!”", "“Ingat kayo sa pag-akyat!”"))
                        add(QuestionData("Ano ang ginagawa ni Jeff kapag pula na ang ilaw trapiko?\n", "Tumatakbo pa rin", "Humihinto at naghihintay", "Bumubusina", "Lumiliko agad", "Humihinto at naghihintay"))
                        add(QuestionData("Ano ang ginawa ni Saro bago magmaneho?\n", "Natulog", "Naglinis ng jeep", "Uminom ng alak", " Nagdasal", "Uminom ng alak"))
                        add(QuestionData("Ano ang nangyari kay Saro sa huli?\n", " Naging mas sikat", "Nabangga sa puno", "Nanalo sa karera", "Umalis sa bayan", "Nabangga sa puno"))
                        add(QuestionData("Ano ang ginawa ni Jeff matapos ang aksidente ni Saro?\n", "Iniwan siya", "Tinulungan siya", "Pinagalitan siya", "Pinagtawanan siya", "Tinulungan siya"))
                        add(QuestionData("Ano ang aral ng kuwento?\n", "Maging mabilis sa daan", "Maging mayabang", "Maging responsable at mabait", " Maging palaaway", "Maging responsable at mabait"))
                    })

                    4 -> openGenericFragment(StationFragment())

                    5 -> openStation5WithQuestions(ArrayList<String>().apply {
                        add("1. Ano ang naramdaman mo nang makita mong tinulungan ni Jeff si Saro pagkatapos ng aksidente?")
                        add("2. Paano nagbago ang damdamin ni Saro matapos siyang dalawin ni Jeff?")
                        add("3. Anong emosyon ang naramdaman mo para kay Saro noong siya ay nagmamaneho nang mabilis?")
                    })

                    6 -> openEssay("Station 6", "Ano ang ipinapakita ng kuwento tungkol sa isang mabuting drayber?")
                    7 -> openEssay("Station 7", "Bakit mahalagang sumunod sa mga batas-trapiko ayon sa kuwento?")
                    8 -> openEssay("Station 8", "Ano ang maaaring mangyari kung ang isang tao ay pabaya at pasaway sa kalsada?")
                    9 -> openEssay("Station 9", "Paano nakatutulong ang pagiging mabait at responsable sa kapwa?")
                    10 -> openEssay("Station 10", "Anong aral ang maaari nating ilapat sa ating sariling buhay mula sa kuwento?\n")
                    else -> openGenericStation(stationNumber)
                }
            }

            // ============================================================
            // BOOK 4 LOGIC
            // ============================================================
            else if (currentBookId == 4) {
                when (stationNumber) {
                    1 -> openGenericFragment(Book4Station1Fragment())

                    2 -> openStory(
                        "Masayang namumuhay ang lahat ng klase ng isda sa kaharian ng Tagadtala. Ang lahat ay masaya, nagtutulungan at nagmamahalan. Ang bawat isda ay buong pusong gumaganap ng kanyang tungkulin. Lahat sila ay may obligasyon na pangalagaan ang karagatan. Maayos na sana ang Tagadtala kung 'di dumating si Lucia mula sa lahi ng janitor fish. Mainggitin siya, selosa at tamad at gusto'y laging bida. \"Kailangan nating maghanda,\" wika ng tagapagbalita. \"Isang linggo mula ngayon ay bibisita ang Diyosa ng karagatan. Kailangang malinis at maayos ang lahat para matuwa siya sa atin.\" Ayon sa kanyang narinig, ang Diyosa ng karagatan ay mabait. Kapag mayroon daw itong nakakatuwang isda ay binibigyan niya ng kapangyarihan. Nag-isip si Lucia. \"kailangang matuwa sa akin ang diyosa.",
                        "Kailangang makaisip ako ng paraan upang mapansin ako.\" Nang sumunod na araw ang lahat ay abala, patingin-tingin lang si Lucia. \"Bakit ako magpapagod? Wais yata ako. Pag nariyan na ang diyosa saka ako kikilos. Hindi naman niya alam kung nagtrabaho ako o hindi.\" Araw ng pagbisita ng diyosa. Naka pormasyon ang lahat upang salubungin siya. Si Lucia lamang ang wala sa pila. Tiniyak ni Lucia na makikita siya ng diyosa habang kunwari ay abala sa paglilinis. Nakita nga siya ng diyosa. pinatawag siya. \"Huwag kang mag kunwaring masipag,\" galit na sabi ng diyosa. \"Mula ngayon ay isinusumpa kong maging pangit ang iyong anyo tulad ng yong ugali.\" Ang mga janitor fish ang pinakapangit na isda sa mundo.\n")

                    3 -> openQuiz(ArrayList<QuestionData>().apply {
                        add(QuestionData("Ano ang ginagawa ng mga isda sa kaharian ng Tagadatala?", "Nag-aaway araw-araw", "Naglalaro buong maghapon", "Nagtutulungan at ginagampanan ang kanilang tungkulin ", "Natutulog lamang", "Nagtutulungan at ginagampanan ang kanilang tungkulin"))
                        add(QuestionData("Bakit masaya ang pamumuhay ng mga isda sa simula ngkuwento?", "Dahil marami silang pagkain", "Dahil sila ay nagtutulungan at nagmamahalan ", "Dahil walangtrabaho", "Dahil takot sila sa Diyosa", "Dahil sila ay nagtutulungan at nagmamahalan "))
                        add(QuestionData("Bakit kailangang maglinis ang lahat ng isda bago dumatingang Diyosa?", "Dahil may paligsahan", "Dahil may darating na bisita", "Dahil gusto nilang umalis ", "Dahil utos ng mga janitor fish", "Dahil may darating na bisita"))
                        add(QuestionData("Sino ang Diyosa ng karagatan? ", "Isang karaniwang isda", "Tagapagbalita ng kaharian", "Tagapangalaga ng karagatan", "Kaibigan ni Lucia ", "Tagapangalaga ng karagatan"))
                        add(QuestionData("Ano ang iniisip ni Lucia tungkol sa paglilinis?", "Mahalaga ito", "Masaya itong gawin", "Nakakapagod at hindi mahalaga", "Nakakatulong sa lahat", "Nakakapagod at hindi mahalaga"))
                        add(QuestionData("Ano ang ginawa ng ibang isda bilang paghahanda sa pagbisitang Diyosa? ", "Nagtago sa bato", "Naglaro sa karagatan", "Naglinis at nag-ayos ng paligid", "Natulog buong araw", "Naglinis at nag-ayos ng paligid "))
                        add(QuestionData("Bakit nagalit ang Diyosa kay Lucia? ", "Dahil siya ay nawala ", "Dahil hindi siya tumulong at naging tamad", "Dahil siya ay nagkasakit", "Dahil siya ay umalis sa kaharian", "Dahil hindi siya tumulong at naging tamad"))
                        add(QuestionData("Ano ang parusang ibinigay ng Diyosa kay Lucia? ", "Pinalayas siya", "Ginawang pinakapangit na isda", "Inalis ang kanyang kapangyarihan", "Ikinulong siya", "Ginawang pinakapangit na isda"))
                        add(QuestionData("Ano ang ipinapakita ng ugali ni Lucia sa kuwento?", "Kasipagan", "Kabaitan", "Katamaran at pagiging makasarili", "Katapatan", "Katamaran at pagiging makasarili"))
                        add(QuestionData("Ano ang aral na matututunan sa kuwento? ", "Mas mabuting maging bida", "Hindi mahalaga ang tungkulin", "Dapat gampanan ang responsibilidad kahit walangnakakakita", "Mas masaya ang pagiging tamad", "Dapat gampanan ang responsibilidad kahit walangnakakakita "))
                    })

                    4 -> openGenericFragment(StationFragment())

                    5 -> openStation5WithQuestions(ArrayList<String>().apply {
                        add("1. Ano ang nararamdaman ni Lucia nang isinumpa siya ng Diyosa?")
                        add("2. Ano ang nararamdaman ng mga janitor fish nang malaman na sila ang magiging anyo ni Lucia?")
                        add("3. Ano ang nararamdaman ni Lucia nang marinig niya na bibisita ang Diyosa at maaaring bigyan ng kapangyarihan ang isda na nakatuwa sa kanya?")
                    })

                    6 -> openEssay("Station 6", "Ano ang natutunan mo mula sa ugali ni Lucia sa kuwento?")
                    7 -> openEssay("Station 7", "Bakit mahalaga ang gampanan ang ating tungkulin kahit walang nakatingin?")
                    8 -> openEssay("Station 8", "Kung ikaw si Lucia, paano mo gagawin ang paglilinis bago dumating ang Diyosa?")
                    9 -> openEssay("Station 9", "Paano mo maiwasan ang pagiging tamad o makasarili tulad ni Lucia?")
                    10 -> openEssay("Station 10", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?")
                    else -> openGenericStation(stationNumber)
                }
            }


            // ============================================================
            // BOOK 5 LOGIC
            // ============================================================
            else if (currentBookId == 5) {
                when (stationNumber) {
                    1 -> openGenericFragment(StationFragment())

                    2 -> openStory(
                        " Sa isang malaking kweba na may malalaking bato, may isang leon na sa tagal ng panahon ay mahina at matanda na kaya’t nakahiga na lamang ito. Isang araw, namataan niya ang isang kabayo na tumatawid na matagal na niyang gustong kainin. Dahil alam niyang mas mabilis sa kanyang tumakbo ito, umisip siya ng mabuting paraan para makalapit sa kanya. ",
                        "“Sa lahat ng hayop dito ay ako lamang ang nakaka-alam ng lunas sa anumang sakit”, wika ni leon. “Talaga?” manghang sagot ng kabayo. “Sige subukin mo ako,” muling sagot ng leon. Paika-ika siyang lumapit sa leon. Nagkunwaring masakit ang natinik na paa. Hiniling ng leon na ipakita sa kanya ang kanyang masakit na paa ng kabayo. Sa ganitong kilos ay napansin ng kabayo na siya ay buong lakas na sisilain ng leon. Bigla niyang itinaas ang mga paa at buong lakas na sinipa ang leon. Sabay takbo ng tumatawang kabayo. Nauwi sa wala ang maiitim na balak ng palalong leon.\n" +
                                "\n")

                    3 -> openQuiz(ArrayList<QuestionData>().apply {
                        add(QuestionData("Sino ang dalawang hayop sa kwento?", "Si Pagong at si Pusa", "Si Kabayo at si Leon", "Si Isda at si Tigre", "Si Kuneho at si Ibon", "Si Kabayo at si Leon"))
                        add(QuestionData("Ano ang binabalak ng Leon sa Kabayo?", "Sipain ", "Laruin ", "Kainin ", "Tawanan", "Kainin"))
                        add(QuestionData("Ano ang kalagayan ng leon sa simula ng kuwento? ", "Mahina at matanda na", "Malakas at bata pa", "Makisig ", "Mabait", "Mahina at matanda na"))
                        add(QuestionData("Anong paraan ang naisip ng leon para makalapit sa kabayo?", "Nagkunwaring may alam na lunas sa anumang sakit", "Binigyan niya ng maraming pagkain ang kabayo ", "Nakipaglaro ang leon sa kabayo", "Wala siyang ginawa", "Nagkunwaring may alam na lunas sa anumang sakit"))
                        add(QuestionData("Ano ang napansin ng kabayo habang papalapit siya sa leon?", "Napansin niya na mahina ang leon", "Napansin niya na tumatawa ang leon", "Napansin niya na may ubo ang leon", "Napansin niya na buong lakas na sisilain siya ng leon", "Napansin niya na buong lakas na sisilain siya ng leon"))
                        add(QuestionData("Ano ang ginawa ng kabayo upang mapigilan ang balak ng leon?", "Tinawanan niya ang leon", "Itinaas niya ang kanyang mga paa at buong lakas na sinipa ang leon", "Tumalon siya sa harapan ng leon", "Kinagat niya ang leon", "Itinaas niya ang kanyang mga paa at buong lakas na sinipa ang leon"))
                        add(QuestionData("Saan nagkita sina leon at kabayo?", "Sa dagat", "Sa bundok", " Sa ilog", "Sa kweba na may malalaking bato", "Sa kweba na may malalaking bato"))
                        add(QuestionData("Ano ang ginagawa ng leon habang nakita niya ang kabayo?", "Nakatayo", "Nakahiga ", "Nakaupo", "Nanonood", "Nakahiga "))
                        add(QuestionData("Ano ang ginagawa ng kabayo noong nakita siya ni leon?", "Natutulog ", "Kumakain", "Tumatawid ", "Tumatakbo", "Tumatawid "))
                        add(QuestionData("Ano ang tema ng kwento?", "Pagkagulat o pagharap sa sorpresa", "Pagkakakibigan ", "Pagtutulungan ", "Pagkakasundo", "Pagkagulat o pagharap sa sorpresa"))
                    })

                    4 -> openGenericFragment(StationFragment())

                    5 -> openStation5WithQuestions(ArrayList<String>().apply {
                        add("1. Ano ang nararamdaman ng leon sa pagkatalo?")
                        add("2. Ano ang nararamdaman sa pagligtas sa sarili?")
                        add("3. Ano ang nararamdamanng leon sa pagkawala ng dignidad? ")
                    })

                    6 -> openEssay("Station 6", "Ano ang naging pangunahing pagkakamali ng leon?")
                    7 -> openEssay("Station 7", "Bakit mahalaga ang bilis ng pag-iisip sa harap ng panganib?")
                    8 -> openEssay("Station 8", "Anong katangian ng leon ang hindi nagtagumpay sa kwento?")
                    9 -> openEssay("Station 9", "Paano ipinakita sa kwento ang konsepto ng “maitim na balak?")
                    10 -> openEssay("Station 10", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?\n")
                    else -> openGenericStation(stationNumber)
                }
            }

            // Fallback
            else {
                openGenericStation(stationNumber)
            }
        }
    }

    // ============================================================
    // HELPER FUNCTIONS (Do not edit these unless necessary)
    // ============================================================

    private fun openGenericFragment(fragment: Fragment) {
        (activity as MainActivity).loadFragment(fragment)
    }

    private fun openStory(page1: String, page2: String) {
        val fragment = StoryFragment()
        val bundle = Bundle()
        if (page1.isNotEmpty()) bundle.putString("STORY_PAGE1", page1)
        if (page2.isNotEmpty()) bundle.putString("STORY_PAGE2", page2)
        fragment.arguments = bundle
        (activity as MainActivity).loadFragment(fragment)
    }

    private fun openQuiz(questions: ArrayList<QuestionData>) {
        val fragment = QuizFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("QUESTIONS", questions)
        fragment.arguments = bundle
        (activity as MainActivity).loadFragment(fragment)
    }

    private fun openStation5WithQuestions(questions: ArrayList<String>) {
        val fragment = Station5Fragment()
        val bundle = Bundle()
        bundle.putStringArrayList("QUESTION_LIST", questions)
        fragment.arguments = bundle
        (activity as MainActivity).loadFragment(fragment)
    }

    private fun openEssay(title: String, question: String) {
        val fragment = EssayFragment()
        val bundle = Bundle()
        bundle.putString("ESSAY_TITLE", title)
        bundle.putString("ESSAY_QUESTION", question)
        fragment.arguments = bundle
        (activity as MainActivity).loadFragment(fragment)
    }

    private fun openGenericStation(stationNumber: Int) {
        val fragment = StationFragment()
        val bundle = Bundle()
        bundle.putInt("STATION_NUMBER", stationNumber)
        fragment.arguments = bundle
        (activity as MainActivity).loadFragment(fragment)
    }
}
