package ru.sgrstudios.countryquiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

public class SettingsActivity extends BaseActivity {

    Spinner themeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String theme = prefs.getString("theme", "default");

        switch (theme) {
            case "amoled":
                setTheme(R.style.Amoled_Theme_CountryQuiz);
                break;
            case "dark":
                setTheme(R.style.Dark_Theme_CountryQuiz);
                break;
            case "blue":
                setTheme(R.style.Blue_Theme_CountryQuiz);
                break;
            default:
                setTheme(R.style.Base_Theme_CountryQuiz);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        themeSpinner = findViewById(R.id.themeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.themes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        themeSpinner.setAdapter(adapter);

        switch (theme) {
            case "amoled":
                themeSpinner.setSelection(1);
                break;
            case "dark":
                themeSpinner.setSelection(2);
                break;
            case "blue":
                themeSpinner.setSelection(3);
                break;
            default:
                themeSpinner.setSelection(0);
                break;
        }

        themeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedTheme = "default";
                        switch (position) {
                            case 1:
                                selectedTheme = "amoled";
                                break;
                            case 2:
                                selectedTheme = "dark";
                                break;
                            case 3:
                                selectedTheme = "blue";
                                break;
                        }

                        if(selectedTheme.equals(theme)) {
                            return;
                        }

                        prefs.edit().putString("theme", selectedTheme).apply();
                        recreate();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    public void back(View view) {
        finish();
    }

}
