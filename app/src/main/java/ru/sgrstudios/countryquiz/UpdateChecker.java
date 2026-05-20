package ru.sgrstudios.countryquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    public static void checkForUpdates(Context context) {
        new Thread(() -> {
            try {

                URL url = new URL("https://api.github.com/repos/the-sgr/country-quiz/releases/latest");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject json = new JSONObject(result.toString());
                String latestVersion = json.getString("tag_name");
                String currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                latestVersion = latestVersion.replace("v", "");

                if (!latestVersion.equals(currentVersion)) {

                    String updateUrl = json.getString("html_url");
                    ((android.app.Activity) context).runOnUiThread(() -> {
                        new AlertDialog.Builder(context)
                                .setTitle("update available")
                                .setPositiveButton("Download", (dialog, which) -> {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                                    context.startActivity(intent);
                                }).setNegativeButton("Later", null).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //честно, не знаю, как эта херня на деле работает, но она вроде должна работать. Хз.
    //если не работает, то TODO: сделать эту хрень работающей
}
