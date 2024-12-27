package com.example.quiz_app_submit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnHome;
    private Button btnReview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvResult = findViewById(R.id.tvResult);
        btnHome = findViewById(R.id.btnHome);
        btnReview = findViewById(R.id.btnReview);

        btnHome.setVisibility(View.INVISIBLE);
        btnHome.setEnabled(false); // 홈 버튼 비활성화

        List<Question> wrongQuestions = (List<Question>) getIntent().getSerializableExtra("wrongQuestions");
        String category = getIntent().getStringExtra("category");

        if (wrongQuestions != null && !wrongQuestions.isEmpty()) {
            for (Question q : wrongQuestions) {
                Log.d("QuizEconomyActivity", "틀린 문제: " + q.getQuestionText());
            }
        } else {
            Log.d("ResultActivity", "틀린 문제 없음");
        }

        // 점수와 소요시간 가져오기
        int score = getIntent().getIntExtra("score", 0);
        long timeSpent = getIntent().getLongExtra("timeSpent", 0);

        // 결과 텍스트 설정
        tvResult.setText("카테고리: " + category + "\n점수: " + score + "점\n소요 시간: " + timeSpent + "초");

        // 복습하기 버튼 클릭 리스너
        btnReview.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(this, QuizReviewActivity.class);
            reviewIntent.putExtra("wrongQuestions", new ArrayList<>(wrongQuestions)); // 틀린 문제 전달
            startActivity(reviewIntent);
        });

        // 홈 화면으로 돌아가기 버튼 클릭 리스너
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.putExtra("score", score); // 점수 전달
            intent.putExtra("timeSpent", timeSpent); // 소요 시간 전달
            intent.putExtra("category", category); // 카테고리 전달
            startActivity(intent);
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        btnHome.setVisibility(View.VISIBLE);
        btnHome.setEnabled(true);
    }

}
