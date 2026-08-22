package com.example.quiz_app_submit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

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

        List<Question> receivedReviewQuestions =
                (List<Question>) getIntent().getSerializableExtra("reviewQuestions");
        if (receivedReviewQuestions == null) {
            // 이전 화면 전달 방식과 호환
            receivedReviewQuestions =
                    (List<Question>) getIntent().getSerializableExtra("wrongQuestions");
        }
        final List<Question> reviewQuestions = receivedReviewQuestions == null
                ? new ArrayList<>()
                : receivedReviewQuestions;
        String category = getIntent().getStringExtra("category");

        if (!reviewQuestions.isEmpty()) {
            for (Question q : reviewQuestions) {
                Log.d("ResultActivity", "복습 문제: " + q.getQuestionText());
            }
        } else {
            Log.d("ResultActivity", "복습 문제 없음");
        }

        // 점수와 소요시간 가져오기
        int score = getIntent().getIntExtra("score", 0);
        long timeSpent = getIntent().getLongExtra("timeSpent", 0);

        // 결과 텍스트 설정
        tvResult.setText("카테고리: " + toCategoryLabel(category)
                + "\n점수: " + score + "점\n소요 시간: " + timeSpent + "초");

        // 복습하기 버튼 클릭 리스너
        btnReview.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(this, QuizReviewActivity.class);
            reviewIntent.putExtra(
                    "reviewQuestions",
                    new ArrayList<>(reviewQuestions));
            startActivity(reviewIntent);
        });

        // 홈 화면으로 돌아가기 버튼 클릭 리스너
        btnHome.setOnClickListener(v -> returnToHome(score, timeSpent, category));

        // 시스템 뒤로가기도 홈 버튼과 동일하게 처리하여 결과 저장과 백스택 정리 보장
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnToHome(score, timeSpent, category);
            }
        });
    }

    private void returnToHome(int score, long timeSpent, String category) {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("timeSpent", timeSpent);
        intent.putExtra("category", category);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnHome.setVisibility(View.VISIBLE);
        btnHome.setEnabled(true);
    }

    private String toCategoryLabel(String category) {
        if ("finance".equalsIgnoreCase(category)) return "금융";
        if ("public".equalsIgnoreCase(category)) return "공공";
        return "경제";
    }

}
