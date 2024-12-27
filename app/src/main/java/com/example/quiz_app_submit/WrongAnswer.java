package com.example.quiz_app_submit;

public class WrongAnswer {
    private String category;
    private Question question;

    public WrongAnswer(String category, Question question) {
        this.category = category;
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public Question getQuestion() {
        return question;
    }
}
