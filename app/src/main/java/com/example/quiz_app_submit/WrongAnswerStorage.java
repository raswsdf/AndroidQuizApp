package com.example.quiz_app_submit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrongAnswerStorage {

    private static Map<String, List<Question>> wrongAnswers = new HashMap<>();

    // 카테고리별로 잘못된 답안을 가져오는 메서드
    public static List<Question> getWrongAnswers(String category) {
        return wrongAnswers.computeIfAbsent(category, k -> new ArrayList<>());
    }

    public static void addWrongAnswers(String category, List<Question> questions) {
        if (!wrongAnswers.containsKey(category)) {
            wrongAnswers.put(category, new ArrayList<>());
        }
        List<Question> currentWrongAnswers = wrongAnswers.get(category);
        for (Question question : questions) {
            if (!currentWrongAnswers.contains(question)) {
                currentWrongAnswers.add(question);
            }
        }
    }
}
