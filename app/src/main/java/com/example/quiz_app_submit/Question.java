package com.example.quiz_app_submit;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {

    private String questionText;  // 문제 텍스트
    private String answer;        // 정답
    private List<String> options; // 선택지 리스트

    public Question(String questionText, String answer, List<String> options) {
        this.questionText = questionText;
        this.answer = answer;
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    // 선택지를 섞어서 반환
    public List<String> getShuffledOptions() {
        Collections.shuffle(options);  // 선택지 섞기
        return options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return questionText.equals(question.questionText) && answer.equals(question.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionText, answer);
    }
}

