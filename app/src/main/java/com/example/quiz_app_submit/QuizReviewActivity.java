package com.example.quiz_app_submit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuizReviewActivity extends AppCompatActivity {

    private TextView tvReviewQuestion, tvReviewProgress, tvReviewAnswer;
    private Button btnNextPage;
    private List<Question> wrongQuestions;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_review);

        tvReviewQuestion = findViewById(R.id.tvReviewQuestion);
        tvReviewProgress = findViewById(R.id.tvReviewProgress);
        tvReviewAnswer = findViewById(R.id.tvReviewAnswer);
        btnNextPage = findViewById(R.id.btnNextPage);

        // 틀린 문제 리스트 받기
        wrongQuestions = (List<Question>) getIntent().getSerializableExtra("wrongQuestions");

        if (wrongQuestions == null || wrongQuestions.isEmpty()) {
            Toast.makeText(this, "복습할 문제가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            displayQuestion();
            setNextPageClickListener();
        }
    }

    private void displayQuestion() {
        if (wrongQuestions != null && !wrongQuestions.isEmpty()) {
            if (currentQuestionIndex < wrongQuestions.size()) {
                Question currentQuestion = wrongQuestions.get(currentQuestionIndex);

                tvReviewQuestion.setText("문제: " + currentQuestion.getQuestionText());
                tvReviewProgress.setText((currentQuestionIndex + 1) + "/" + wrongQuestions.size());
                String correctAnswer = currentQuestion.getAnswer();
                tvReviewAnswer.setText("정답: " + correctAnswer);
                btnNextPage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "복습이 끝났습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // 복습할 문제가 없을 경우
            Toast.makeText(this, "복습할 문제가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setNextPageClickListener() {
        btnNextPage.setOnClickListener(v -> {
            currentQuestionIndex++;
            displayQuestion();
        });
    }

}
