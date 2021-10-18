package com.nakibul.myquizapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartingScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREF = "sharedfres";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private int highScore;
    private TextView highScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        highScoreText = findViewById(R.id.textview_highscore);
        loadHighscore();
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }

    private void startQuiz() {
        startActivityForResult(new Intent(StartingScreenActivity.this,QuizActivity.class),REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ)
        {
            if(resultCode == RESULT_OK)
            {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                if(score>highScore)
                {
                    upgradeHighscore(score);
                }
            }
        }
    }

    private void loadHighscore()
    {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        highScore = prefs.getInt(KEY_HIGHSCORE,0);
        highScoreText.setText("Highscore: "+highScore);
    }
    private void upgradeHighscore(int score) {
        highScore = score;
        highScoreText.setText("Highscore: "+highScore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE,highScore);
        editor.apply();
    }
}