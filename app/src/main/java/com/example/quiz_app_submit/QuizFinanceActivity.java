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

public class QuizFinanceActivity extends AppCompatActivity {

    private TextView tvFinanceQuestion, tvFinanceScore, tvFinanceProgress;
    private Button btnFinanceOp1, btnFinanceOp2, btnFinanceOp3, btnFinanceOp4, btnFinanceHint, btnNextPage;
    private QuizData quizData;
    private int currentQuestionIndex = 0;
    private int score = 100;
    private List<Question> wrongQuestions = new ArrayList<>();
    private List<Question> shuffledQuestions = new ArrayList<>();
    private long quizStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finance);

        tvFinanceQuestion = findViewById(R.id.tvFinanceQuestion);
        tvFinanceScore = findViewById(R.id.tvFinanceScore);
        tvFinanceProgress = findViewById(R.id.tvFinanceProgress);
        btnFinanceOp1 = findViewById(R.id.btnFinanceOp1);
        btnFinanceOp2 = findViewById(R.id.btnFinanceOp2);
        btnFinanceOp3 = findViewById(R.id.btnFinanceOp3);
        btnFinanceOp4 = findViewById(R.id.btnFinanceOp4);
        btnFinanceHint = findViewById(R.id.btnFinanceHint);
        btnNextPage = findViewById(R.id.btnNextPage);

        quizData = new QuizData("finance");
        shuffleQuestions();
        quizStartTime = System.currentTimeMillis();
        displayQuestion();

        setOptionClickListeners();
        setHintClickListener();
        setNextPageClickListener();
    }

    private void updateProgressText() {
        String progressText = "진행 상황: " + (currentQuestionIndex + 1) + "/5";
        tvFinanceProgress.setText(progressText);
    }

    private void shuffleQuestions() {
        List<Question> allQuestions = quizData.getAllQuestions();
        Collections.shuffle(allQuestions);
        shuffledQuestions = allQuestions.subList(0, 5);
    }

    private void displayQuestion() {
        if (currentQuestionIndex < 5) {
            updateProgressText();
            Question question = shuffledQuestions.get(currentQuestionIndex);
            tvFinanceQuestion.setText(question.getQuestionText());

            List<String> options = quizData.getRandomWrongAnswer(currentQuestionIndex);

            btnFinanceOp1.setText(options.get(0));
            btnFinanceOp2.setText(options.get(1));
            btnFinanceOp3.setText(options.get(2));
            btnFinanceOp4.setText(options.get(3));

            enableOptionButtons();
            btnNextPage.setVisibility(View.GONE);
        } else {
            finishQuiz();
        }
    }

    private void setOptionClickListeners() {
        btnFinanceOp1.setOnClickListener(v -> checkAnswer(btnFinanceOp1.getText().toString(), btnFinanceOp1));
        btnFinanceOp2.setOnClickListener(v -> checkAnswer(btnFinanceOp2.getText().toString(), btnFinanceOp2));
        btnFinanceOp3.setOnClickListener(v -> checkAnswer(btnFinanceOp3.getText().toString(), btnFinanceOp3));
        btnFinanceOp4.setOnClickListener(v -> checkAnswer(btnFinanceOp4.getText().toString(), btnFinanceOp4));
    }

    private void setHintClickListener() {
        btnFinanceHint.setOnClickListener(v -> {
            int remainingOptions = countEnabledOptions();
            if (remainingOptions > 1) {
                score -= 5;
                tvFinanceScore.setText("점수: " + Math.max(score, 0));
                disableOneIncorrectOption();
            } else {
                Toast.makeText(this, "힌트를 더 이상 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int countEnabledOptions() {
        int count = 0;
        for (Button option : new Button[]{btnFinanceOp1, btnFinanceOp2, btnFinanceOp3, btnFinanceOp4}) {
            if (option.isEnabled()) {
                count++;
            }
        }
        return count;
    }

    private void disableOneIncorrectOption() {
        Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);
        for (Button option : new Button[]{btnFinanceOp1, btnFinanceOp2, btnFinanceOp3, btnFinanceOp4}) {
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
        for (Button option : new Button[]{btnFinanceOp1, btnFinanceOp2, btnFinanceOp3, btnFinanceOp4}) {
            option.setEnabled(true);
            option.setBackgroundColor(Color.parseColor("#8A73C6")); // 연한 보라
        }
        btnFinanceHint.setBackgroundColor(Color.parseColor("#64B5B4")); // 옅은 청록
    }

    private void checkAnswer(String selectedAnswer, Button selectedButton) {
        Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);

        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            Toast.makeText(this, "정답입니다!", Toast.LENGTH_SHORT).show();
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
            selectedButton.setEnabled(false);
            selectedButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            score -= 10;
            score = Math.max(score, 0);
            tvFinanceScore.setText("점수: " + score);

            Toast.makeText(this, "틀렸습니다. 10점이 감점되었습니다.", Toast.LENGTH_SHORT).show();

            if (!wrongQuestions.contains(currentQuestion)) {
                wrongQuestions.add(currentQuestion);
            }
            btnNextPage.setVisibility(View.VISIBLE);
        }
    }

    private void disableAllOptionButtons() {
        for (Button option : new Button[]{btnFinanceOp1, btnFinanceOp2, btnFinanceOp3, btnFinanceOp4}) {
            option.setEnabled(false);
            option.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void finishQuiz() {
        int finalScore = Math.max(score, 0);
        long quizEndTime = System.currentTimeMillis();
        long timeSpent = (quizEndTime - quizStartTime) / 1000;

        SharedPreferencesManager.getInstance(this).saveWrongQuestions("Finance", wrongQuestions);

        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("category", "finance");
        resultIntent.putExtra("score", finalScore);
        resultIntent.putExtra("timeSpent", timeSpent);
        resultIntent.putExtra("wrongQuestions", new ArrayList<>(wrongQuestions));

        startActivity(resultIntent);
        finish();
    }
}
