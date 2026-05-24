package ru.sgrstudios.countryquiz;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private boolean currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        currentTheme = prefs.getBoolean("amoled", false);

        if (currentTheme) {
            setTheme(R.style.Amoled_Theme_CountryQuiz);
        } else {
            setTheme(R.style.Base_Theme_CountryQuiz);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean newTheme = prefs.getBoolean("amoled", false);

        if (newTheme != currentTheme) {
            recreate();
        }
    }
}