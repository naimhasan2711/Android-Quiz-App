package com.nakibul.myquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    public static final long COUNTDOWN_IN_MILLIS = 30000;
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private List<Question> questionList;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private long backPressedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        textViewQuestion = findViewById(R.id.textview_question);
        textViewScore = findViewById(R.id.textview_score);
        textViewQuestionCount = findViewById(R.id.textview_question_count);
        textViewCountDown = findViewById(R.id.textview_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.button_1);
        rb2 = findViewById(R.id.button_2);
        rb3 = findViewById(R.id.button_3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();


        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();

        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered)
                {
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked())
                    {
                        checkAnswer();
                    }else{
                        Toast.makeText(QuizActivity.this, "Please, Select an answer.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showNextQuestion();
                }
            }


        });
    }

    private void showNextQuestion() {

        rb1.setTextColor(Color.WHITE);
        rb2.setTextColor(Color.WHITE);
        rb3.setTextColor(Color.WHITE);
        rbGroup.clearCheck();

        if(questionCounter < questionCountTotal)
        {
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            textViewQuestionCount.setText("Question: "+questionCounter+"/"+questionCountTotal);
            answered=false;
            buttonConfirmNext.setText("confirm");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else{
            finishQuiz();
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    private void startCountDown()
    {
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer();

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered=true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answer = rbGroup.indexOfChild(rbSelected)+1;

        if(answer == currentQuestion.getAnswer())
        {
            score++;
            textViewScore.setText("Score "+score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswer())
        {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 1 is correct.");
                break;

            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 2 is correct.");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 3 is correct.");
                break;
        }

        if(questionCounter < questionCountTotal)
        {
            buttonConfirmNext.setText("Next");
        }else{
            buttonConfirmNext.setText("Finish");
        }
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime+2000 > System.currentTimeMillis())
        {
            finishQuiz();
        }else{
            Toast.makeText(QuizActivity.this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null)
        {
            countDownTimer.cancel();
        }
    }
}