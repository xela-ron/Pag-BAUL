package com.example.pag_baul

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!
    private var currentBookId: Int = 1

    companion object {
        const val BOOK_ID_KEY = "bookId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        currentBookId = arguments?.getInt(BOOK_ID_KEY) ?: 1

        val stationButtons = mapOf(
            1 to binding.btnStation1, 2 to binding.btnStation2, 3 to binding.btnStation3,
            4 to binding.btnStation4, 5 to binding.btnStation5, 6 to binding.btnStation6,
            7 to binding.btnStation7, 8 to binding.btnStation8, 9 to binding.btnStation9,
            10 to binding.btnStation10
        )

        stationButtons.forEach { (stationNumber, button) ->
            button.setOnClickListener { onStationClick(stationNumber) }
        }

        binding.btnBackToBooks.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun onStationClick(stationNumber: Int) {
        Log.d("BookFragment", "Book ID: $currentBookId, Clicked Station: $stationNumber")

        if (currentBookId == 1) {
            when (stationNumber) {
                1 -> openGenericFragment(Station1Fragment(), "Station 1. \"JUMBLED LETTERS\"")
                2 -> openStory(
                    "Sabado ng umaga, ang lahat ay abalang abala sa kanilang Gawain. Karaniwang tanawin din sa araw na ito ang mga batang masayang naglalaro. Ngunit kaiba sa bahay nila Gng. Ferrer. Madilim pa lamang ay gising na ang magkapatid na sina Willy at Arlyn. Nilinis ni Willy ang kanilang maliit na bahay, samantalang nilabhan naman ni Arlyn ang kanilang maruruming damit. Nang maluto ang kanilang almusal, hinatiran ng pagkain ng magkapatid ang kanilang ina. llang araw ng maysakit si Gng. Ferrer, kaya't hindi siya makapagtrabaho. Nang umagang iyon inutusan niya ang kanyang mga anak na pumunta sa palengke. Gumawa siya ng listahan ng mga bagay na kailangan nila sa araw na iyon at ito ay iniabot kay Arlyn. Sinipat ni Arlyn ng listahan. \"Sana maibili po naming kayo ng itlog at prutas Nay\", wika ni Arlyn. \"Hindi na sapat ang pera para sa itlog at prutas anak,\" sagot ni Gng. Ferrer. \" Hayaan po ninyo, Nanay, pag nakatapos po ako ng pag aaral, ibibili ko po kayo ng mga itlog at sariwang prutas at hindi na po kayo magtra trabaho para hindi na po kayo magkasakit,\" sabi ni Willy. \"Salamat mga anak napakapalad ko sa inyo masipag na ay mababait pa. Salat man tayo sa materyal na bagay ay mayaman naman tayo sa pagmamahal.\" Naluluhang wika ni Gng. Ferrer.\n",
                    "Station 2. \"Ang Yaman ng Magulang\"",
                    R.drawable.book1cover
                )
                3 -> openQuiz(ArrayList<QuestionData>().apply {
                    add(QuestionData("Ano ang ginagawa ng magkapatid na sina Willy at Arlyn sa bahay?", "Naglalaro sila", "Nag-aaral sila", "Naglilinis sila at naglalaba", "Nag-aaway sila", "Naglilinis sila at naglalaba"))
                    add(QuestionData("Bakit hindi makapagtrabaho si Gng. Ferrer?", "Dahil ayaw niya", "Dahil may sakit siya", "Dahil wala siyang trabaho", "Dahil may lakad siya", "Dahil may sakit siya"))
                    add(QuestionData("Ano ang inutos ni Gng. Ferrer sa kanyang mga anak?", "Pumunta sa paaralan", "Pumunta sa parke", "Pumunta sa sinehan", "Pumunta sa palengke", "Pumunta sa palengke"))
                    add(QuestionData("Sino ang gumawa ng listahan ng mga bagay na kailangan sa araw na iyon?", "Willy", "Gng. Ferrer", "Arlyn", "Walang nabanggit", "Gng. Ferrer"))
                    add(QuestionData("Ayon sa listahan, para saan hindi sapat ang kanilang pera?", "Para sa bigas at ulam", "Para sa gamot", "Para sa itlog at prutas", "Para sa pamasahe", "Para sa itlog at prutas"))
                    add(QuestionData("Ano ang pangako ni Willy sa kanyang ina?", "Hindi na siya mag-aaral", "Ibibili niya ng maraming laruan ang ina", "Ibibili niya ang ina ng itlog at prutas pag nakapagtapos siya", "Aalis siya ng bahay", "Ibibili niya ang ina ng itlog at prutas pag nakapagtapos siya"))
                    add(QuestionData("Ano ang naramdaman ni Gng. Ferrer para sa kanyang mga anak?", "Nalungkot", "Nagalit", "Nainis", "Naluha dahil sa tuwa at pagiging mapalad", "Naluha dahil sa tuwa at pagiging mapalad"))
                    add(QuestionData("Paano inilarawan ni Gng. Ferrer ang kanilang pamilya?", "Mayaman sa materyal na bagay", "Salat sa pagmamahal", "Salat sa materyal na bagay ngunit mayaman sa pagmamahal", "Walang anumang yaman", "Salat sa materyal na bagay ngunit mayaman sa pagmamahal"))
                    add(QuestionData("Sino ang magkapatid sa kuwento?", "Ana at Eba", "Juan at Pedro", "Willy at Arlyn", "Jose at Maria", "Willy at Arlyn"))
                    add(QuestionData("Ano ang pangunahing tema ng kuwento?", "Kasakiman at pag-iimbot", "Pagmamahal ng pamilya at sakripisyo", "Pakikipagsapalaran sa palengke", "Kahalagahan ng pera", "Pagmamahal ng pamilya at sakripisyo"))
                }, "Station 3. Q&A")
                4 -> openGenericFragment(Station4Game1Fragment(), "Station 4. \"FLIP CARDS\"")
                5 -> openStation5WithQuestions(ArrayList<String>().apply {
                    add("1. Ano ang nararamdaman  ni Gng. Ferrer nang makitang nagkukusang naglilinis ang kaniyang mga anak?")
                    add("2. Ano ang nararamdaman  nila Arlyn at Willy habang sinasabi ng kanilang ina ang katagang “ Hindi sapat ang pera para sa itlog at prutas anak")
                    add("3. Ano ang nararamdaman  ni Gng. Ferrer habang binibigkas ng kaniyang anak ang katagang “Hayaan po ninyo, Nanay, pag nakatapos po ako ng pag aaral, ibibili ko po kayo ng mga itlog at sariwang prutas at hindi na po kayo magtra trabaho para hindi na po kayo magkasakit”")
                }, "Station 5. \"EMOTION CHART\"")
                6 -> openEssay("Station 6. \"ESSAY\"", "Kung ikaw si Willy, ano ang gagawin mo upang matulungan ang iyong ina?")
                7 -> openEssay("Station 7. \"ESSAY\"", "Ano ang mga hamon na kinakaharap ng pamilya ni Gng. Ferrer at paano nila ito hinaharap?")
                8 -> openEssay("Station 8. \"ESSAY\"", "Paano ipinakita ng kwento ang kahalagahan ng pagmamahal at pagtutulungan sa pamilya? ")
                9 -> openEssay("Station 9. \"ESSAY\"", "Kung ikaw ang may akda, ano ang gusto mong mangyari kina Willy at Arlyn sa katapusan ng kwento? ")
                10 -> openEssay("Station 10. \"ESSAY\"", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?")
                else -> openGenericStation(stationNumber)
            }
        } else if (currentBookId == 2) {
            when (stationNumber) {
                1 -> openGenericFragment(Book2Station1Fragment(), "Station 1. \"4Pics 1 Word\"")
                2 -> openStory(
                    "Isang araw, nagtagpo sina tagak at kalabaw. Parehong uhaw na uhaw. \"Kumusta kaibigan,\" bati ni kalabaw. \"Heto malakas pa.\" Sagot ni tagak. \"Ano ang ginagawa mo rito?\" tanong ni kalabaw. \"Akin ang ilog na ito.\" Pagmamalaking sabi ni Tagak. \"Akin ito, ako ang may-ari nito,\" giit ni kalabaw.\n\n" +
                            "\"Pinagbabawalan mo ba ako,\" paniniyak ni Tagak. \"Ako ang hari ng ilog, lubluban ko ito maghapon.\" sabi ni kalabaw. \"Ako rin, akin ang mga isda.\" Binabantayan ko sila sa maghapon\", diin ni Tagak.\n\n" +
                            "\"Kung talagang hari ka inumin mo lahat ng tubig, kapag naubos mo yan saludo ako. Lahat ng utos mo'y susundin ko, kung matalo ka, akin ang tubig. Ako pa ang tatanghaling hari.\n" +
                            "\"Payag ako kaibigang tagak, kapag natalo ka, ika'y magiging alipin ko.\n\n" +
                            "Sinimulan na ni kalabaw ang pag-inom ng malinamnam na tubig. Nagkataon noon na malaki ang tubig. Sa kakainom ni kalabaw siya'y lumobo na tila buntis. \"Suko na ako, ikaw naman, kaibigang tagak.\" Hindi alam ni kalabaw na nag-umpisa ng kumati ang tubig sa batis kaya't madaling komonti. Nang Makita ni kalabaw na nawawala ang tubig...\n\n" +
                            "Nanalo ka na kaibigang Tagak.\" Mula noon si Tagak ay nakasakay sa likod ni kalabaw. Siya ang hari at si kalabaw ang tusan niya.",
                    "Station 2. \"SI TAGAK AT SI KALABAW\"",
                    R.drawable.book2cover
                )
                3 -> openQuiz(ArrayList<QuestionData>().apply {
                    add(QuestionData("Sino ang nagtagpo isang araw na parehong uhaw na uhaw?", "Aso at pusa", "Tagak at kalabaw", "Unggoy at pagong", "Elepante at daga", "Tagak at kalabaw"))
                    add(QuestionData("Ano ang sabi ni Tagak nang batiin siya ni Kalabaw?", "\"Heto, gutom na gutom.\"", "\"Heto, malakas pa.\"", "\"Heto, pagod na pagod.\"", "Heto, inaantok.", "Heto, malakas pa."))
                    add(QuestionData("Ano ang inaangkin ni Kalabaw?", "Ang buong kagubatan", "Ang ilog", "Ang mga isda", "Ang langit", "Ang ilog"))
                    add(QuestionData("Ano ang inaangkin naman ni Tagak?", "Ang mga puno sa paligid", "Ang mga ibon sa himpapawid", "Ang mga isda sa ilog", "Ang mga bulaklak", "Ang mga isda sa ilog"))
                    add(QuestionData("Ano ang hamon ni Tagak kay Kalabaw?", "Lumangoy sa pinakamalalim na bahagi ng ilog", "Inumin lahat ng tubig sa ilog", "Makipagbuno sa putikan", "Tumakbo ng mabilis", "Inumin lahat ng tubig sa ilog"))
                    add(QuestionData("Ano ang napala ni Kalabaw sa pag-inom ng maraming tubig?", "Sumakit ang tiyan", "Lumakas ang katawan", "Lumobo na tila buntis", "Naging masaya siya", "Lumobo na tila buntis"))
                    add(QuestionData("Bakit madaling naubos ang tubig sa batis?", "Dahil tag-init", "Dahil nag-umpisa nang kumati ang tubig", "Dahil may tumulong sa pag-inom kay Tagak", "Dahil may butas ang ilog", "Dahil nag-umpisa nang kumati ang tubig"))
                    add(QuestionData("Sino ang nanalo sa hamon?", "Kalabaw", "Tagak", "Walang nanalo", "Parehas silang nanalo", "Tagak"))
                    add(QuestionData("Ano ang naging papel ni Tagak pagkatapos manalo?", "Naging alipin ni Kalabaw", "Naging hari at nakasakay sa likod ni Kalabaw", "Umalis at naghanap ng ibang ilog", "Naging kaibigan sila", "Naging hari at nakasakay sa likod ni Kalabaw"))
                    add(QuestionData("Ano ang naging papel ni Kalabaw pagkatapos matalo?", "Naging hari", "Naging utusan ni Tagak", "Lumayas at nagtago sa gubat", "Naging masaya", "Naging utusan ni Tagak"))
                }, "Station 3. Q&A")
                4 -> openGenericFragment(Book2Station4Fragment(), "Station 4. \"4Pics 1 Word\"")
                5 -> openStation5WithQuestions(ArrayList<String>().apply {
                    add("1. Sa iyong palagay, ano ang naramdaman ni Tagak noong sinabi ni Kalabaw na siya ang hari ng ilog at may-ari nito?")
                    add("2. Paano kaya nagbago ang pakiramdam ni Kalabaw habang iniinom niya ang tubig at napansin niyang lumalaki ang kanyang tiyan?")
                    add("3. Anong emosyon ang nangingibabaw sa'yo sa huling bahagi ng kuwento kung saan si Tagak ay nakasakay na sa likod ni Kalabaw bilang hari?")
                }, "Station 5. \"Emotion Chart\"")
                6 -> openEssay("Station 6. \"ESSAY\"", "Sa iyong palagay, ano ang pinakamahalagang aral na makukuha sa kuwento ng Tagak at Kalabaw? Bakit ito mahalaga sa iyo?\n")
                7 -> openEssay("Station 7. \"ESSAY\"", "Kung ikaw si Kalabaw, ano ang gagawin mong iba sa sitwasyon? Bakit?  \n")
                8 -> openEssay("Station 8. \"ESSAY\"", "Sa anong mga sitwasyon sa iyong buhay mo naranasan ang pagiging tuso o pagkapanalo sa pamamagitan ng hindi inaasahang paraan?\n")
                9 -> openEssay("Station 9. \"ESSAY\"", "Paano mo maiuugnay ang karakter ni Tagak o Kalabaw sa mga taong nakilala mo sa iyong buhay? Magbigay ng halimbawa. \n")
                10 -> openEssay("Station 10. \"ESSAY\"", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?\n")
                else -> openGenericStation(stationNumber)
            }
        } else if (currentBookId == 3) {
            when (stationNumber) {
                1 -> openGenericFragment(Book3Station1Fragment(), "Station 1. \"Word Search\"")
                2 -> openStory(
                    "Si Jeff ay isang mabait na jeep. Magalang siya sa mga pasahero, bata man o matanda. Marami ang natutuwa sa kanya. \"Ligtas ang pakiramdam ko kapag kay Jeff ako nakasakay,\" anang isang nanay. Siyempre ang mga bata ay gusting gusto ring kay Jeff sumakay. \"Ingat kayo sa pag-akyat, mga bata!\" Sino ba naman ang hindi gustong sumakay sa isang mabait na jeep? Kaya naman lagging maraming pasahero si Jeff. Ito ang dahilan kaya asar sa kanya si Saro, ang jeep na barumbado. \"Umalis ka sa daraanan ko!\" sigaw niya kay Jeff. \"Ano ka ba naman Saro? Hindi iyo ang kalsada,\" sabi ni Jeff. \"Bakit, kakasa ka ba sa akin? Karera nalang tayo!\" ang hamon ni Saro. \n\n" +
                            "\"Alam mong hindi kita papatulan,\" ani Jeff.Responsableng sasakyan si Jeff. Ang lahat ng mga batas trapiko ay sinusunod niya. Hindi siya nakikipag-unahan kapag pula na ang ilaw. Tinitiyak din niyang nakatawid na ang lahat bago siya umandar. Bago mamasada ay naliligo pa siya at lagi siyang malinis. Lalong nainis at nainggit si Saro. Minsan ay nagyabang na mabuti si Saro. Uminom pa ito ng alak bago namasada. Napakabilis ng takbo nito. Iskriiits! Braaang! Ang malakas na pagsalpok ng dyip ni Saro sa puno. Marami ang tumulong para madala si Saro sa pagawaan ng sasakyan. Masakit ang lahat ng piyesa niya. Dinalaw siya ni Jeff. \"Magpagaling kang mabuti. Sana ay maging aral sa iyo iyan para hindi ka na muling mapahamak. Mula noon ay nagging responsable ng jeep si Saro.",
                    "Station 2. \"ANG MABAIT NA JEEP\"",
                    R.drawable.book3cover
                )
                3 -> openQuiz(ArrayList<QuestionData>().apply {
                    add(QuestionData("Sino si Jeff sa kuwento?", "Isang drayber", "Isang bata", "Isang mabait na jeep", "Isang pulis", "Isang mabait na jeep"))
                    add(QuestionData("Ano ang ugali ni Jeff sa mga pasahero?", "Masungit", "Walang pakialam", "Mabait at magalang", "Palaging galit", "Mabait at magalang"))
                    add(QuestionData("Sino ang jeep na barumbado sa kuwento?\n?", "Jeff", "Saro", "Nanay", "Bata", "Saro"))
                    add(QuestionData("Bakit gustong-gusto ng mga bata na sumakay kay Jeff?\n", "Dahil mabilis siya", "Dahil libre ang sakay", " Dahil mabait siya", "Dahil bago ang jeep", "Dahil mabait siya"))
                    add(QuestionData("Ano ang palaging paalala ni Jeff sa mga bata?\n", "“Uupo kayo!”", "“Hawak nang mabuti!”", "“Ingat kayo sa pag-akyat!”", "“Bumaba na kayo!”", "“Ingat kayo sa pag-akyat!”"))
                    add(QuestionData("Ano ang ginagawa ni Jeff kapag pula na ang ilaw trapiko?\n", "Tumatakbo pa rin", "Humihinto at naghihintay", "Bumubusina", "Lumiliko agad", "Humihinto at naghihintay"))
                    add(QuestionData("Ano ang ginawa ni Saro bago magmaneho?\n", "Natulog", "Naglinis ng jeep", "Uminom ng alak", " Nagdasal", "Uminom ng alak"))
                    add(QuestionData("Ano ang nangyari kay Saro sa huli?\n", " Naging mas sikat", "Nabangga sa puno", "Nanalo sa karera", "Umalis sa bayan", "Nabangga sa puno"))
                    add(QuestionData("Ano ang ginawa ni Jeff matapos ang aksidente ni Saro?\n", "Iniwan siya", "Tinulungan siya", "Pinagalitan siya", "Pinagtawanan siya", "Tinulungan siya"))
                    add(QuestionData("Ano ang aral ng kuwento?\n", "Maging mabilis sa daan", "Maging mayabang", "Maging responsable at mabait", " Maging palaaway", "Maging responsable at mabait"))
                }, "Station 3. Q&A")
                4 -> openGenericFragment(Book3Station4Fragment(), "Station 4. \"GAMES\"")
                5 -> openGenericFragment(Book3Station5GameFragment(), "Station 5. Responsable o Hindi?")
                6 -> openEssay("Station 6. \"ESSAY\"", "Ano ang ipinapakita ng kuwento tungkol sa isang mabuting drayber?")
                7 -> openEssay("Station 7. \"ESSAY\"", "Bakit mahalagang sumunod sa mga batas-trapiko ayon sa kuwento?")
                8 -> openEssay("Station 8. \"ESSAY\"", "Ano ang maaaring mangyari kung ang isang tao ay pabaya at pasaway sa kalsada?")
                9 -> openEssay("Station 9. \"ESSAY\"", "Paano nakatutulong ang pagiging mabait at responsable sa kapwa?")
                10 -> openEssay("Station 10. \"ESSAY\"", "Anong aral ang maaari nating ilapat sa ating sariling buhay mula sa kuwento?\n")
                else -> openGenericStation(stationNumber)
            }
        } else if (currentBookId == 4) {
            when (stationNumber) {
                1 -> openGenericFragment(Book4Station1Fragment(), "Station 1. \"PUMILI NG ISDA\"")
                2 -> openStory(
                    "Masayang namumuhay ang lahat ng klase ng isda sa kaharian ng Tagadtala. Ang lahat ay masaya, nagtutulungan at nagmamahalan. Ang bawat isda ay buong pusong gumaganap ng kanyang tungkulin. Lahat sila ay may obligasyon na pangalagaan ang karagatan. Maayos na sana ang Tagadtala kung 'di dumating si Lucia mula sa lahi ng janitor fish. Mainggitin siya, selosa at tamad at gusto'y laging bida. \"Kailangan nating maghanda,\" wika ng tagapagbalita. \"Isang linggo mula ngayon ay bibisita ang Diyosa ng karagatan. Kailangang malinis at maayos ang lahat para matuwa siya sa atin.\" Ayon sa kanyang narinig, ang Diyosa ng karagatan ay mabait. Kapag mayroon daw itong nakakatuwang isda ay binibigyan niya ng kapangyarihan. Nag-isip si Lucia. \"kailangang matuwa sa akin ang diyosa. Kailangang makaisip ako ng paraan upang mapansin ako.\" Nang sumunod na araw ang lahat ay abala, patingin-tingin lang si Lucia. \"Bakit ako magpapagod? Wais yata ako. Pag nariyan na ang diyosa saka ako kikilos. Hindi naman niya alam kung nagtrabaho ako o hindi.\" Araw ng pagbisita ng diyosa. Naka pormasyon ang lahat upang salubungin siya. Si Lucia lamang ang wala sa pila. Tiniyak ni Lucia na makikita siya ng diyosa habang kunwari ay abala sa paglilinis. Nakita nga siya ng diyosa. pinatawag siya. \"Huwag kang mag kunwaring masipag,\" galit na sabi ng diyosa. \"Mula ngayon ay isinusumpa kong maging pangit ang iyong anyo tulad ng yong ugali.\" Ang mga janitor fish ang pinakapangit na isda sa mundo.\n",
                    "Station 2. \"ANG ISINUMPANG ISDA\"",
                    R.drawable.book4cover
                )
                3 -> openQuiz(ArrayList<QuestionData>().apply {
                    add(QuestionData("Ano ang ginagawa ng mga isda sa kaharian ng Tagadatala?\n", "Nag-aaway araw-araw", "Naglalaro buong maghapon", "Nagtutulungan at ginagampanan", "Nagtutulungan at ginagampanan ang kanilang tungkulin ", "Nagtutulungan at ginagampanan ang kanilang tungkulin "))
                    add(QuestionData(" Bakit masaya ang pamumuhay ng mga isda sa simula ng kuwento?\n", "Dahil marami silang pagkain ", "Dahil sila ay nagtutulungan at nagmamahalan ", "Dahil walangtrabaho", "Dahil takot sila sa Diyosa", "Dahil sila ay nagtutulungan at nagmamahalan "))
                    add(QuestionData("Bakit kailangang malinis ang lahat ng isda bago dumating ang Diyosa?  \n", "Dahil may paligsahan", "Dahil may darating na bisita", "Dahil gusto nilang umalis", "Dahil utos ng mga janitor fish ", " Dahil may darating na bisita"))
                    add(QuestionData("Sino ang Diyosa ng karagatan?\n", "Isang karaniwang isda ", "Tagapagbalita ng kaharian ", "Tagapangalaga ng karagatan", "Kaibigan ni Lucia ", "Tagapangalaga ng karagatan "))
                    add(QuestionData("Ano ang iniisip ni Lucia tungkol sa paglilinis?\n", "Mahalaga ito ", "Masaya itong gawin ", "Nakakapagod at hindi mahalaga", "Nakakatulong sa lahat", "Nakakapagod at hindi mahalaga "))
                    add(QuestionData("Ano ang ginawa ng ibang isda bilang paghahanda sa pagbisita ng Diyosa? \n", "Nagtago sa bato ", "Naglaro sa karagatan ", "Naglilinis at nag-ayos ng paligid ", " Natulog buong araw ", "Naglilinis at nag-ayos ng paligid \n"))
                    add(QuestionData("Bakit nagalit ang Diyosa kay Lucia?\n", "Dahil siya ay nawala ", "Dahil hindi siya tumulong at naging tamad", "Dahil siya ay nagkasakit ", "Dahil siya ay umalis sa kaharian", " Dahil hindi siya tumulong at naging tamad"))
                    add(QuestionData("Ano ang parusang ibinigay ng Diyosa kay Lucia? \n", "Pinalayas siya ", "Ginawang pinakapangit na isda ", "Inalis ang kanyang kapangyarihan ", "Ikinulong siya", "Ginawang pinakapangit na isda "))
                    add(QuestionData("Ano ang ipinapakita ng ugali ni Lucia sa kuwento?\n", "Kasipagan ", "Kabaitan", "Katamaran at pagiging makasarili", "Katapatan", "Katamaran at pagiging makasarili"))
                    add(QuestionData("Ano ang aral na matututunan sa kuwento? \n", "Mas mabuting maging bida ", "Hindi mahalaga ang tungkulin", "Dapat gampanan ang responsibilidad kahit walang nakakakita", "Mas masaya ang pagiging tamad", "Dapat gampanan ang responsibilidad kahit walang nakakakita "))
                }, "Station 3. Q&A")
                4 -> openGenericFragment(Book4Station4Game1Fragment(), "Station 4. Games")
                5 -> openStation5WithQuestions(ArrayList<String>().apply {
                    add("1. Ano ang nararamdaman ni Lucia nang isumpa siya ng Diyosa?\n")
                    add("2. Ano ang nararamdaman ng mga janitor fish nang malaman na sila ang magiging anyo ni Lucia?\n")
                    add("3. Ano ang nararamdaman ni Lucia nang marinig niya na bibisita ang Diyosa at maaaring bigyan ng kapangyarihan ang isda na nakatuwa sa kanya?")
                }, "Station 5. \"Emotion Chart\"")
                6 -> openEssay("Station 6. \"ESSAY\"", "Ano ang natutunan mo mula sa ugali ni Lucia sa kuwento?")
                7 -> openEssay("Station 7. \"ESSAY\"", "Bakit mahalaga ang gampanan ang ating tungkulin kahit walang nakatingin?")
                8 -> openEssay("Station 8. \"ESSAY\"", "Kung ikaw si Lucia, paano mo gagawin ang paglilinis bago dumating ang Diyosa?")
                9 -> openEssay("Station 9. \"ESSAY\"", "Paano mo maiwasan ang pagiging tamad o makasarili tulad ni Lucia?")
                10 -> openEssay("Station 10. \"ESSAY\"", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?\n\n\n")
                else -> openGenericStation(stationNumber)
            }
        } else if (currentBookId == 5) {
            when (stationNumber) {
                1 -> openGenericFragment(Book5Station1Fragment(), "Station 1. MAZE")
                2 -> openStory(
                    "Isang malaking kweba ang tahanan ng isang Leon. Sa tagal ng panahon, ang Leon ay mahina na at matanda na. Nakahiga na lamang ito at naghihintay ng kanyang katapusan. Isang aral, namataan niya ang isang Kabayo na tumatawid. Gustong-gusto niya itong kainin. Alam niyang mas mabilis itong tumakbo. Umisip siya ng mabuting paraan para makalapit dito. 'Lahat ng hayop dito ay ako lamang ang nakaka-alam ng lunas sa anumang sakit,' wika ng Leon. 'Talaga?' manghang sagot ng Kabayo. 'Sige, subukin mo ako.' Muling wika ng Leon. Paika-ikang lumapit ang Kabayo. Nagkunwaring may masakit, ang Leon. 'Natinik yata ang paa ko,' at hiniling na ipakita sa Kabayo. Sa kanyang pagpansin, buong lakas na sisilain ng Leon ang Kabayo. Ngunit bigla nitong itinaas ang paa at sinipa ang Leon sabay takbo ng tumatawang Kabayo. Nauwi sa wala ang maiitim na balak ng palalong Leon.",
                    "Station 2. \"ANG LEON AT ANG KABAYO\"",
                    R.drawable.book5cover
                )
                3 -> openQuiz(ArrayList<QuestionData>().apply {
                    add(QuestionData("Sino ang dalawang hayop sa kwento?", "Si Pagong at si Pusa", "Si Kabayo at si Leon", "Si Isda at si Tigre", "Si Kuneho at si Ibon", "Si Kabayo at si Leon"))
                    add(QuestionData("Ano ang binabalak ng Leon sa Kabayo?", "Sipain", "Laruin", "Kainin", "Tawanan", "Kainin"))
                    add(QuestionData("Ano ang kalagayan ng leon sa simula ng kuwento?", "Mahina at matanda na", "Malakas at bata pa", "Makisig", "Mabait", "Mahina at matanda na"))
                    add(QuestionData("Anong paraan ang naisip ng leon para makalapit sa kabayo?", "Nagkunwaring may alam na lunas sa anumang sakit", "Binigyan niya ng maraming pagkain ang kabayo", "Nakipaglaro ang leon sa kabayo", "Wala siyang ginawa", "Nagkunwaring may alam na lunas sa anumang sakit"))
                    add(QuestionData("Ano ang napansin ng kabayo habang papalapit siya sa leon?", "Napansin niya na mahina ang leon", "Napansin niya na tumatawa ang leon", "Napansin niya na may ubo ang leon", "Napansin niya na buong lakas na sisilain siya ng leon", "Napansin niya na buong lakas na sisilain siya ng leon"))
                    add(QuestionData("Ano ang ginawa ng kabayo upang mapigilan ang balak ng leon?", "Tinawanan niya ang leon", "Itinaas niya ang kanyang mga paa at buong lakas na sinipa ang leon", "Tumalon siya sa harapan ng leon", "Kinagat niya ang leon", "Itinaas niya ang kanyang mga paa at buong lakas na sinipa ang leon"))
                    add(QuestionData("Saan nagkita sina leon at kabayo?", "Sa dagat", "Sa bundok", "Sa ilog", "Sa kweba na may malalaking bato", "Sa kweba na may malalaking bato"))
                    add(QuestionData("Ano ang ginagawa ng leon habang nakita niya ang kabayo?", "Nakatayo", "Nakahiga", "Nakaupo", "Nanonood", "Nakahiga"))
                    add(QuestionData("Ano ang ginagawa ng kabayo noong nakita siya ni leon?", "Natutulog", "Kumakain", "Tumatawid", "Tumatakbo", "Tumatawid"))
                    add(QuestionData("Ano ang tema ng kwento?", "Pagkagulat o pagharap sa sorpresa", "Pagkakakibigan", "Pagtutulungan", "Pagkakasundo", "Pagkagulat o pagharap sa sorpresa"))
                }, "Station 3. Q&A")
                4 -> openGenericFragment(Book5Station4Game1Fragment(), "Station 4. \"GAMES\"")
                5 -> openStation5WithQuestions(ArrayList<String>().apply {
                    add("1. Ano ang nararamdaman ng leon sa pagkatalo?")
                    add("2. Ano ang nararamdaman  ng kabayo sa pagligtas sa sarili?")
                    add("3. Ano ang nararamdaman  ng leon sa pagkawala ng dignidad?")
                }, "Station 5. \"EMOTION CHART\"")
                6 -> openEssay("Station 6. \"ESSAY\"", "Ano ang naging pangunahing pagkakamali ng leon?")
                7 -> openEssay("Station 7. \"ESSAY\"", "Bakit mahalaga ang bilis ng pag-iisip sa harap ng panganib?")
                8 -> openEssay("Station 8. \"ESSAY\"", "Anong katangian ng leon ang hindi nagtagumpay sa kwento?")
                9 -> openEssay("Station 9. \"ESSAY\"", "Paano ipinakita sa kwento ang konsepto ng “maitim na balak?”")
                10 -> openEssay("Station 10. \"ESSAY\"", "Nakatulong ba ang paggamit mo ng Pag-BAUL app upang mas maunawaan niyong mabuti ang kwento?\n\n\n")
                else -> openGenericStation(stationNumber)
            }
        }
    }

    // ============================================================
    // Helper functions to open different fragments
    // ============================================================

    // --- MODIFICATION START ---
    // The only change is in the helper functions below.
    // I am replacing `addToBackStack(null)` with a named transaction.
    // This allows us to return to this "Station Map" screen from any game.

    private fun openFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("STATION_MAP_TRANSACTION") // MODIFIED
            .commit()
    }

    private fun openGenericFragment(fragment: Fragment, title: String) {
        val bundle = Bundle().apply { putString("title", title) }
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("STATION_MAP_TRANSACTION") // MODIFIED
            .commit()
    }

    private fun openStory(storyText: String, title: String, backgroundResId: Int) {
        val fragment = StoryFragment()
        val bundle = Bundle().apply {
            putString(StoryFragment.STORY_TEXT_KEY, storyText)
            putString(StoryFragment.STORY_TITLE_KEY, title)
            putInt(StoryFragment.BG_RES_ID_KEY, backgroundResId)
            putInt(StoryFragment.BOOK_ID_KEY, currentBookId)
        }
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("STATION_MAP_TRANSACTION") // MODIFIED
            .commit()
    }

    private fun openQuiz(questions: ArrayList<QuestionData>, title: String) {
        val fragment = QuizFragment()
        val bundle = Bundle().apply {
            putParcelableArrayList("questions", questions)
            putString("title", title)
        }
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("STATION_MAP_TRANSACTION") // MODIFIED
            .commit()
    }

    private fun openStation5WithQuestions(questions: ArrayList<String>, title: String) {
        val fragment = Station5Fragment()
        val bundle = Bundle().apply {
            putStringArrayList("questions", questions)
            putString("title", title)
        }
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("STATION_MAP_TRANSACTION") // MODIFIED
            .commit()
    }

    private fun openEssay(title: String, question: String) {
        val fragment = EssayFragment()
        val bundle = Bundle().apply {
            putString("title", title)
            putString("question", question)
        }
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("STATION_MAP_TRANSACTION") // MODIFIED
            .commit()
    }

    private fun openTrueOrFalseGame(questions: ArrayList<QuestionData>, title: String) {
        val fragment = Book4Station4Game2Fragment()
        val bundle = Bundle().apply {
            putParcelableArrayList("questions", questions)
            putString("title", title)
        }
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("STATION_MAP_TRANSACTION") // MODIFIED
            .commit()
    }

    // --- MODIFICATION END ---

    private fun openGenericStation(stationNumber: Int) {
        Toast.makeText(context, "Station $stationNumber for Book $currentBookId is not yet implemented.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
