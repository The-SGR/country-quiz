package xyz.sgrverse.countryquiz;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class ProgressManager {
    private final SharedPreferences preferences;

    public ProgressManager(Context context) {
        preferences = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE);
    }

    private String getKey(String quizType) {
        return "guessed_" + quizType;
    }

    public Set<String> getGuessed(String quizType) {
        return new HashSet<>(preferences.getStringSet(getKey(quizType), new HashSet<>()));
    }

    public boolean isGuessed(String quizType, String countryId) {
        return getGuessed(quizType).contains(countryId);
    }

    public void markGuessed(String quizType, String countryId) {
        Set<String> guessed = getGuessed(quizType);
        guessed.add(countryId);

        preferences.edit().putStringSet(getKey(quizType), guessed).apply();
    }

    public int getGuessedCount(String quizType) {
        return getGuessed(quizType).size();
    }

    public void resetQuiz(String quizType) {
        preferences.edit().remove(getKey(quizType)).apply();
    }

    public void resetAllProgress() {
        preferences.edit().clear().apply();
    }
}