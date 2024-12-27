package com.example.quiz_app_submit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizPublicActivity extends AppCompatActivity {

    private TextView tvPublicQuestion, tvPublicScore, tvPublicProgress;
    private Button btnPublicOp1, btnPublicOp2, btnPublicOp3, btnPublicOp4, btnPublicHint, btnNextPage;
    private QuizData quizData;
    private int currentQuestionIndex = 0;
    private int score = 100;
    private List<Question> wrongQuestions = new ArrayList<>(); // 틀린 문제를 저장할 리스트
    private List<Question> shuffledQuestions = new ArrayList<>(); // 섞인 문제를 저장할 리스트
    private long quizStartTime; // 퀴즈 시작 시간 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_public);

        tvPublicQuestion = findViewById(R.id.tvPublicQuestion);
        tvPublicScore = findViewById(R.id.tvPublicScore);
        tvPublicProgress = findViewById(R.id.tvPublicProgress);
        updateProgressText();

        btnPublicOp1 = findViewById(R.id.btnPublicOp1);
        btnPublicOp2 = findViewById(R.id.btnPublicOp2);
        btnPublicOp3 = findViewById(R.id.btnPublicOp3);
        btnPublicOp4 = findViewById(R.id.btnPublicOp4);
        btnPublicHint = findViewById(R.id.btnPublicHint);
        btnNextPage = findViewById(R.id.btnNextPage);

        quizData = new QuizData("public");
        shuffleQuestions();
        quizStartTime = System.currentTimeMillis(); // 퀴즈 시작 시간 저장
        displayQuestion();

        setOptionClickListeners();
        setHintClickListener();
        setNextPageClickListener();
    }

    private void updateProgressText() {
        String progressText = "진행 상황: " + (currentQuestionIndex + 1) + "/5";
        tvPublicProgress.setText(progressText);
    }

    private void shuffleQuestions() {
        List<Question> allQuestions = quizData.getAllQuestions();
        Collections.shuffle(allQuestions);
        shuffledQuestions = allQuestions.subList(0, 5);
    }

    private void displayQuestion() {
        if (currentQuestionIndex < 5) {
            updateProgressText(); // 진행 상태 업데이트
            Question question = shuffledQuestions.get(currentQuestionIndex);
            tvPublicQuestion.setText(question.getQuestionText());

            List<String> options = quizData.getRandomWrongAnswer(currentQuestionIndex);

            btnPublicOp1.setText(options.get(0));
            btnPublicOp2.setText(options.get(1));
            btnPublicOp3.setText(options.get(2));
            btnPublicOp4.setText(options.get(3));

            enableOptionButtons();

            btnNextPage.setVisibility(View.GONE);
        } else {
            finishQuiz();
        }
    }

    private void setOptionClickListeners() {
        btnPublicOp1.setOnClickListener(v -> checkAnswer(btnPublicOp1.getText().toString(), btnPublicOp1));
        btnPublicOp2.setOnClickListener(v -> checkAnswer(btnPublicOp2.getText().toString(), btnPublicOp2));
        btnPublicOp3.setOnClickListener(v -> checkAnswer(btnPublicOp3.getText().toString(), btnPublicOp3));
        btnPublicOp4.setOnClickListener(v -> checkAnswer(btnPublicOp4.getText().toString(), btnPublicOp4));
    }

    private void setHintClickListener() {
        btnPublicHint.setOnClickListener(v -> {
            int remainingOptions = countEnabledOptions();

            if (remainingOptions > 1) {
                score -= 5;
                tvPublicScore.setText("점수: " + Math.max(score, 0));
                disableOneIncorrectOption();
            } else {
                Toast.makeText(this, "힌트를 더 이상 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int countEnabledOptions() {
        int count = 0;
        for (Button option : new Button[]{btnPublicOp1, btnPublicOp2, btnPublicOp3, btnPublicOp4}) {
            if (option.isEnabled()) {
                count++;
            }
        }
        return count;
    }

    private void disableOneIncorrectOption() {
        Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);
        List<Button> options = new ArrayList<>();
        options.add(btnPublicOp1);
        options.add(btnPublicOp2);
        options.add(btnPublicOp3);
        options.add(btnPublicOp4);

        for (Button option : options) {
            if (!option.getText().toString().equals(currentQuestion.getAnswer()) && option.isEnabled()) {
                option.setEnabled(false);
                option.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                break;
            }
        }
    }

    private void setNextPageClickListener() {
        btnNextPage.setOnClickListener(v -> {
            currentQuestionIndex++;
            displayQuestion();
        });
    }

    private void enableOptionButtons() {
        for (Button option : new Button[]{btnPublicOp1, btnPublicOp2, btnPublicOp3, btnPublicOp4}) {
            option.setEnabled(true);
            option.setBackgroundColor(Color.parseColor("#8A73C6")); // 연한 보라
        }
        btnPublicHint.setBackgroundColor(Color.parseColor("#64B5B4")); // 옅은 청록
    }

    private void checkAnswer(String selectedAnswer, Button selectedButton) {
        Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);

        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            Toast.makeText(this, "정답입니다!", Toast.LENGTH_SHORT).show();

            // 모든 버튼 비활성화
            disableAllOptionButtons();

            new Handler().postDelayed(() -> {
                currentQuestionIndex++;
                if (currentQuestionIndex < 5) {
                    displayQuestion();
                } else {
                    finishQuiz();
                }
            }, 1500);
        } else {
            // 틀린 경우 선택한 버튼만 비활성화
            selectedButton.setEnabled(false);
            selectedButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            // 점수 감소
            score -= 10;
            score = Math.max(score, 0);
            tvPublicScore.setText("점수: " + score);

            Toast.makeText(this, "틀렸습니다. 10점이 감점되었습니다.", Toast.LENGTH_SHORT).show();

            if (!wrongQuestions.contains(currentQuestion)) {
                wrongQuestions.add(currentQuestion);  // 중복 없이 추가
            }

            // 다음 버튼 표시
            btnNextPage.setVisibility(View.VISIBLE);
        }
    }

    private void disableAllOptionButtons() {
        for (Button option : new Button[]{btnPublicOp1, btnPublicOp2, btnPublicOp3, btnPublicOp4}) {
            option.setEnabled(false);
            option.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void finishQuiz() {
        // 카테고리와 관련된 최종 점수와 시간을 저장
        int finalScore = Math.max(score, 0);
        long quizEndTime = System.currentTimeMillis();
        long timeSpent = (quizEndTime - quizStartTime) / 1000;

        // 틀린 문제를 SharedPreferences에 저장
        SharedPreferencesManager.getInstance(this).saveWrongQuestions("Public", wrongQuestions);

        // 결과 화면으로 이동
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("category", "public");
        resultIntent.putExtra("score", finalScore);
        resultIntent.putExtra("timeSpent", timeSpent);
        // 틀린 문제 리스트 전달
        resultIntent.putExtra("wrongQuestions", new ArrayList<>(wrongQuestions));

        startActivity(resultIntent);

        finish();
    }
}
