package com.example.quiz_app_submit;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivityDetail extends AppCompatActivity {

    private static final long ANSWER_TRANSITION_DELAY_MS = 3500L;

    // 공용 레이아웃(id 통일) 기반 View
    private TextView tvQuestion, tvScore, tvProgress;
    private Button btnOp1, btnOp2, btnOp3, btnOp4, btnHint, btnNextPage;
    private ScrollView quizScrollView;

    // 퀴즈 상태
    private QuizData quizData;
    private int currentQuestionIndex = 0;
    private int score = 100;
    private final List<Question> wrongQuestions = new ArrayList<>();
    private final List<Question> reviewQuestions = new ArrayList<>();
    private List<Question> shuffledQuestions = new ArrayList<>();
    private long quizStartTime;

    // 카테고리(문제 데이터용/저장용)
    private String categoryLower; // "economy" | "finance" | "public"
    private String categoryPrefKey; // "Economy" | "Finance" | "Public"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        // 1) 카테고리 받기
        // - MainActivity에서 넘길 값: "economy"/"finance"/"public"
        categoryLower = getIntent().getStringExtra("category");
        if (categoryLower == null) categoryLower = "economy"; // 방어 코드 (기본값)

        categoryPrefKey = toPrefKey(categoryLower);

        // 2) View 바인딩
        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvProgress = findViewById(R.id.tvProgress);

        btnOp1 = findViewById(R.id.btnOp1);
        btnOp2 = findViewById(R.id.btnOp2);
        btnOp3 = findViewById(R.id.btnOp3);
        btnOp4 = findViewById(R.id.btnOp4);
        btnHint = findViewById(R.id.btnHint);
        btnNextPage = findViewById(R.id.btnNextPage);
        quizScrollView = findViewById(R.id.quizScrollView);

        // 3) 데이터 준비
        quizData = new QuizData(this, categoryLower);
        if (quizData.getAllQuestions().size() < 5) {
            Toast.makeText(this, "퀴즈 데이터를 불러오지 못했습니다.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        shuffleQuestions();
        quizStartTime = System.currentTimeMillis();

        // 4) 첫 문제 표시 + 리스너
        displayQuestion();
        setOptionClickListeners();
        setHintClickListener();
        setNextPageClickListener();
    }

    // 기존 SharedPreferencesManager 키와 호환되게 유지
    private String toPrefKey(String lower) {
        switch (lower) {
            case "finance": return "Finance";
            case "public":  return "Public";
            case "economy":
            default:        return "Economy";
        }
    }

    private void updateProgressText() {
        String progressText = "진행 상황: " + (currentQuestionIndex + 1) + "/" + shuffledQuestions.size();
        tvProgress.setText(progressText);
    }

    private void shuffleQuestions() {
        List<Question> allQuestions = quizData.getAllQuestions();
        Collections.shuffle(allQuestions);

        // 안전장치: 문제 수가 5개 미만인 경우도 대비
        int count = Math.min(5, allQuestions.size());
        shuffledQuestions = new ArrayList<>(allQuestions.subList(0, count));
    }

    private void displayQuestion() {
        if (currentQuestionIndex < shuffledQuestions.size()) {
            updateProgressText();

            Question question = shuffledQuestions.get(currentQuestionIndex);
            tvQuestion.setText(question.getQuestionText());

            List<String> options = quizData.getRandomWrongAnswer(question);

            btnOp1.setText(options.get(0));
            btnOp2.setText(options.get(1));
            btnOp3.setText(options.get(2));
            btnOp4.setText(options.get(3));

            enableOptionButtons();
            btnNextPage.setVisibility(View.GONE);
            quizScrollView.post(() -> quizScrollView.fullScroll(View.FOCUS_UP));
        } else {
            finishQuiz();
        }
    }

    private void setOptionClickListeners() {
        btnOp1.setOnClickListener(v -> checkAnswer(btnOp1.getText().toString(), btnOp1));
        btnOp2.setOnClickListener(v -> checkAnswer(btnOp2.getText().toString(), btnOp2));
        btnOp3.setOnClickListener(v -> checkAnswer(btnOp3.getText().toString(), btnOp3));
        btnOp4.setOnClickListener(v -> checkAnswer(btnOp4.getText().toString(), btnOp4));
    }

    private void setHintClickListener() {
        btnHint.setOnClickListener(v -> {
            int remainingOptions = countEnabledOptions();

            // 마지막 선지 1개 남았으면 힌트 사용 불가
            if (remainingOptions > 1) {
                Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);
                score -= 5;
                tvScore.setText("점수: " + Math.max(score, 0));
                disableOneIncorrectOption();
                addReviewQuestion(currentQuestion);
                updateHintButtonState();
                showNextPageButton();
            } else {
                setHintButtonEnabled(false);
                Toast.makeText(this, "힌트를 더 이상 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int countEnabledOptions() {
        int count = 0;
        for (Button option : new Button[]{btnOp1, btnOp2, btnOp3, btnOp4}) {
            if (option.isEnabled()) count++;
        }
        return count;
    }

    private void updateHintButtonState() {
        setHintButtonEnabled(countEnabledOptions() > 1);
    }

    private void setHintButtonEnabled(boolean enabled) {
        btnHint.setEnabled(enabled);
        btnHint.setBackgroundTintList(ColorStateList.valueOf(getColor(
                enabled ? R.color.color_main : R.color.color_button_disabled)));
        btnHint.setTextColor(getColor(R.color.white));
    }

    private void disableOneIncorrectOption() {
        Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);

        for (Button option : new Button[]{btnOp1, btnOp2, btnOp3, btnOp4}) {
            // 정답이 아닌 버튼 중, 아직 활성화된 것 하나만 제거
            if (!option.getText().toString().equals(currentQuestion.getAnswer()) && option.isEnabled()) {
                option.setEnabled(false);
                option.setBackgroundTintList(
                        ColorStateList.valueOf(getColor(R.color.color_button_disabled)));
                option.setTextColor(getColor(R.color.white));
                break;
            }
        }
    }

    private void setNextPageClickListener() {
        btnNextPage.setOnClickListener(v -> {
            Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);
            if (!wrongQuestions.contains(currentQuestion)) {
                wrongQuestions.add(currentQuestion);
            }
            addReviewQuestion(currentQuestion);
            currentQuestionIndex++;
            displayQuestion();
        });
    }

    private void showNextPageButton() {
        btnNextPage.setEnabled(true);
        btnNextPage.setVisibility(View.VISIBLE);
        quizScrollView.post(() -> quizScrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void addReviewQuestion(Question question) {
        if (!reviewQuestions.contains(question)) {
            reviewQuestions.add(question);
        }
    }

    private void enableOptionButtons() {
        for (Button option : new Button[]{btnOp1, btnOp2, btnOp3, btnOp4}) {
            option.setEnabled(true);
            option.setBackgroundTintList(
                    ColorStateList.valueOf(getColor(R.color.color_quiz_option)));
            option.setTextColor(getColor(R.color.white));
        }
        setHintButtonEnabled(true);
        btnNextPage.setEnabled(true);
        btnNextPage.setBackgroundTintList(getColorStateList(R.color.button_background_tint));
        btnNextPage.setTextColor(getColor(R.color.white));
        tvScore.setText("점수: " + Math.max(score, 0));
    }

    private void checkAnswer(String selectedAnswer, Button selectedButton) {
        Question currentQuestion = shuffledQuestions.get(currentQuestionIndex);

        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            Toast.makeText(this, "정답입니다!", Toast.LENGTH_SHORT).show();
            tvQuestion.setText(currentQuestion.getOriginalQuestionText());
            btnNextPage.setEnabled(false);
            disableAllOptionButtons();

            new Handler().postDelayed(() -> {
                currentQuestionIndex++;
                if (currentQuestionIndex < shuffledQuestions.size()) {
                    displayQuestion();
                } else {
                    finishQuiz();
                }
            }, ANSWER_TRANSITION_DELAY_MS);

        } else {
            // 틀린 경우: 선택한 버튼만 비활성화
            selectedButton.setEnabled(false);
            selectedButton.setBackgroundTintList(
                    ColorStateList.valueOf(getColor(R.color.color_button_disabled)));
            selectedButton.setTextColor(getColor(R.color.white));

            // 점수 감소
            score -= 10;
            score = Math.max(score, 0);
            tvScore.setText("점수: " + score);

            Toast.makeText(this, "틀렸습니다. 10점이 감점되었습니다.", Toast.LENGTH_SHORT).show();

            // 중복 없이 추가 (Question equals/hashCode 구현 여부에 따라 contains가 동작)
            if (!wrongQuestions.contains(currentQuestion)) {
                wrongQuestions.add(currentQuestion);
            }
            addReviewQuestion(currentQuestion);
            updateHintButtonState();

            // 다음 버튼 표시
            showNextPageButton();
        }
    }

    private void disableAllOptionButtons() {
        for (Button option : new Button[]{btnOp1, btnOp2, btnOp3, btnOp4}) {
            option.setEnabled(false);
            option.setBackgroundTintList(
                    ColorStateList.valueOf(getColor(R.color.color_button_disabled)));
            option.setTextColor(getColor(R.color.white));
        }
        setHintButtonEnabled(false);
    }

    private void finishQuiz() {
        int finalScore = Math.max(score, 0);
        long quizEndTime = System.currentTimeMillis();
        long timeSpent = (quizEndTime - quizStartTime) / 1000;

        // 기존 복습 퀴즈(QuizActivity)와 호환: "Economy/Finance/Public" 키로 저장
        SharedPreferencesManager.getInstance(this).saveWrongQuestions(categoryPrefKey, wrongQuestions);

        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("category", categoryLower); // 기존 ResultActivity 표기/저장 흐름 유지
        resultIntent.putExtra("score", finalScore);
        resultIntent.putExtra("timeSpent", timeSpent);
        resultIntent.putExtra("reviewQuestions", new ArrayList<>(reviewQuestions));
        resultIntent.putExtra("wrongQuestions", new ArrayList<>(wrongQuestions));
        startActivity(resultIntent);

        finish();
    }
}
