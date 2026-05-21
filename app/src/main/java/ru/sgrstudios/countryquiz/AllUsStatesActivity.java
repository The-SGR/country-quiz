package ru.sgrstudios.countryquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AllUsStatesActivity extends AppCompatActivity {

    public long backPressedTime = 0;
    private String svgContent;
    WebView mapView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_us_states);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish();
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(AllUsStatesActivity.this, R.string.toast_back_press, Toast.LENGTH_SHORT).show();
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
            String code = stateMap.get(userInput);

            if (code != null && !guessed.contains(userInput)) {
                guessed.add(userInput);

                colorState(code);

                Toast.makeText(this, R.string.toast_correct, Toast.LENGTH_SHORT).show();
            } else if (guessed.contains(userInput)) {
                Toast.makeText(this, R.string.toast_already_guessed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.toast_incorrect, Toast.LENGTH_SHORT).show();
            }

            input.setText("");
            counter.setText(guessed.size() + " / 50");

            if (guessed.size() == 50) {
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
            InputStream is = getResources().openRawResource(R.raw.us);
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            svgContent = scanner.hasNext() ? scanner.next() : "";
            svgContent = svgContent
                    .replace("width=\"959\"", "")
                    .replace("height=\"593\"", "")
                    .replace("<svg", "<svg viewBox=\"0 0 959 593\"");
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

        String rule = "." + stateCode + " { fill: #00FF00 !important; }";

        if (!dynamicStyles.contains(rule)) {
            dynamicStyles += rule + "\n";
        }

        renderSvg();
    }
    Set<String> states = new HashSet<>(Arrays.asList(
            "alabama", "alaska", "arizona", "arkansas", "california",
            "colorado", "connecticut", "delaware", "florida", "georgia",
            "hawaii", "idaho", "illinois", "indiana", "iowa",
            "kansas", "kentucky", "louisiana", "maine", "maryland",
            "massachusetts", "michigan", "minnesota", "mississippi", "missouri",
            "montana", "nebraska", "nevada", "new hampshire", "new jersey",
            "new mexico", "new york", "north carolina", "north dakota", "ohio",
            "oklahoma", "oregon", "pennsylvania", "rhode island", "south carolina",
            "south dakota", "tennessee", "texas", "utah", "vermont",
            "virginia", "washington", "west virginia", "wisconsin", "wyoming"
    ));

    Set<String> guessed = new HashSet<>();

    Map<String, String> stateMap = new HashMap<String, String>() {{
        put("alabama", "al");
        put("alaska", "ak");
        put("arizona", "az");
        put("arkansas", "ar");
        put("california", "ca");
        put("colorado", "co");
        put("connecticut", "ct");
        put("delaware", "de");
        put("florida", "fl");
        put("georgia", "ga");
        put("hawaii", "hi");
        put("idaho", "id");
        put("illinois", "il");
        put("indiana", "in");
        put("iowa", "ia");
        put("kansas", "ks");
        put("kentucky", "ky");
        put("louisiana", "la");
        put("maine", "me");
        put("maryland", "md");
        put("massachusetts", "ma");
        put("michigan", "mi");
        put("minnesota", "mn");
        put("mississippi", "ms");
        put("missouri", "mo");
        put("montana", "mt");
        put("nebraska", "ne");
        put("nevada", "nv");
        put("new hampshire", "nh");
        put("new jersey", "nj");
        put("new mexico", "nm");
        put("new york", "ny");
        put("north carolina", "nc");
        put("north dakota", "nd");
        put("ohio", "oh");
        put("oklahoma", "ok");
        put("oregon", "or");
        put("pennsylvania", "pa");
        put("rhode island", "ri");
        put("south carolina", "sc");
        put("south dakota", "sd");
        put("tennessee", "tn");
        put("texas", "tx");
        put("utah", "ut");
        put("vermont", "vt");
        put("virginia", "va");
        put("washington", "wa");
        put("west virginia", "wv");
        put("wisconsin", "wi");
        put("wyoming", "wy");
    }};

    public void back(View view) { finish(); }

    // я так задолбался делать это
    // az963258: тебе ещё субъекты делать :-)
}
