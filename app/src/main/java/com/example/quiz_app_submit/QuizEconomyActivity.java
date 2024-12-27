package com.example.quiz_app_submit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizEconomyActivity extends AppCompatActivity {

    private TextView tvEconomyQuestion, tvEconomyScore;
    private TextView tvEconomyProgress;
    private Button btnEconomyOp1, btnEconomyOp2, btnEconomyOp3, btnEconomyOp4, btnEconomyHint, btnNextPage;
    private QuizData quizData;
    private int currentQuestionIndex = 0;
    private int score = 100;
    private List<Question> wrongQuestions = new ArrayList<>(); // 틀린 문제를 저장할 리스트
    private List<Question> shuffledQuestions = new ArrayList<>(); // 섞인 문제를 저장할 리스트

    private long quizStartTime; // 퀴즈 시작 시간 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_economy);

        tvEconomyQuestion = findViewById(R.id.tvEconomyQuestion);
        tvEconomyScore = findViewById(R.id.tvEconomyScore);
        tvEconomyProgress = findViewById(R.id.tvEconomyProgress);
        updateProgressText();

        btnEconomyOp1 = findViewById(R.id.btnEconomyOp1);
        btnEconomyOp2 = findViewById(R.id.btnEconomyOp2);
        btnEconomyOp3 = findViewById(R.id.btnEconomyOp3);
        btnEconomyOp4 = findViewById(R.id.btnEconomyOp4);
        btnEconomyHint = findViewById(R.id.btnEconomyHint);
        btnNextPage = findViewById(R.id.btnNextPage);

        quizData = new QuizData("economy");
        shuffleQuestions();
        quizStartTime = System.currentTimeMillis(); // 퀴즈 시작 시간 저장

        displayQuestion();

        setOptionClickListeners();
        setHintClickListener();
        setNextPageClickListener();
    }

    private void updateProgressText() {
        String progressText = "진행 상황: " + (currentQuestionIndex + 1) + "/5";
        tvEconomyProgress.setText(progressText);
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
            tvEconomyQuestion.setText(question.getQuestionText());

            List<String> options = quizData.getRandomWrongAnswer(currentQuestionIndex);

            btnEconomyOp1.setText(options.get(0));
            btnEconomyOp2.setText(options.get(1));
            btnEconomyOp3.setText(options.get(2));
            btnEconomyOp4.setText(options.get(3));

            enableOptionButtons();

            btnNextPage.setVisibility(View.GONE);
        } else {
            finishQuiz();
        }
    }

    private void setOptionClickListeners() {
        btnEconomyOp1.setOnClickListener(v -> checkAnswer(btnEconomyOp1.getText().toString(), btnEconomyOp1));
        btnEconomyOp2.setOnClickListener(v -> checkAnswer(btnEconomyOp2.getText().toString(), btnEconomyOp2));
        btnEconomyOp3.setOnClickListener(v -> checkAnswer(btnEconomyOp3.getText().toString(), btnEconomyOp3));
        btnEconomyOp4.setOnClickListener(v -> checkAnswer(btnEconomyOp4.getText().toString(), btnEconomyOp4));
    }

    private void setHintClickListener() {
        btnEconomyHint.setOnClickListener(v -> {
            int remainingOptions = countEnabledOptions();

            if (remainingOptions > 1) { // 마지막 선지
                score -= 5;
                tvEconomyScore.setText("점수: " + Math.max(score, 0));
                disableOneIncorrectOption();
            } else {
                Toast.makeText(this, "힌트를 더 이상 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int countEnabledOptions() {
        int count = 0;
        for (Button option : new Button[]{btnEconomyOp1, btnEconomyOp2, btnEconomyOp3, btnEconomyOp4}) {
            if (option.isEnabled()) {
                count++;
            }
        }
        return count;
    }

    private void disableOneIncorrectOption() {
        Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);
        List<Button> options = new ArrayList<>();
        options.add(btnEconomyOp1);
        options.add(btnEconomyOp2);
        options.add(btnEconomyOp3);
        options.add(btnEconomyOp4);

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
        for (Button option : new Button[]{btnEconomyOp1, btnEconomyOp2, btnEconomyOp3, btnEconomyOp4}) {
            option.setEnabled(true);
            option.setBackgroundColor(Color.parseColor("#8A73C6")); // 연한 보라
        }
        btnEconomyHint.setBackgroundColor(Color.parseColor("#64B5B4")); // 옅은 청록
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
            tvEconomyScore.setText("점수: " + score);

            Toast.makeText(this, "틀렸습니다. 10점이 감점되었습니다.", Toast.LENGTH_SHORT).show();

            // 중복 체크 후 틀린 문제 추가
            if (!wrongQuestions.contains(currentQuestion)) {
                wrongQuestions.add(currentQuestion);
            }

            // 다음 버튼 표시
            btnNextPage.setVisibility(View.VISIBLE);
        }
    }


    private void disableAllOptionButtons() {
        for (Button option : new Button[]{btnEconomyOp1, btnEconomyOp2, btnEconomyOp3, btnEconomyOp4}) {
            option.setEnabled(false);
            option.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void finishQuiz() {
        int finalScore = Math.max(score, 0);
        long quizEndTime = System.currentTimeMillis();
        long timeSpent = (quizEndTime - quizStartTime) / 1000;

        SharedPreferencesManager.getInstance(this).saveWrongQuestions("Economy", wrongQuestions);

        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("category", "economy");
        resultIntent.putExtra("score", finalScore);
        resultIntent.putExtra("timeSpent", timeSpent);
        // 틀린 문제 리스트 전달
        resultIntent.putExtra("wrongQuestions", new ArrayList<>(wrongQuestions));

        startActivity(resultIntent);

        finish();
    }
}
