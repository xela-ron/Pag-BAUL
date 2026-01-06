package com.example.pag_baul

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.abs

class StoryFragment : Fragment(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_story, container, false)

        // Initialize TextToSpeech
        tts = TextToSpeech(context, this)

        // 1. Get Arguments
        val storyText = arguments?.getString("STORY_PAGE1") ?: ""
        val bookId = arguments?.getInt("BOOK_ID") ?: 1
        val stationTitle = arguments?.getString("STATION_TITLE") ?: "Story"

        // 2. Setup UI Elements
        val tvStoryText = view.findViewById<TextView>(R.id.tvStoryText)
        val tvPageNumber = view.findViewById<TextView>(R.id.tvPageNumber)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val scrollViewStory = view.findViewById<ScrollView>(R.id.scrollViewStory)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackIcon)
        val ivBackground = view.findViewById<ImageView>(R.id.ivStoryBackground)
        // btnReadAloud Removed from layout as per request, so removing logic here

        // Find the Maze Container
        val mazeContainer = view.findViewById<FrameLayout>(R.id.mazeContainer)

        // Set Title if available
        if (tvTitle != null) tvTitle.text = stationTitle

        ivBackground.scaleType = ImageView.ScaleType.CENTER_CROP

        // 3. LOGIC HANDLING
        // Condition: Book 5 AND empty text means it's the Maze Station
        if (bookId == 5 && storyText.isEmpty()) {
            // ============================================================
            // BOOK 5 STATION 1: MAZE MODE
            // ============================================================

            // Hide standard story elements
            if (scrollViewStory != null) scrollViewStory.visibility = View.GONE
            if (btnNext != null) btnNext.visibility = View.GONE
            if (btnDone != null) btnDone.visibility = View.GONE
            
            // Hide the background image
            ivBackground.visibility = View.GONE

            // FORCE ROOT BACKGROUND TO WHITE to prevent black background issues
            view.setBackgroundColor(android.graphics.Color.WHITE)

            // Prepare the Compose View for the Maze
            val composeMazeView = ComposeView(requireContext()).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MazeGameScreen(
                        onWin = {
                            Toast.makeText(context, "Mahusay! Natapos mo ang maze!", Toast.LENGTH_LONG).show()
                            parentFragmentManager.popBackStack()
                        }
                    )
                }
            }

            // Define Layout Parameters to ensure the view FILLS the screen
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            // Add the Maze View
            if (mazeContainer != null) {
                mazeContainer.visibility = View.VISIBLE
                mazeContainer.removeAllViews() // Clean up any previous views
                mazeContainer.addView(composeMazeView, layoutParams)
            } else {
                // Fallback: Add to root layout if container is missing
                val rootLayout = view as ViewGroup
                rootLayout.addView(composeMazeView, layoutParams)
            }

        } else {
            // ============================================================
            // STANDARD STORY MODE
            // ============================================================

            if (mazeContainer != null) {
                mazeContainer.visibility = View.GONE
            }

            // Set Background Cover
            when (bookId) {
                1 -> ivBackground.setImageResource(R.drawable.book1cover)
                2 -> ivBackground.setImageResource(R.drawable.book2cover)
                3 -> ivBackground.setImageResource(R.drawable.book3cover)
                4 -> ivBackground.setImageResource(R.drawable.book4cover)
                5 -> ivBackground.setImageResource(R.drawable.book5cover)
                else -> ivBackground.setImageResource(R.drawable.book1cover)
            }

            ivBackground.visibility = View.VISIBLE
            if (scrollViewStory != null) scrollViewStory.visibility = View.VISIBLE

            if (tvPageNumber != null) tvPageNumber.visibility = View.GONE
            if (btnNext != null) btnNext.visibility = View.GONE
            if (btnDone != null) btnDone.visibility = View.VISIBLE

            if (tvStoryText != null) {
                // Only enable clickable dictionary words for Book 1, 2, 3, 4, and 5
                if (bookId == 1 || bookId == 2 || bookId == 3 || bookId == 4 || bookId == 5) {
                    val vocab = getVocabulary(bookId)
                    val spannable = SpannableString(storyText)
                    var foundAny = false

                    for ((word, definition) in vocab) {
                        // Match whole words, case-insensitive
                        val pattern = Pattern.compile("\\b${Pattern.quote(word)}\\b", Pattern.CASE_INSENSITIVE)
                        val matcher = pattern.matcher(storyText)
                        while (matcher.find()) {
                            foundAny = true
                            val start = matcher.start()
                            val end = matcher.end()
                            // Capture word and definition for the closure
                            val currentWord = word
                            val currentDef = definition

                            val clickableSpan = object : ClickableSpan() {
                                override fun onClick(widget: View) {
                                    // SPEAK OUT THE WORD IMMEDIATELY
                                    speakOut(currentWord)
                                    
                                    AlertDialog.Builder(requireContext())
                                        .setTitle(currentWord)
                                        .setMessage(currentDef)
                                        .setPositiveButton("OK", null)
                                        .show()
                                }

                                override fun updateDrawState(ds: TextPaint) {
                                    // Make it look like normal text
                                    ds.isUnderlineText = false
                                    // We do not change color or bold, so it inherits the TextView's style
                                }
                            }
                            spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    }

                    tvStoryText.text = spannable
                    if (foundAny) {
                        tvStoryText.movementMethod = LinkMovementMethod.getInstance()
                    }
                } else {
                    tvStoryText.text = storyText
                }
            }
        }

        // 4. Button Listeners
        btnDone?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set Language to Filipino, fallback to default if not available
            val result = tts?.setLanguage(Locale("fil", "PH"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language Filipino is not supported, trying default.")
                tts?.language = Locale.getDefault()
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }

    private fun getVocabulary(bookId: Int): Map<String, String> {
        return when (bookId) {
            1 -> mapOf(
                "Sabado" to "Ikapitong araw ng linggo",
                "Umaga" to "Unang bahagi ng araw",
                "Abalang-abala" to "Maraming ginagawa; lubhang okupado",
                "Gawain" to "Trabaho o tungkulin",
                "Karaniwang" to "Madalas mangyari; pangkalahatan",
                "Tanawin" to "Bagay na nakikita; eksena",
                "Masayang" to "Puno ng tuwa o ligaya",
                "Naglalaro" to "Gumagawa ng laro o libangan",
                "Kaiba" to "Hindi katulad ng iba",
                "Madilim" to "Kulang o walang liwanag",
                "Gising" to "Hindi natutulog",
                "Magkapatid" to "Magkakapatid na lalaki at/o babae",
                "Nilinis" to "Inalisan ng dumi",
                "Maliit" to "May limitadong laki o sukat",
                "Bahay" to "Tirahan o lugar kung saan naninirahan",
                "Nilabhan" to "Nilinis gamit ang tubig at sabon",
                "Marurumi" to "May dumi o hindi malinis",
                "Damit" to "Kasuotan o bagay na isinusuot sa katawan",
                "Almusal" to "Pagkain sa umaga",
                "Hinatiran" to "Dinalhan",
                "Ina" to "Nanay; babaeng magulang",
                "Maysakit" to "May karamdaman",
                "Makapagtrabaho" to "Makagawa ng hanapbuhay",
                "Inutusan" to "Binigyan ng gawain o utos",
                "Palengke" to "Pamilihan ng pagkain at bilihin",
                "Listahan" to "Talaan ng mga bagay",
                "Sinipat" to "Tiningnan nang mabuti",
                "Pera" to "Salapi",
                "Sapat" to "Tama o hindi kulang",
                "Prutas" to "Bunga ng halaman na kinakain",
                "Pag-aaral" to "Proseso ng pagkatuto",
                "Sariwa" to "Bago at hindi luma",
                "Materyal" to "Pisikal; may anyo o bagay na nahahawakan",
                "Bagay" to "Item o bagay na maaaring gamitin o hawakan",
                "Salat" to "Kapos o kulang",
                "Mayaman" to "Sagana o maraming tinatangkilik",
                "Pagmamahal" to "Malalim na pag-aalaga at pagtingin",
                "Naluluhang" to "May pumapatak na luha",
                "Wika" to "Sinabi o pahayag"
            )
            2 -> mapOf(
                "Isang" to "Isa; nag-iisa",
                "Araw" to "Panahon ng liwanag mula sa araw; maghapon",
                "Nagtagpo" to "Nagkita; nagtipon sa isang lugar",
                "Tagak" to "Uri ng ibon na may mahaba at payat na tuka",
                "Kalabaw" to "Malaking hayop na ginagamit sa bukid",
                "Parehong" to "Magkasing-katulad; parehong kondisyon",
                "Uhaw" to "Nangangailangan o naghahangad ng inumin",
                "Kumusta" to "Tanong sa kalagayan o kondisyon",
                "Kaibigan" to "Kakilala o kasama na may pagkakaibigan",
                "Bati" to "Pagbati; pagbibigay ng magandang salita",
                "Sagot" to "Tugon o pahayag bilang kasagutan",
                "Malakas" to "Matatag, may kapangyarihan o lakas",
                "Ano" to "Tanong tungkol sa bagay o pangyayari",
                "Ginagawa" to "Isinasagawa; ginagawa",
                "Rito" to "Dito; sa lugar na tinutukoy",
                "Akin" to "Pag-aari ng nagsasalita",
                "Ilog" to "Tubig na dumadaloy sa natural na daluyan",
                "Pagmamalaking" to "Pagpapakita ng kayabangan o pagpapahalaga sa sarili",
                "Sabi" to "Pahayag; sinabi",
                "Giit" to "Pagsasabi nang may paninindigan",
                "Pinagbabawalan" to "Paghihigpit o pagbabawal sa ibang tao",
                "Paniniyak" to "Pagtatanong ng katiyakan; pagsisiguro",
                "Ako" to "Tumutukoy sa nagsasalita",
                "Hari" to "Pinuno o pinakadakilang may kapangyarihan",
                "Lubluban" to "Paglubog o paglubog sa tubig",
                "Maghapon" to "Buong araw o buong panahon mula umaga hanggang gabi",
                "Isda" to "Hayop na naninirahan sa tubig",
                "Binabantayan" to "Minomonitor; inaalagaan o binabantayan",
                "Talagang" to "Tunay o talaga",
                "Inumin" to "Tubig o likido na iniinom",
                "Lahat" to "Kabuuan; buong dami",
                "Tubig" to "Likidong pangunahing inumin at mahalaga sa buhay",
                "Naubos" to "Nawala o nauubos; hindi na natitira",
                "Yan" to "Iyan; tumutukoy sa bagay na binanggit",
                "Saludo" to "Paggalang o pagkilala sa kakayahan ng iba",
                "Utos" to "Paghihikayat o kautusan",
                "Susundin" to "Sasunod o gagawin ayon sa utos",
                "Matalo" to "Natalo o natalisod; hindi nanalo",
                "Tatanghaling" to "Magiging; sumasagisag sa pagkakaroon ng titulo",
                "Payag" to "Pumapayag; nagbigay ng pahintulot",
                "Kapag" to "Kung sakali; sa oras na",
                "Natalo" to "Hindi nanalo; natalisod",
                "Ika'y" to "Ikaw ay; tumutukoy sa kausap",
                "Magiging" to "Magkakaroon ng estado o posisyon",
                "Alipin" to "Tauhan o alipin ng iba",
                "Sinimulan" to "Pinagsimulan o umpisa",
                "Malinamnam" to "Masarap; kaaya-aya sa panlasa",
                "Nagkataon" to "Nangyari; aksidenteng pangyayari",
                "Noon" to "Sa panahong iyon",
                "Malaki" to "May malaking sukat o lawak",
                "Kakainom" to "Patuloy sa pag-inom",
                "Siya'y" to "Siya ay; tumutukoy sa tao o hayop",
                "Lumobo" to "Laki o umunlad; lumaki",
                "Tila" to "Parang; kagaya ng",
                "Buntis" to "May laman sa sinapupunan; punong-puno",
                "Suko" to "Sumuko; tumigil sa paglaban",
                "Alam" to "Marunong o may kaalaman",
                "Nag-umpisa" to "Sinimulan; nagsimula",
                "Kumati" to "Gumalaw o kumilos nang mabilis (tubig sa batis)",
                "Batis" to "Maliit na daluyan ng tubig",
                "Madaling" to "Agad o mabilis na paraan",
                "Komonti" to "Matigil o mawala; magbago ang daloy",
                "Makita" to "Masilayan; mapansin",
                "Nawawala" to "Wala; hindi na matatagpuan",
                "Nanalo" to "Nagwagi; naging panalo",
                "Mula" to "Simula o pinagmulan",
                "Nakasakay" to "Nasa ibabaw ng likod o ibabaw ng hayop",
                "Likod" to "Parte sa katawan ng hayop o tao na nasa ibabaw",
                "Tusan" to "Alipin o tauhan; katulong sa paglilingkod"
            )
            3 -> mapOf(
                "Si" to "Ginagamit bago pangalan ng tao o hayop",
                "Jeff" to "Pangalan ng dyip na mabait sa kuwento",
                "Ay" to "Pandiwa; nagsasaad ng estado o katangian",
                "Mabait" to "Maganda ang ugali; maayos sa pakikitungo",
                "Jeep" to "Sasakyan na pampasahero",
                "Magalang" to "May respeto; mahinahon sa pakikitungo",
                "Siya" to "Tumutukoy sa tao o hayop na binanggit",
                "Pasahero" to "Sumakay o sakay ng sasakyan",
                "Bata" to "Kabataan; hindi pa matanda",
                "Matanda" to "Tao na may edad; hindi bata",
                "Marami" to "Madaming bilang",
                "Natutuwa" to "Masaya o nagagalak",
                "Pakiramdam" to "Damdamin; nararamdaman ng tao",
                "Nakasaakay" to "Nasa ibabaw ng sasakyan; nakasakay",
                "Nanay" to "Babaeng magulang",
                "Gusting" to "Gustong gawin o maranasan",
                "Sumakay" to "Umakyat sa sasakyan",
                "Ingat" to "Mag-ingat; magpakasiguro",
                "Pag-akyat" to "Pagpasok sa sasakyan mula ibaba",
                "Lagging" to "Palaging; tuloy-tuloy",
                "Dahilan" to "Rason; dahilan kung bakit nangyari",
                "Asar" to "Naiinis; nagagalit",
                "Barumbado" to "Masamang ugali; bastos o magaspang",
                "Umalis" to "Lumayo o umalis sa lugar",
                "Daraan" to "Dadaan; tatawid o tatahak",
                "Sigaw" to "Malakas na pag-awit o pagsasalita",
                "Kalsada" to "Daan o lugar ng sasakyan",
                "Bakit" to "Tanong; dahilan kung bakit",
                "Kakasa" to "Nakikipagkumpetensya; nakikipagsabayan",
                "Karera" to "Laban sa bilis o takbo",
                "Hamon" to "Panawagan sa labanan o kompetisyon",
                "Alam" to "May kaalaman",
                "Papatulan" to "Sasagot o tutugon sa hamon",
                "Responsableng" to "Maingat at may pananagutan",
                "Sasakyan" to "Bagay na ginagamit sa transportasyon",
                "Batas" to "Alituntunin o patakaran",
                "Trapiko" to "Daloy ng sasakyan sa kalsada",
                "Sinusunod" to "Tinutupad; pinapairal ang batas o patakaran",
                "Nakikipag-unahan" to "Nakikipagsabayan o nakikipagkarera",
                "Pula" to "Kulay; senyales ng babala sa trapiko",
                "Ilaw" to "Liwanag o signal sa kalsada",
                "Tinitiyak" to "Pinagpapasiguro; sinisiguro ang kaligtasan",
                "Nakatawid" to "Ligtas na nakalampas sa daan",
                "Umandar" to "Umpisa o paggalaw ng sasakyan",
                "Mamasada" to "Pagsisimula ng pagbiyahe o pagbyahe sa kalsada",
                "Naliligo" to "Naghuhugas ng katawan; malinis",
                "Malinis" to "Walang dumi; maayos",
                "Nainis" to "Naiinis o nagagalit",
                "Nainggit" to "Naiinggit o may selos",
                "Nagyabang" to "Ipinagmalaki ang sarili",
                "Uminom" to "Uminom ng likido",
                "Alak" to "Inuming may alkohol",
                "Napakabilis" to "Napakataas ng bilis",
                "Takbo" to "Paggalaw mula isang lugar patungo sa iba",
                "Iskriiits" to "Tunog ng preno o pagbagal ng sasakyan",
                "Braaang" to "Tunog ng pagsalpok o banggaan",
                "Pagsalpok" to "Banggaan o pagtama sa bagay",
                "Puno" to "Halaman na matangkad at may kahoy",
                "Tumulong" to "Nagbigay ng tulong o nakipag-assist",
                "Madala" to "Dinala; inilapit sa tamang lugar",
                "Pagawaan" to "Lugar ng pagkukumpuni o paggawa",
                "Masakit" to "Nagdudulot ng sakit o kirot",
                "Piyesa" to "Bahagi o parte ng sasakyan",
                "Dinalaw" to "Binisita o pinuntahan",
                "Magpagaling" to "Pagalingin; bumuti ang kalagayan",
                "Sana" to "Pahayag ng pag-asang mangyari",
                "Aral" to "Matutunan mula sa karanasan",
                "Muling" to "Muli o ulitin",
                "Mapahamak" to "Malagay sa panganib o delikado",
                "Nagging" to "Naging; naging estado",
                "Responsable" to "Maingat at may pananagutan"
            )
            4 -> mapOf(
                "Masayang" to "Puno ng tuwa o ligaya",
                "Namumuhay" to "Buhay; nabubuhay",
                "Lahat" to "Buong grupo; kabuuan",
                "Klase" to "Uri o kategorya",
                "Isda" to "Hayop na naninirahan sa tubig",
                "Kaharian" to "Lugar na pinamumunuan o nasasakupan ng hari/diyosa",
                "Tagadtala" to "Pangalan ng kaharian ng mga isda",
                "Nagtutulungan" to "Sama-samang pagtulong",
                "Nagmamahalan" to "Pag-aalaga at pagmamalasakit sa isa't isa",
                "Bawat" to "Isa-isa; bawat isa",
                "Buong" to "Kabuuan; walang kulang",
                "Pusong" to "May damdamin o buong puso",
                "Gumaganap" to "Ginagawa ang tungkulin o responsibilidad",
                "Kanyang" to "Pag-aari o pag-aakibat sa tao o bagay",
                "Tungkulin" to "Responsibilidad o obligasyon",
                "Obligasyon" to "Kailangan o tungkulin na gawin",
                "Pangalagaan" to "Alagaan; protektahan",
                "Karagatan" to "Malawak na anyong-tubig; dagat",
                "Maayos" to "Maayos ang kaayusan o kalagayan",
                "Sana" to "Pahayag ng pag-asang mangyari",
                "Dumating" to "Nakarating sa lugar",
                "Lucia" to "Pangalan ng isda na selosa at tamad",
                "Lahi" to "Uri o angkan",
                "Janitor fish" to "Uri ng isda na naglilinis ng dagat; janitor fish",
                "Mainggitin" to "May selos o madaling mainis",
                "Selosa" to "Mainggit sa ibang nakatatanggap ng pansin",
                "Tamad" to "Hindi masipag; ayaw magtrabaho",
                "Gusto'y" to "Hinahangad; nais",
                "Laging" to "Palaging; tuloy-tuloy",
                "Bida" to "Paborito o sentro ng pansin",
                "Kailangan" to "Kailangang gawin o mahalaga",
                "Maghanda" to "Mag-ayos o magplano",
                "Wika" to "Sinabi o pahayag",
                "Tagapagbalita" to "Nag-uulat o nagbabalita",
                "Linggo" to "Panahon ng pitong araw",
                "Mula" to "Simula o pinagmulan",
                "Ngayon" to "Kasalukuyang panahon",
                "Bibisita" to "Pupunta bilang bisita",
                "Diyosa" to "Babaeng may kapangyarihan; espiritwal na pinuno",
                "Malinis" to "Walang dumi; maayos",
                "Matuwa" to "Masiyahan o maging masaya",
                "Narinig" to "Nabalitaan; nakarinig",
                "Mabait" to "May magandang ugali o malasakit",
                "Nakakatuwang" to "Nakapagpapasaya; kaaya-aya",
                "Binibigyan" to "Ibinibigay o ipinararating",
                "Kapangyarihan" to "Lakas o kakayahang espesyal",
                "Nag-isip" to "Nagplano o nag-isip ng paraan",
                "Makaisip" to "Mag-isip o makagawa ng ideya",
                "Paraan" to "Pamamaraan o paraan ng paggawa",
                "Mapansin" to "Mapagtanto o mabigyang pansin",
                "Sumunod" to "Sunod; kasunod",
                "Araw" to "Panahon ng liwanag mula sa araw",
                "Abala" to "Masipag o may ginagawa",
                "Patingin-tingin" to "Pagsilip o paminsang pagtingin",
                "Magpapagod" to "Magsisikap o magtrabaho",
                "Wais" to "Matalino o mapanuri",
                "Nariyan" to "Nasa lugar",
                "Kikilos" to "Gagawa o kumikilos",
                "Alam" to "May kaalaman",
                "Pagbisita" to "Pagpunta bilang bisita",
                "Naka-pormasyon" to "Nakaayos sa tamang ayos o linya",
                "Salubungin" to "Tanggapin; salubungin",
                "Pila" to "Hanay o linya ng mga tao o hayop",
                "Tiniyak" to "Pinagpasiguro; siniguro",
                "Kunwari" to "Ginagaya o nagpapanggap",
                "Makikita" to "Masisilayan o mapapansin",
                "Huwag" to "Pagsasabi ng pagbabawal",
                "Kunwaring" to "Nagpapanggap o nagpapakita ng hindi totoo",
                "Isinusumpa" to "Binabansagan o binibigyan ng kaparusahan",
                "Pangit" to "Hindi maganda; hindi kaakit-akit",
                "Anyo" to "Hitsura o anyo ng katawan",
                "Ugali" to "Karakter o pagkatao",
                "Pinakapangit" to "Pinakamasama sa hitsura o katangian",
                "Mundo" to "Daigdig o kabuuan ng kalikasan"
            )
            5 -> mapOf(
                "Isang" to "Isa; nag-iisa",
                "Malaking" to "Malawak o malaki ang sukat",
                "Kweba" to "Lungga o yungib sa ilalim ng lupa o bato",
                "May" to "Nagkakaroon; nagtataglay",
                "Bato" to "Matigas na bahagi ng lupa o bundok",
                "Leon" to "Uri ng hayop na malakas at mabangis na pusa; hayop na tanging pangunahing karakter sa kwento ",
                "Tagal" to "Mahabang panahon",
                "Panahon" to "Paglipas ng oras",
                "Mahina" to "Walang lakas; hindi malakas",
                "Matanda" to "May edad na; hindi bata",
                "Nakahiga" to "Humiga o nakapahinga sa lupa",
                "Aral" to "Panahon; sa konteksto ng kuwento, araw",
                "Namataan" to "Napansin; nakita",
                "Kabayo" to "Uri ng hayop na ginagamit sa sakahan o sakay",
                "Tumatawid" to "Naglalakad o dumadaan sa isang lugar",
                "Gustong" to "Nais o hinahangad",
                "Kainin" to "Pagkain; baga o bibig sa pagkain",
                "Alam" to "May kaalaman",
                "Mas" to "Higit; mas mataas sa iba",
                "Mabilis" to "Madali o mabilis ang kilos",
                "Tumakbo" to "Paggalaw mula isang lugar patungo sa iba",
                "Umisip" to "Nag-isip o nagplano",
                "Mabuting" to "Maganda o maayos ang intensyon",
                "Para" to "Layunin o dahilan",
                "Makalapit" to "Makalapit o makalapit sa layunin",
                "Lahat" to "Buong grupo; kabuuan",
                "Hayop" to "Nilalang na may buhay, hindi tao",
                "Dito" to "Sa lugar na tinutukoy",
                "Ako" to "Tumutukoy sa nagsasalita",
                "Lamang" to "Isa; tanging",
                "Nakaka-alam" to "May kaalaman o may nalalaman",
                "Lunas" to "Gamot o paraan upang magaling",
                "Anumang" to "Kahit ano; anumang uri",
                "Sakit" to "Karamdaman o kirot",
                "Wika" to "Sinabi o pahayag",
                "Talaga" to "Totoo; totoo nga",
                "Manghang" to "Nagulat o may pagkagulat",
                "Sagot" to "Tugon o pahayag bilang kasagutan",
                "Sige" to "Pahintulot o pagpayag",
                "Subukin" to "Subukan o patunayan",
                "Muling" to "Ulitin; muli",
                "Paika-ika" to "Dahan-dahan o maingat na kilos",
                "Lumapit" to "Naglapit; pumunta malapit sa isang tao",
                "Nagkunwaring" to "Nagpanggap o nagpakita ng hindi totoo",
                "Masakit" to "Nagdudulot ng kirot o sakit",
                "Natinik" to "May tinik; may matulis na bahagi",
                "Paa" to "Parte ng katawan na ginagamit sa paglakad o pagtayo",
                "Hiniling" to "Hiningi; nais o ipinag-utos",
                "Ipakita" to "Iparating o ipakita",
                "Kanyang" to "Pag-aari o pag-aakibat sa tao o bagay",
                "Pansin" to "Napansin o napagtanto",
                "Buong" to "Kabuuan; hindi kulang",
                "Lakas" to "Kapangyarihan o lakas ng katawan",
                "Sisilain" to "Sasalain o sasalain ng paa",
                "Bigla" to "Agad o biglaang kilos",
                "Itinaas" to "Pinataas o inangat",
                "Sinipa" to "Tinapakan o sinipa gamit ang paa",
                "Sabay" to "Kasabay; sabay-sabay",
                "Takbo" to "Paggalaw mula isang lugar patungo sa iba",
                "Tumatawang" to "Tumatawa habang gumagalaw; masayang kilos",
                "Nauwi" to "Naging resulta o nagresulta",
                "Wala" to "Hindi natuloy; wala o hindi nangyari",
                "Maiitim" to "Madilim; kulay itim",
                "Balak" to "Plano o intensyon",
                "Palalong" to "Mapagmataas o mayabang"
            )
            else -> emptyMap()
        }
    }
}

// ============================================================
// COMPOSE MAZE LOGIC
// ============================================================

@Composable
fun MazeGameScreen(onWin: () -> Unit) {
    // 1=Wall, 0=Path, 2=Goal, 3=Start
    val mazeGrid = remember {
        listOf(
            listOf(1, 1, 1, 1, 1, 1, 1, 1),
            listOf(1, 3, 0, 0, 1, 0, 0, 1),
            listOf(1, 1, 1, 0, 1, 0, 1, 1),
            listOf(1, 0, 0, 0, 0, 0, 0, 1),
            listOf(1, 0, 1, 1, 1, 1, 0, 1),
            listOf(1, 0, 0, 0, 0, 1, 0, 1),
            listOf(1, 1, 1, 1, 0, 0, 2, 1),
            listOf(1, 1, 1, 1, 1, 1, 1, 1)
        )
    }

    var playerPos by remember { mutableStateOf(Pair(1, 1)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val rows = mazeGrid.size
                        val cols = mazeGrid[0].size

                        if (size.width > 0 && size.height > 0) {
                            val cellWidth = size.width / cols
                            val cellHeight = size.height / rows

                            val tappedCol = (offset.x / cellWidth).toInt()
                            val tappedRow = (offset.y / cellHeight).toInt()

                            if (tappedRow in 0 until rows && tappedCol in 0 until cols) {
                                val currentRow = playerPos.first
                                val currentCol = playerPos.second
                                val isAdjacent = (abs(tappedRow - currentRow) + abs(tappedCol - currentCol)) == 1

                                if (isAdjacent) {
                                    val cellType = mazeGrid[tappedRow][tappedCol]
                                    if (cellType != 1) { // Not a wall
                                        playerPos = Pair(tappedRow, tappedCol)
                                        if (cellType == 2) onWin()
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            val rows = mazeGrid.size
            val cols = mazeGrid[0].size

            if (size.width > 0 && size.height > 0) {
                val cellWidth = size.width / cols
                val cellHeight = size.height / rows

                for (row in 0 until rows) {
                    for (col in 0 until cols) {
                        val color = when (mazeGrid[row][col]) {
                            1 -> Color(0xFF4E342E) // Wall (Brown)
                            2 -> Color(0xFFC19A6B) // Goal (Light Brown)
                            3 -> Color(0xFFFFD700) // Start (Gold)
                            else -> Color(0xFFFFF3E0) // Path (Cream)
                        }
                        drawRect(
                            color = color,
                            topLeft = Offset(col * cellWidth, row * cellHeight),
                            size = Size(cellWidth, cellHeight)
                        )
                    }
                }

                val playerRadius = (minOf(cellWidth, cellHeight) / 2) - 10f

                drawCircle(
                    color = Color.Red,
                    radius = playerRadius,
                    center = Offset(
                        x = (playerPos.second * cellWidth) + (cellWidth / 2),
                        y = (playerPos.first * cellHeight) + (cellHeight / 2)
                    )
                )
            }
        }
    }
}
