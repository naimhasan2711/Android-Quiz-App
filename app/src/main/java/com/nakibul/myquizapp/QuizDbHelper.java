package com.nakibul.myquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.nakibul.myquizapp.QuizContract.*;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }
    private void fillQuestionsTable() {

        Question q1 = new Question("Android is developed by - ", "Apple", "Google", "Android Inc", 3);
        addQuestion(q1);
        Question q2 = new Question("What does AAPT stand for", "Android Asset Processing Tool", "Android Asset Packaging Tool", "Android Asset Providing Tool", 2);
        addQuestion(q2);
        Question q3 = new Question("Which Programming language is used for Android Application Development? ", "Java", "Python", "C", 1);
        addQuestion(q3);
        Question q4 = new Question("Android is based on which Kernal?", "Windows", "Mac", "Linux", 3);
        addQuestion(q4);
        Question q5 = new Question("ADB stands for-", "Android Debug Bridge", "Android Delete Bridge", "Android Drive Bridge", 1);
        addQuestion(q5);
        Question q6 = new Question("Which of the following is not a activity callback method in Android?", "onStart()", "onDestroy()", "onBackPressed()", 3);
        addQuestion(q6);
        Question q7 = new Question("NDK stands for-", "New Development Kit", "Native Development Kit", "Native Design Kit", 2);
        addQuestion(q7);
        Question q8 = new Question("Which of the following class in android displays information for a short period of time and disappears after some time?", "Log class", "Toast class", "None of the above", 2);
        addQuestion(q8);
        Question q9 = new Question("ANR in android stands for -", "Application Not Reacting", "Application Not Responding", "Application Not Rendering", 2);
        addQuestion(q9);
        Question q10 = new Question("All layout classes are the subclasses of -", "android.view.View", "android.widget", "None of the above", 3);
        addQuestion(q10);
    }
    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswer());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }
    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                int a = c.getColumnIndex(QuestionsTable.COLUMN_QUESTION);
                int o1 = c.getColumnIndex(QuestionsTable.COLUMN_OPTION1);
                int o2 = c.getColumnIndex(QuestionsTable.COLUMN_OPTION2);
                int o3 = c.getColumnIndex(QuestionsTable.COLUMN_OPTION3);
                int an = c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR);


                question.setQuestion(c.getString(a));
                question.setOption1(c.getString(o1));
                question.setOption2(c.getString(o2));
                question.setOption3(c.getString(o3));
                question.setAnswer(c.getInt(an));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
