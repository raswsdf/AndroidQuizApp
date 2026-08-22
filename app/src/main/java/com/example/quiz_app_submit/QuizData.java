package com.example.quiz_app_submit;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuizData {
    private static final String TAG = "QuizData";
    private static final String DATA_FILE_NAME = "quiz_data.json";
    private static final Pattern PARENTHESIS_CONTENT =
            Pattern.compile("[（(]([^）)]+)[）)]");
    private static final String ANSWER_MASK = "________";
    private static final String IMMEDIATE_PARENTHESIS = "\\s*[（(][^）)]*[）)]";

    private final String category;
    private final List<Question> questions;

    public QuizData(Context context, String category) {
        this.category = category;
        questions = new ArrayList<>();
        loadQuestions(context.getApplicationContext());
    }

    private void loadQuestions(Context context) {
        try (InputStream inputStream = context.getAssets().open(DATA_FILE_NAME);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            StringBuilder jsonBuilder = new StringBuilder();
            char[] buffer = new char[8192];
            int readCount;
            while ((readCount = reader.read(buffer)) != -1) {
                jsonBuilder.append(buffer, 0, readCount);
            }

            JSONObject root = new JSONObject(jsonBuilder.toString());
            JSONArray categoryQuestions = root.optJSONArray(category);
            if (categoryQuestions == null) {
                Log.e(TAG, "지원하지 않는 카테고리입니다: " + category);
                return;
            }

            int maskedExposedAnswers = 0;
            for (int i = 0; i < categoryQuestions.length(); i++) {
                JSONObject item = categoryQuestions.getJSONObject(i);
                String questionText = item.optString("questionText", "").trim();
                String answer = item.optString("answer", "").trim();

                if (questionText.isEmpty() || answer.isEmpty()) {
                    continue;
                }

                String maskedQuestionText = maskAnswerInQuestion(questionText, answer);
                if (!questionText.equals(maskedQuestionText)) {
                    maskedExposedAnswers++;
                }
                questions.add(createQuestion(questionText, maskedQuestionText, answer));
            }
            Log.d(TAG, category + " 정답 노출 문항 마스킹: " + maskedExposedAnswers);
        } catch (IOException | JSONException e) {
            Log.e(TAG, "퀴즈 데이터를 불러오지 못했습니다.", e);
        }
    }

    public static String maskAnswerInQuestion(String questionText, String answer) {
        if (questionText == null || answer == null
                || questionText.isEmpty() || answer.trim().isEmpty()) {
            return questionText;
        }

        List<String> answerCandidates = new ArrayList<>(buildAnswerCandidates(answer));
        answerCandidates.sort((left, right) -> Integer.compare(right.length(), left.length()));

        String maskedQuestion = questionText;
        for (String candidate : answerCandidates) {
            String trimmedCandidate = candidate.trim();
            if (trimmedCandidate.length() < 2) {
                continue;
            }

            String candidatePattern = buildFlexibleLiteralPattern(trimmedCandidate);
            maskedQuestion = maskedQuestion.replaceAll(
                    "(?iu)" + candidatePattern + "(?:" + IMMEDIATE_PARENTHESIS + ")?",
                    ANSWER_MASK
            );
        }
        return maskedQuestion;
    }

    private static Set<String> buildAnswerCandidates(String answer) {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(answer.trim());

        Matcher matcher = PARENTHESIS_CONTENT.matcher(answer);
        while (matcher.find()) {
            Collections.addAll(candidates, matcher.group(1).split("[,/·:：]"));
        }

        String outsideParentheses = PARENTHESIS_CONTENT.matcher(answer).replaceAll(" ");
        Collections.addAll(candidates, outsideParentheses.split("[/·]"));
        return candidates;
    }

    private static String buildFlexibleLiteralPattern(String candidate) {
        String compactCandidate = candidate.replaceAll("\\s+", "");
        StringBuilder pattern = new StringBuilder();
        int offset = 0;
        while (offset < compactCandidate.length()) {
            if (pattern.length() > 0) {
                pattern.append("\\s*");
            }
            int codePoint = compactCandidate.codePointAt(offset);
            pattern.append(Pattern.quote(new String(Character.toChars(codePoint))));
            offset += Character.charCount(codePoint);
        }
        return pattern.toString();
    }

    private Question createQuestion(
            String originalQuestionText,
            String maskedQuestionText,
            String answer
    ) {
        Question question = new Question(maskedQuestionText, answer, new ArrayList<>());
        question.setOriginalQuestionText(originalQuestionText);
        return question;
    }

    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }

    public List<String> getRandomWrongAnswer(Question currentQuestion) {
        String correctAnswer = currentQuestion.getAnswer();

        Set<String> uniqueWrongAnswers = new LinkedHashSet<>();
        for (Question question : questions) {
            String candidate = question.getAnswer();
            if (!correctAnswer.equals(candidate)) {
                uniqueWrongAnswers.add(candidate);
            }
        }

        if (uniqueWrongAnswers.size() < 3) {
            throw new IllegalStateException("오답 선택지를 만들 데이터가 부족합니다: " + category);
        }

        List<String> wrongAnswers = new ArrayList<>(uniqueWrongAnswers);
        Collections.shuffle(wrongAnswers);

        List<String> options = new ArrayList<>(wrongAnswers.subList(0, 3));
        options.add(correctAnswer);
        Collections.shuffle(options);
        return options;
    }
}
