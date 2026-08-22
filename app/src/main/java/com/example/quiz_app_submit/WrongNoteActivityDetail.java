package com.example.quiz_app_submit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class WrongNoteActivityDetail extends AppCompatActivity {

    private TextView tvCategoryTitle;
    private LinearLayout llWrongNotes;
    private Button btnStartQuiz;

    private String categoryLower;   // economy | finance | public
    private String categoryPrefKey; // Economy | Finance | Public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_note_detail);

        // 1) View 바인딩
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        llWrongNotes = findViewById(R.id.llWrongNotes);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        // 2) 카테고리 수신 (호환: category(lower) 또는 CATEGORY(prefKey) 둘 다 받음)
        categoryLower = getIntent().getStringExtra("category");  // economy/finance/public
        String categoryFromQuizKey = getIntent().getStringExtra("CATEGORY"); // Economy/Finance/Public

        if (categoryFromQuizKey != null) {
            // CATEGORY가 들어오면 그것을 우선 사용
            categoryPrefKey = categoryFromQuizKey;
            categoryLower = toLowerKey(categoryPrefKey);
        } else {
            // category(lower)가 들어오면 prefKey로 변환
            if (categoryLower == null) categoryLower = "economy"; // 방어 코드
            categoryPrefKey = toPrefKey(categoryLower);
        }

        // 3) 제목 표기
        tvCategoryTitle.setText(makeTitle(categoryPrefKey));

        // 4) 복습 퀴즈 버튼
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(WrongNoteActivityDetail.this, QuizActivity.class);
            // QuizActivity는 SharedPreferences 키("Economy/Finance/Public")를 CATEGORY로 받음
            intent.putExtra("CATEGORY", categoryPrefKey);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 복습 퀴즈에서 삭제된 오답을 상세 화면 복귀 즉시 반영
        renderWrongQuestions(categoryPrefKey);
    }

    private void renderWrongQuestions(String prefKey) {
        llWrongNotes.removeAllViews();

        List<Question> wrongQuestions =
                SharedPreferencesManager.getInstance(this).getWrongQuestions(prefKey);

        if (wrongQuestions != null && !wrongQuestions.isEmpty()) {
            for (Question question : wrongQuestions) {
                TextView tvQuestionAnswer = new TextView(this);
                tvQuestionAnswer.setText(
                        "문제: " + question.getOriginalQuestionText() + "\n" +
                                "답: " + question.getAnswer()
                );
                tvQuestionAnswer.setTextSize(16);
                tvQuestionAnswer.setTextColor(getColor(R.color.black));
                tvQuestionAnswer.setLineSpacing(dpToPx(3), 1f);
                tvQuestionAnswer.setPadding(
                        dpToPx(16), dpToPx(14), dpToPx(16), dpToPx(14));
                tvQuestionAnswer.setBackgroundResource(
                        R.drawable.wrong_note_item_background);
                tvQuestionAnswer.setElevation(dpToPx(1));

                LinearLayout.LayoutParams itemLayoutParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                itemLayoutParams.setMargins(
                        dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(12));
                llWrongNotes.addView(tvQuestionAnswer, itemLayoutParams);
            }
            btnStartQuiz.setVisibility(View.VISIBLE);
        } else {
            TextView tvNoQuestions = new TextView(this);
            tvNoQuestions.setText("복습할 문제가 없습니다.");
            tvNoQuestions.setTextSize(18);
            llWrongNotes.addView(tvNoQuestions);

            // 복습할 문제가 없으면 버튼 숨김
            btnStartQuiz.setVisibility(View.GONE);
        }
    }

    // lower(economy/finance/public) -> prefKey(Economy/Finance/Public)
    private String toPrefKey(String lower) {
        switch (lower) {
            case "finance": return "Finance";
            case "public":  return "Public";
            case "economy":
            default:        return "Economy";
        }
    }

    // prefKey(Economy/Finance/Public) -> lower(economy/finance/public)
    private String toLowerKey(String prefKey) {
        switch (prefKey) {
            case "Finance": return "finance";
            case "Public":  return "public";
            case "Economy":
            default:        return "economy";
        }
    }

    private String makeTitle(String prefKey) {
        switch (prefKey) {
            case "Finance": return "금융 오답노트";
            case "Public":  return "공공 오답노트";
            case "Economy":
            default:        return "경제 오답노트";
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
