package com.example.quiz_app_submit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private LinearLayout llOptions;
    private Button btnNext, btnStop;
    private TextView tvRemainingQuestions;
    private List<Question> wrongQuestions; // 틀린 문제 리스트
    private Question currentQuestion; // 현재 문제
    private List<String> currentOptions; // 현재 보기를 저장
    private Random random;
    private String currentCategory; // 현재 카테고리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        llOptions = findViewById(R.id.llOptions);
        btnNext = findViewById(R.id.btnNext);
        btnStop = findViewById(R.id.btnStop);
        tvRemainingQuestions = findViewById(R.id.tvRemainingQuestions);
        random = new Random();

        currentCategory = getIntent().getStringExtra("CATEGORY");

        wrongQuestions = new ArrayList<>(SharedPreferencesManager.getInstance(this).getWrongQuestions(currentCategory));
        if (wrongQuestions.isEmpty()) {
            Toast.makeText(this, "복습할 문제가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showNextQuestion();

        btnNext.setOnClickListener(v -> showNextQuestion());
        btnStop.setOnClickListener(v -> {
            Intent intent = new Intent(QuizActivity.this, WrongNoteActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showNextQuestion() {
        if (wrongQuestions.isEmpty()) {
            Toast.makeText(this, "모든 문제를 복습했습니다!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 랜덤으로 다음 문제 선택
        currentQuestion = wrongQuestions.get(random.nextInt(wrongQuestions.size()));
        currentOptions = generateOptions(currentQuestion);

        tvQuestion.setText("문제: " + currentQuestion.getQuestionText());
        llOptions.removeAllViews();

        for (String option : currentOptions) {
            Button optionButton = new Button(this);
            optionButton.setText(option);
            optionButton.setOnClickListener(v -> handleAnswer(option));
            llOptions.addView(optionButton);
        }

        updateRemainingQuestionsCount();
    }

    // 보기 생성 (정답 + 랜덤 오답 3개)
    private List<String> generateOptions(Question question) {
        List<String> options = new ArrayList<>();
        options.add(question.getAnswer());
        List<Question> tempList = new ArrayList<>(wrongQuestions);
        tempList.remove(question); // 현재 문제는 제외

        int numOptions = Math.min(4, wrongQuestions.size());

        while (options.size() < numOptions && !tempList.isEmpty()) {
            String randomWrongAnswer = tempList.remove(random.nextInt(tempList.size())).getAnswer();
            if (!options.contains(randomWrongAnswer)) {
                options.add(randomWrongAnswer); // 중복 방지
            }
        }

        // 마지막 문제에서 선지에 "수고하셨습니다" 추가
        if (wrongQuestions.size() == 1) {
            options.add("수고하셨습니다.");
        }

        Collections.shuffle(options);
        return options;
    }

    private void handleAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            // 정답 처리
            Toast.makeText(this, "정답입니다! 해당 문제는 삭제됩니다.", Toast.LENGTH_SHORT).show();
            wrongQuestions.remove(currentQuestion);
            updateWrongQuestionsInPreferences();
            Log.d("QuizActivity", "지운 후, wrongQuestions size: " + wrongQuestions.size());
            showNextQuestion(); // 다음 문제로 이동
        } else {
            Toast.makeText(this, "틀렸습니다. 다시 복습하세요.", Toast.LENGTH_SHORT).show();
            showNextQuestion(); // 다음 문제로 이동 (삭제 안 함)
        }
    }

    private void updateWrongQuestionsInPreferences() {
        SharedPreferencesManager.getInstance(this).addWrongQuestions(currentCategory, wrongQuestions);
        Log.d("QuizActivity", "업데이트 후 wrongQuestions size: " + wrongQuestions.size());
    }

    private void updateRemainingQuestionsCount() {
        int remainingQuestions = wrongQuestions.size();
        tvRemainingQuestions.setText("남은 문제 수: " + remainingQuestions);
    }
}
