package ru.sgrstudios.countryquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AllRusSubjectsActivity extends BaseActivity {

    public long backPressedTime = 0;
    private String svgContent;
    WebView mapView;

    @SuppressLint("SetTextI18n") //TODO: ДОРАБОТАТЬ!!!!!!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_rus_subjects);

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish();
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(AllRusSubjectsActivity.this, R.string.toast_back_press, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mapView = findViewById(R.id.mapView);
        EditText input = findViewById(R.id.inputState);
        Button check = findViewById(R.id.check_btn);
        TextView counter = findViewById(R.id.counter_text);

        WebSettings webSettings = mapView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        mapView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        check.setOnClickListener(v -> {
            String userInput = input.getText().toString().toLowerCase().trim();

            if (aliasMap.containsKey(userInput)) {
                userInput = aliasMap.get(userInput);
            }

            String[] codes = stateMap.get(userInput);

            if (codes != null && !guessed.contains(userInput)) {
                guessed.add(userInput);

                for (String code : codes) {
                    colorState(code);
                }

                Toast.makeText(this, R.string.toast_correct, Toast.LENGTH_SHORT).show();
            } else if (guessed.contains(userInput)) {
                Toast.makeText(this, R.string.toast_already_guessed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.toast_incorrect, Toast.LENGTH_SHORT).show();
            }

            input.setText("");
            counter.setText(guessed.size() + " / 83");

            if (guessed.size() == 47) {
                Toast.makeText(this, R.string.toast_win, Toast.LENGTH_LONG).show();
            }
        });

        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                check.performClick();
                return true;
            }
            return false;
        });

        loadSvg();
    }

    private void loadSvg() {
        try {
            InputStream is = getResources().openRawResource(R.raw.rus);
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            svgContent = scanner.hasNext() ? scanner.next() : "";
            svgContent = svgContent
                    .replace("width=\"1650\"", "")
                    .replace("height=\"1000\"", "")
                    .replace("<svg", "<svg viewBox=\"0 0 1650 1000\"");
            scanner.close();


            if (svgContent == null || svgContent.isEmpty()) {
                Toast.makeText(this, R.string.toast_svg_error, Toast.LENGTH_LONG).show();
                return;
            }

            renderSvg();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderSvg() {

        String html =
                "<html><head>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "<style>" +
                        "body { margin:0; background-color: transparent; }" +
                        "svg { width:100%; height:auto; }" +

                        dynamicStyles +

                        "</style></head><body>" +
                        svgContent +
                        "</body></html>";

        mapView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    private String dynamicStyles = "";

    private void colorState(String stateCode) {

        String rule = "#" + stateCode + " { fill: #406c14 !important; }";

        if (!dynamicStyles.contains(rule)) {
            dynamicStyles += rule + "\n";
        }

        renderSvg();
    }

    Set<String> guessed = new HashSet<>();

    Map<String, String[]> stateMap = new HashMap<String, String[]>() {{
        put("adygea", new String[]{"Adygeya"});
        put("altai republic", new String[]{"AltaiRepublic"});
        put("altai krai", new String[]{"AltaiKrai"});
        put("amur oblast", new String[]{"AmurOblast"});
        put("arkhangelsk oblast", new String[]{"ArkhangelskOblast", "path45", "path47", "path49", "path51", "path53", "path55", "path57", "path59"});
        put("astrakhan oblast", new String[]{"AstrakhanOblast"});
        put("bashkortostan", new String[]{"Bashkortostan"});
        put("belgorod oblast", new String[]{"BelgorodOblast"});
        put("bryansk oblast", new String[]{"BryanskOblast"});
        put("buryatia", new String[]{"Buryatia"});
        put("chechen republic", new String[]{"Chechnya"});
        put("chelyabinsk oblast", new String[]{"ChelyabinskOblast"});
        put("chukotka", new String[]{"Chukotka"});
        put("chuvash republic", new String[]{"Chuvashia"});
        put("dagestan", new String[]{"Dagestan"});
        put("ingushetia", new String[]{"Ingushetia"});
        put("irkutsk oblast", new String[]{"IrkutskOblast"});
        put("ivanovo oblast", new String[]{"IvanovoOblast"});
        put("jewish autonomous oblast", new String[]{"JewishAutOblast"});
        put("kabardino-balkaria", new String[]{"KabardinoBalkaria"});
        put("kaliningrad oblast", new String[]{"KaliningradOblast"});
        put("kalmykia", new String[]{"Kalmykia"});
        put("kaluga oblast", new String[]{"KalugaOblast"});
        put("kamchatka krai", new String[]{"KamchatkaKrai"});
        put("karachay-cherkessia", new String[]{"KarachayCherkessia"});
        put("karelia", new String[]{"Karelia"});
        put("kemerovo oblast", new String[]{"KemerovoOblast"});
        put("khabarovsk krai", new String[]{"KhabarovskKrai"});
        put("khakassia", new String[]{"Khakassia"});
        put("khanty-mansi autonomous okrug", new String[]{"KhantiaMansia"});
        put("komi", new String[]{"KomiRep"});
        put("kostroma oblast", new String[]{"KostromaOblast"});
        put("kirov oblast", new String[]{"KirovOblast"});
        put("krasnodar krai", new String[]{"KrasnodarKrai"});
        put("krasnoyarsk krai", new String[]{"path107", "path109", "path111", "path113"});
        put("kurgan oblast", new String[]{"KurganOblast"});
        put("kursk oblast", new String[]{"KurskOblast"});
        put("leningrad oblast", new String[]{"LeningradOblast"});
        put("lipetsk oblast", new String[]{"LipetskOblast"});
        put("magadan oblast", new String[]{"MagadanOblast"});
        put("mari el", new String[]{"MariEl"});
        put("mordovia", new String[]{"Mordovia"});
        put("moscow", new String[]{"Moscow"});
        put("moscow oblast", new String[]{"MoscowOblast"});
        put("murmansk oblast", new String[]{"MurmanskOblast"});
        put("nenets autonomous okrug", new String[]{"NenetsAutDistrict"});
        put("nizhny novgorod oblast", new String[]{"NizhnyNovgorodOblast"});
        put("north ossetia", new String[]{"NorthOssetia"});
        put("novgorod oblast", new String[]{"NovgorodOblast"});
        put("novosibirsk oblast", new String[]{"NovosibirskOblast"});
        put("omsk oblast", new String[]{"OmskOblast"});
        put("orenburg oblast", new String[]{"OrenburgOblast"});
        put("oryol oblast", new String[]{"OryolOblast"});
        put("penza oblast", new String[]{"PenzaOblast"});
        put("perm krai", new String[]{"PermKrai"});
        put("primorsky krai", new String[]{"PrimorskyKrai"});
        put("pskov oblast", new String[]{"PskovOblast"});
        put("rostov oblast", new String[]{"RostovOblast"});
        put("ryazan oblast", new String[]{"RyazanOblast"});
        put("saint petersburg", new String[]{"SaintPetersburg"});
        put("sakhalin oblast", new String[]{"SakhalinOblast"});
        put("sakha", new String[]{"Yakutia"});
        put("samara oblast", new String[]{"SamaraOblast"});
        put("saratov oblast", new String[]{"SaratovOblast"});
        put("smolensk oblast", new String[]{"SmolenskOblast"});
        put("stavropol krai", new String[]{"StavropolKrai"});
        put("sverdlovsk oblast", new String[]{"SverdlovskOblast"});
        put("tambov oblast", new String[]{"TambovOblast"});
        put("tatarstan", new String[]{"Tatarstan"});
        put("tomsk oblast", new String[]{"TomskOblast"});
        put("tula oblast", new String[]{"TulaOblast"});
        put("tuva", new String[]{"Tuva"});
        put("tver oblast", new String[]{"TverOblast"});
        put("tyumen oblast", new String[]{"TyumenOblast"});
        put("udmurtia", new String[]{"Udmurtia"});
        put("ulyanovsk oblast", new String[]{"UlyanovskOblast"});
        put("vladimir oblast", new String[]{"VladimirOblast"});
        put("volgograd oblast", new String[]{"VolgogradOblast"});
        put("vologda oblast", new String[]{"VologdaOblast"});
        put("voronezh oblast", new String[]{"VoronezhOblast"});
        put("yamalo-nenets autonomous okrug", new String[]{"YamaloNenetsAutDistrict"});
        put("yaroslavl oblast", new String[]{"YaroslavlOblast"});
        put("zabaykalsky krai", new String[]{"ChitaOblast"});
    }};

    Map<String, String> aliasMap = new HashMap<String, String>() {{
        put("adygeya", "adygea");
        put("alania", "north ossetia");

        put("altay", "altai republic");
        put("altai", "altai republic");

        put("amur", "amur oblast");
        put("arkhangelsk", "arkhangelsk oblast");
        put("astrakhan", "astrakhan oblast");
        put("belgorod", "belgorod oblast");
        put("bryansk", "bryansk oblast");

        put("bashkiria", "bashkortostan");
        put("ufa", "bashkortostan");

        put("chechnya", "chechen republic");
        put("chechenia", "chechen republic");
        put("chelyabinsk", "chelyabinsk oblast");

        put("chita", "chita oblast");
        put("zabaykalye", "chita oblast");
        put("transbaikal", "chita oblast");
        put("zabaikalye", "chita oblast");

        put("chuvashia", "chuvash republic");

        put("irkutsk", "irkutsk oblast");
        put("ivanovo", "ivanovo oblast");

        put("birobidzhan", "jewish autonomous oblast");
        put("eao", "jewish autonomous oblast");
        put("evreyskaya avtonomnaya oblast", "jewish autonomous oblast");

        put("kabardino balkaria", "kabardino-balkaria");
        put("kabardino balkar republic", "kabardino-balkaria");

        put("kaliningrad", "kaliningrad oblast");
        put("kaluga", "kaluga oblast");
        put("kamchatka", "kamchatka krai");

        put("karachay cherkessia", "karachay-cherkessia");
        put("karachaevo-cherkessia", "karachay-cherkessia");

        put("petrozavodsk", "karelia");

        put("kemerovo", "kemerovo oblast");
        put("kuzbass", "kemerovo oblast");

        put("khabarovsk", "khabarovsk krai");

        put("khanty-mansi", "khanty-mansi autonomous okrug");
        put("khanty mansi", "khanty-mansi autonomous okrug");
        put("hmao", "khanty-mansi autonomous okrug");

        put("kostroma", "kostroma oblast");
        put("kirov", "kirov oblast");
        put("krasnodar", "krasnodar krai");
        put("krasnoyarsk", "krasnoyarsk krai");
        put("kurgan", "kurgan oblast");
        put("kursk", "kursk oblast");
        put("leningrad", "leningrad oblast");
        put("lipetsk", "lipetsk oblast");
        put("magadan", "magadan oblast");

        put("msk", "moscow");
        put("moscow city", "moscow");

        put("murmansk", "murmansk oblast");

        put("nenets", "nenets autonomous okrug");
        put("nao", "nenets autonomous okrug");

        put("nizhny novgorod", "nizhny novgorod oblast");
        put("novgorod", "novgorod oblast");
        put("novosibirsk", "novosibirsk oblast");
        put("omsk", "omsk oblast");
        put("orenburg", "orenburg oblast");

        put("oryol", "oryol oblast");
        put("orel", "oryol oblast");

        put("penza", "penza oblast");
        put("perm", "perm krai");

        put("primorye", "primorsky krai");
        put("primorsky", "primorsky krai");
        put("vladivostok", "primorsky krai");

        put("pskov", "pskov oblast");

        put("rostov", "rostov oblast");
        put("rostov on don", "rostov oblast");

        put("ryazan", "ryazan oblast");

        put("st petersburg", "saint petersburg");
        put("st. petersburg", "saint petersburg");
        put("saint-petersburg", "saint petersburg");
        put("st petersburg city", "saint petersburg");
        put("spb", "saint petersburg");

        put("sakha republic", "sakha");
        put("yakutia", "sakha");

        put("sakhalin", "sakhalin oblast");
        put("samara", "samara oblast");
        put("saratov", "saratov oblast");
        put("smolensk", "smolensk oblast");
        put("stavropol", "stavropol krai");

        put("sverdlovsk", "sverdlovsk oblast");
        put("yekaterinburg", "sverdlovsk oblast");
        put("ekaterinburg", "sverdlovsk oblast");
        put("ekb", "sverdlovsk oblast");
        put("yekb", "sverdlovsk oblast");

        put("tambov", "tambov oblast");
        put("kazan", "tatarstan");
        put("tomsk", "tomsk oblast");
        put("tula", "tula oblast");
        put("tyva", "tuva");
        put("tver", "tver oblast");
        put("tyumen", "tyumen oblast");

        put("udmurt republic", "udmurtia");
        put("izhevsk", "udmurtia");

        put("ulyanovsk", "ulyanovsk oblast");
        put("vladimir", "vladimir oblast");


        put("stalingrad", "volgograd oblast");
        put("volgograd", "volgograd oblast");
        put("vlg", "volgograd oblast");

        put("vologda", "vologda oblast");
        put("voronezh", "voronezh oblast");

        put("yamal", "yamalo-nenets autonomous okrug");
        put("yamal-nenets", "yamalo-nenets autonomous okrug");
        put("yanao", "yamalo-nenets autonomous okrug");

        put("yaroslavl", "yaroslavl oblast");
    }};

    public void back(View view) { finish(); }
}
