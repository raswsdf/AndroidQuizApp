package com.example.quiz_app_submit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "wrong_questions_pref";
    private static SharedPreferencesManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    public void saveWrongQuestions(String category, List<Question> newQuestions) {
        List<Question> savedWrongQuestions = getWrongQuestions(category);
        // 기존 문제가 없다면 새로운 리스트로 시작
        if (savedWrongQuestions == null) {
            savedWrongQuestions = new ArrayList<>();
        }

        // 기존 문제 리스트에 추가
        savedWrongQuestions.addAll(newQuestions);

        // 중복 문제를 제거하기 위해 Set
        Set<Question> questionSet = new HashSet<>(savedWrongQuestions);
        // Set을 JSON으로 변환해 저장
        String json = gson.toJson(questionSet);
        editor.putString(category, json);
        editor.apply();
        Log.d("SharedPreferences", "저장 후 wrongQuestions size: " + questionSet.size());
    }

    public void addWrongQuestions(String category, List<Question> newQuestions) {

        Set<Question> questionSet = new HashSet<>(newQuestions);
        String json = gson.toJson(questionSet);
        editor.putString(category, json);
        editor.apply();
        Log.d("SharedPreferences", "저장 후 wrongQuestions size: " + questionSet.size());
    }

    public List<Question> getWrongQuestions(String category) {
        String json = sharedPreferences.getString(category, null);
        if (json != null) {

            Type type = new TypeToken<Set<Question>>(){}.getType();
            Set<Question> questionSet = gson.fromJson(json, type);

            return new ArrayList<>(questionSet);
        }
        return null;
    }


}