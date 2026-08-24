package xyz.sgrverse.countryquiz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AllOceaniaCountriesActivity extends BaseActivity {

    public long backPressedTime = 0;
    private String svgContent;
    WebView mapView;

    private ProgressManager pm;
    private Set<String> guessed;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        pm = new ProgressManager(this);
        guessed = pm.getGuessed("oceania");

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish();
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(AllOceaniaCountriesActivity.this, R.string.toast_back_press, Toast.LENGTH_SHORT).show();
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

        counter.setText(guessed.size() + " / 13");

        check.setOnClickListener(v -> {
            String userInput = input.getText().toString().toLowerCase().trim();

            if (aliasMap.containsKey(userInput)) {
                userInput = aliasMap.get(userInput);
            }

            String[] codes = stateMap.get(userInput);

            if (codes != null && !guessed.contains(userInput)) {
                guessed.add(userInput);
                pm.markGuessed("oceania", userInput);

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
            counter.setText(guessed.size() + " / 13");

            if (guessed.size() == 13) {
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
        restoreProgress();
    }

    private void loadSvg() {
        try {
            InputStream is = getResources().openRawResource(R.raw.oceania);
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            svgContent = scanner.hasNext() ? scanner.next() : "";
            svgContent = svgContent
                    .replace("width=\"266\"", "")
                    .replace("height=\"201\"", "")
                    .replace("<svg", "<svg viewBox=\"61 595 716.338 541.36\"");
            scanner.close();


            if (svgContent == null || svgContent.isEmpty()) {
                Toast.makeText(this, R.string.toast_svg_error, Toast.LENGTH_LONG).show();
                return;
            }

            restoreProgress();
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

        String rule = "." + stateCode + " { fill: #406c14 !important; }";

        if (!dynamicStyles.contains(rule)) {
            dynamicStyles += rule + "\n";
        }

        renderSvg();
    }

    private void restoreProgress() {
        for (String country : guessed) {
            String[] codes = stateMap.get(country);
            if (codes != null) {
                for (String code : codes) {
                    String rule = "." + code + " { fill: #406c14 !important; }";
                    if (!dynamicStyles.contains(rule)) {
                        dynamicStyles += rule + "\n";
                    }
                }
            }
        }
    }

    Map<String, String[]> stateMap = new HashMap<String, String[]>() {{
        put("australia", new String[]{"au"});
        put("fiji", new String[]{"fj"});
        put("kiribati", new String[]{"ki"});
        put("marshall islands", new String[]{"mh"});
        put("micronesia", new String[]{"fm"});
        put("nauru", new String[]{"nr"});
        put("new zealand", new String[]{"nz"});
        put("palau", new String[]{"pw"});
        put("papua new guinea", new String[]{"pg"});
        put("samoa", new String[]{"ws"});
        put("solomon islands", new String[]{"sb"});
        put("tonga", new String[]{"to"});
        put("tuvalu", new String[]{"tv"});
        put("vanuatu", new String[]{"vu"});
    }};

    Map<String, String> aliasMap = new HashMap<String, String>() {{

    }};

    public void reset(View view) { AlertDialog dialog = createDialog(); dialog.show(); }
    public void back(View view) { finish(); }

    AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.reset_btn);
        builder.setMessage(R.string.reset_alert_msg);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pm.resetQuiz("oceania");
                recreate();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }
}
