package com.example.quiz_app_submit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnFinanceQuiz, btnEconomyQuiz, btnPublicQuiz, btnWrongNote;
    private TextView tvQuizResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFinanceQuiz = findViewById(R.id.btnFinanceQuiz);
        btnEconomyQuiz = findViewById(R.id.btnEconomyQuiz);
        btnPublicQuiz = findViewById(R.id.btnPublicQuiz);
        btnWrongNote = findViewById(R.id.btnWrongNote);
        tvQuizResults = findViewById(R.id.tvQuizResults);
        Button btnHelp = findViewById(R.id.btnHelp); // 도움말 버튼

        btnFinanceQuiz.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuizFinanceActivity.class)));
        btnEconomyQuiz.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuizEconomyActivity.class)));
        btnPublicQuiz.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuizPublicActivity.class)));
        btnWrongNote.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WrongNoteActivity.class)));

        // 도움말 버튼 클릭 리스너
        btnHelp.setOnClickListener(v -> showHelpDialog());

        // 이전 퀴즈 결과 불러오기
        loadQuizResults();

        // Intent로 넘어온 퀴즈 결과 처리
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("score") && intent.hasExtra("timeSpent") && intent.hasExtra("category")) {
            int score = intent.getIntExtra("score", 0);
            long timeSpent = intent.getLongExtra("timeSpent", 0);
            String category = intent.getStringExtra("category");

            saveQuizResults(category, score, timeSpent);
            loadQuizResults();
        }
    }

    // 도움말 팝업창 띄우기
    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("도움말")
                .setMessage("퀴즈의 정답을 맞추면 1.5초 뒤 자동으로 다음 페이지로 넘어갑니다.\n\n" +
                        "100점 만점이며, 틀릴 때마다 10점이 감점됩니다.\n\n" +
                        "힌트는 각 문제 별로 최대 3번 사용 가능하며, 사용 시 5점이 감점됩니다.")
                .setCancelable(true)
                .setPositiveButton("확인", (dialog, which) -> dialog.dismiss()) // 확인 버튼 추가
                .show();
    }

    // SharedPreferences에서 퀴즈 결과 불러오기
    private void loadQuizResults() {
        SharedPreferences sharedPref = getSharedPreferences("QuizResults", MODE_PRIVATE);
        String quizResults = sharedPref.getString("quiz_results", "최근 퀴즈 결과가 없습니다.");
        tvQuizResults.setText(quizResults);
    }

    // SharedPreferences에 퀴즈 결과 저장
    private void saveQuizResults(String category, int score, long timeSpent) {
        SharedPreferences sharedPref = getSharedPreferences("QuizResults", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String previousResults = sharedPref.getString("quiz_results", "");
        String newResult = "카테고리: " + category + "\n점수: " + score + "점\n소요 시간: " + timeSpent + "초\n\n";

        editor.putString("quiz_results", newResult + previousResults);
        editor.apply();
    }
}
