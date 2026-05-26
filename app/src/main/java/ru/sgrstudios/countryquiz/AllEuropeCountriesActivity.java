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

public class AllEuropeCountriesActivity extends BaseActivity {

    public long backPressedTime = 0;
    private String svgContent;
    WebView mapView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_europe_countries);

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish();
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(AllEuropeCountriesActivity.this, R.string.toast_back_press, Toast.LENGTH_SHORT).show();
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
            counter.setText(guessed.size() + " / 47");

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
            InputStream is = getResources().openRawResource(R.raw.europe);
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            svgContent = scanner.hasNext() ? scanner.next() : "";
            svgContent = svgContent
                    .replace("width=\"593\"", "")
                    .replace("height=\"606\"", "")
                    .replace("<svg", "<svg viewBox=\"50 0 500 600\"");
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
                        "html, body {" +
                        "margin:0;" +
                        "padding:0;" +
                        "width:100%;" +
                        "height:100%;" +
                        "overflow:hidden;" +
                        "background-color: transparent;" +
                        "}" +

                        "svg {" +
                        "display:block;" +
                        "width:100%;" +
                        "height:100%;" +
                        "}" +

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
        put("albania", new String[]{"al"});
        put("andorra", new String[]{"ad"});
        put("austria", new String[]{"at"});
        put("belarus", new String[]{"by"});
        put("belgium", new String[]{"be"});
        put("bosnia and herzegovina", new String[]{"ba"});
        put("bosnia", new String[]{"ba"});
        put("bulgaria", new String[]{"bg"});
        put("croatia", new String[]{"hr"});
        put("cyprus", new String[]{"cy", "nc"});
        put("czechia", new String[]{"cz"});
        put("czech", new String[]{"cz"});
        put("czech republic", new String[]{"cz"});
        put("denmark", new String[]{"dk", "fo"});
        put("estonia", new String[]{"ee"});
        put("finland", new String[]{"fi"});
        put("france", new String[]{"fr"});
        put("germany", new String[]{"de"});
        put("greece", new String[]{"gr"});
        put("hungary", new String[]{"hu"});
        put("iceland", new String[]{"is"});
        put("ireland", new String[]{"ie"});
        put("italy", new String[]{"it"});
        put("kosovo", new String[]{"xk"});
        put("latvia", new String[]{"lv"});
        put("liechtenstein", new String[]{"li"});
        put("lithuania", new String[]{"lt"});
        put("luxembourg", new String[]{"lu"});
        put("malta", new String[]{"mt"});
        put("moldova", new String[]{"md", "transnistria1", "transnistria2", "transnistria3"});
        put("moldavia", new String[]{"md", "transnistria1", "transnistria2", "transnistria3"});
        put("monaco", new String[]{"mc"});
        put("montenegro", new String[]{"me"});
        put("netherlands", new String[]{"nl"});
        put("macedonia", new String[]{"mk"});
        put("north macedonia", new String[]{"mk"});
        put("norway", new String[]{"no"});
        put("poland", new String[]{"pl"});
        put("portugal", new String[]{"pt"});
        put("romania", new String[]{"ro"});
        put("russia", new String[]{"ru-main", "ru-kgd", "crimea_disputed"});
        put("san marino", new String[]{"sm"});
        put("serbia", new String[]{"rs"});
        put("slovakia", new String[]{"sk"});
        put("slovenia", new String[]{"si"});
        put("spain", new String[]{"es"});
        put("sweden", new String[]{"se"});
        put("switzerland", new String[]{"ch"});
        put("turkey", new String[]{"tr"});
        put("ukraine", new String[]{"ua"});
        put("united kingdom", new String[]{"gb-gbn", "im", "gb-nir"});
        put("uk", new String[]{"gb-gbn", "im", "gb-nir"});
        put("great britain", new String[]{"gb-gbn", "im", "gb-nir"});
        put("vatican", new String[]{"va"});
        put("vatican city", new String[]{"va"});
    }};

    Map<String, String> aliasMap = new HashMap<String, String>() {{
        put("uk", "united kingdom");
        put("great britain", "united kingdom");
        put("north macedonia", "macedonia");
        put("bosnia and herzegovina", "bosnia");
        put("czech republic", "czechia");
        put("czech", "czechia");
        put("vatican city", "vatican");
        put("moldova", "moldavia");
    }};

    public void back(View view) { finish(); }
}
