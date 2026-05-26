package ru.sgrstudios.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

public class SelectionActivity extends BaseActivity {

    Button us_states, rus_subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selection);

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        us_states = findViewById(R.id.us_states_button);
        rus_subjects = findViewById(R.id.rus_subjects_button);
    }

    public void back(View view) { finish(); }

    public void wip(View view) {
        Toast.makeText(SelectionActivity.this, "WORK IN PROGRESS", Toast.LENGTH_SHORT).show();
    }

    public void openAllUsStates(View view) {
        startActivity(new Intent(this, AllUsStatesActivity.class));
    }

    public void openAllRusSubjects(View view) {
        startActivity(new Intent(this, AllRusSubjectsActivity.class));
    }

    public void openAllEuropeCountries(View view) {
        startActivity(new Intent(this, AllEuropeCountriesActivity.class));
    }

}
