package ru.sgrstudios.countryquiz;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private String currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        currentTheme = prefs.getString("theme", "default");

        switch (currentTheme) {
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
                break;
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String newTheme = prefs.getString("theme", "default");

        if (!newTheme.equals(currentTheme)) {
            recreate();
        }
    }
}