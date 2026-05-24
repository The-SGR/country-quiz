package ru.sgrstudios.countryquiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends BaseActivity {

    MaterialSwitch amoledSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean amoled = prefs.getBoolean("amoled", false);

        if (amoled) {
            setTheme(R.style.Amoled_Theme_CountryQuiz);
        } else {
            setTheme(R.style.Base_Theme_CountryQuiz);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        amoledSwitch = findViewById(R.id.amoledSwitch);
        amoledSwitch.setChecked(amoled);
        amoledSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("amoled", isChecked).apply();
            recreate();
        });
    }

    public void back(View view) {
        finish();
    }

}
