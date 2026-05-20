package ru.sgrstudios.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    Button us_states, rus_subjects;
    public long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selection);

        us_states = findViewById(R.id.us_states_button);
        rus_subjects = findViewById(R.id.rus_subjects_button);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish();
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(SelectionActivity.this, "again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View view) { finish(); }

    public void wip(View view) {
        Toast.makeText(SelectionActivity.this, "WORK IN PROGRESS", Toast.LENGTH_SHORT).show();
    }

    public void openAllUsStates(View view) {
        startActivity(new Intent(this, AllUsStatesActivity.class));
    }

}
