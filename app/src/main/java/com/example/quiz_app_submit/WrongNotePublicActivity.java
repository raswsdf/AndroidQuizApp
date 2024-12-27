package com.example.quiz_app_submit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class WrongNotePublicActivity extends AppCompatActivity {

    private LinearLayout llWrongNotes;
    private Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_note_public);

        llWrongNotes = findViewById(R.id.llWrongNotes);  // 틀린 문제를 표시
        btnStartQuiz = findViewById(R.id.btnStartQuiz);  // 복습 퀴즈 버튼

        List<Question> wrongQuestions = SharedPreferencesManager.getInstance(this).getWrongQuestions("Public");

        if (wrongQuestions != null && !wrongQuestions.isEmpty()) {
            for (Question question : wrongQuestions) {
                TextView tvQuestionAnswer = new TextView(this);
                tvQuestionAnswer.setText(
                        "문제: " + question.getQuestionText() + "\n" +
                                "답: " + question.getAnswer()
                );
                tvQuestionAnswer.setTextSize(18);
                tvQuestionAnswer.setPadding(0, 10, 0, 10);
                llWrongNotes.addView(tvQuestionAnswer);
            }
        } else {
            TextView tvNoQuestions = new TextView(this);
            tvNoQuestions.setText("복습할 문제가 없습니다.");
            tvNoQuestions.setTextSize(18);
            llWrongNotes.addView(tvNoQuestions);
            btnStartQuiz.setVisibility(View.GONE); // 복습 문제가 없으면 버튼 숨김
        }

        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(WrongNotePublicActivity.this, QuizActivity.class);
            intent.putExtra("CATEGORY", "Public");
            startActivity(intent);
        });
    }
}
