package ru.sgrstudios.countryquiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.sgrstudios.countryquiz.BuildConfig;

public class MainActivity extends AppCompatActivity {

    public long backPressedTime = 0;
    Button gitBtn;
    Button tgBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView versionText = findViewById(R.id.versionText);
        versionText.setText(BuildConfig.VERSION_NAME);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish();
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(MainActivity.this, R.string.toast_back_press, Toast.LENGTH_SHORT).show();
                }
            }
        });

        gitBtn = findViewById(R.id.github_btn); //я нагло скопировал это из силли кликера. Даже переменные не менял. Какой я негодяй

        gitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirect = new Intent(Intent.ACTION_VIEW);
                redirect.setData(Uri.parse("https://github.com/The-SGR"));
                startActivity(redirect);
            }
        });

        tgBtn = findViewById(R.id.tg_btn);
        tgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirect = new Intent(Intent.ACTION_VIEW);
                redirect.setData(Uri.parse("https://t.me/the_sgr1"));
                startActivity(redirect);
            }
        });

        TextView tg = findViewById(R.id.textView2);
        tg.setMovementMethod(LinkMovementMethod.getInstance());

        UpdateChecker.checkForUpdates(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void openSelection(View view) {
        startActivity(new Intent(this, SelectionActivity.class));
    }

//    public void check(View view) {
//        UpdateChecker.checkForUpdates(this);
//    }

}