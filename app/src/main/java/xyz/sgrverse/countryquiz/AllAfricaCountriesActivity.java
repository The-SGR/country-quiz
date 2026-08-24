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

public class AllAfricaCountriesActivity extends BaseActivity {

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
        guessed = pm.getGuessed("africa");

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish();
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(AllAfricaCountriesActivity.this, R.string.toast_back_press, Toast.LENGTH_SHORT).show();
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

        counter.setText(guessed.size() + " / 53");

        check.setOnClickListener(v -> {
            String userInput = input.getText().toString().toLowerCase().trim();

            if (aliasMap.containsKey(userInput)) {
                userInput = aliasMap.get(userInput);
            }

            String[] codes = stateMap.get(userInput);

            if (codes != null && !guessed.contains(userInput)) {
                guessed.add(userInput);
                pm.markGuessed("africa", userInput);

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
            counter.setText(guessed.size() + " / 53");

            if (guessed.size() == 53) {
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
            InputStream is = getResources().openRawResource(R.raw.africa);
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            svgContent = scanner.hasNext() ? scanner.next() : "";
            svgContent = svgContent
                    .replace("width=\"1000\"", "")
                    .replace("height=\"1000\"", "")
                    .replace("<svg", "<svg viewBox=\"50 0 1000 1000\"");
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

        String rule = "#" + stateCode + " { fill: #406c14 !important; }";

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
                    String rule = "#" + code + " { fill: #406c14 !important; }";
                    if (!dynamicStyles.contains(rule)) {
                        dynamicStyles += rule + "\n";
                    }
                }
            }
        }
    }

    Map<String, String[]> stateMap = new HashMap<String, String[]>() {{
        put("algeria", new String[]{"dz"});
        put("angola", new String[]{"ao"});
        put("benin", new String[]{"bj"});
        put("botswana", new String[]{"bw"});
        put("burkina faso", new String[]{"bf"});
        put("burundi", new String[]{"bi"});
        put("cabo verde", new String[]{"cv"});
        put("cameroon", new String[]{"cm"});
        put("central african republic", new String[]{"cf"});
        put("chad", new String[]{"td"});
        put("comoros", new String[]{"km"});
        put("democratic republic of the congo", new String[]{"cd"});
        put("republic of the congo", new String[]{"cg"});
        put("djibouti", new String[]{"dj"});
        put("egypt", new String[]{"eg"});
        put("equatorial guinea", new String[]{"gq"});
        put("eritrea", new String[]{"er"});
        put("eswatini", new String[]{"sz"});
        put("ethiopia", new String[]{"et"});
        put("gabon", new String[]{"ga"});
        put("gambia", new String[]{"gm"});
        put("ghana", new String[]{"gh"});
        put("guinea", new String[]{"gn"});
        put("guinea-bissau", new String[]{"gw"});
        put("ivory coast", new String[]{"ci"});
        put("kenya", new String[]{"ke"});
        put("lesotho", new String[]{"ls"});
        put("liberia", new String[]{"lr"});
        put("libya", new String[]{"ly"});
        put("madagascar", new String[]{"mg"});
        put("malawi", new String[]{"mw"});
        put("mali", new String[]{"ml"});
        put("mauritania", new String[]{"mr"});
        put("mauritius", new String[]{"mu"});
        put("morocco", new String[]{"ma"});
        put("mozambique", new String[]{"mz"});
        put("namibia", new String[]{"na"});
        put("niger", new String[]{"ne"});
        put("nigeria", new String[]{"ng"});
        put("rwanda", new String[]{"rw"});
        put("sao tome and principe", new String[]{"st"});
        put("senegal", new String[]{"sn"});
        put("seychelles", new String[]{"sc"});
        put("sierra leone", new String[]{"sl"});
        put("somalia", new String[]{"so"});
        put("south africa", new String[]{"za"});
        put("south sudan", new String[]{"ss"});
        put("sudan", new String[]{"sd"});
        put("tanzania", new String[]{"tz"});
        put("togo", new String[]{"tg"});
        put("tunisia", new String[]{"tn"});
        put("uganda", new String[]{"ug"});
        put("zambia", new String[]{"zm"});
        put("zimbabwe", new String[]{"zw"});
    }};

    Map<String, String> aliasMap = new HashMap<String, String>() {{
        put("car", "central african republic");
        put("drc", "democratic republic of the congo");
        put("côte d'ivoire", "ivory coast");
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
                pm.resetQuiz("africa");
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
