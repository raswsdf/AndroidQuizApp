package com.example.quiz_app_submit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WrongNoteActivity extends AppCompatActivity {

    private Button btnFinanceQuiz, btnEconomyQuiz, btnPublicQuiz, btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_note);

        btnFinanceQuiz = findViewById(R.id.btnFinanceQuiz);
        btnEconomyQuiz = findViewById(R.id.btnEconomyQuiz);
        btnPublicQuiz = findViewById(R.id.btnPublicQuiz);
        btnHelp = findViewById(R.id.btnHelp);

        // WrongNoteActivity.java 안에서, 버튼 리스너 부분만 교체
        btnEconomyQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, WrongNoteActivityDetail.class);
            intent.putExtra("category", "economy"); // lower key
            startActivity(intent);
        });

        btnFinanceQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, WrongNoteActivityDetail.class);
            intent.putExtra("category", "finance");
            startActivity(intent);
        });

        btnPublicQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, WrongNoteActivityDetail.class);
            intent.putExtra("category", "public");
            startActivity(intent);
        });

        // 도움말 버튼 클릭 시 팝업 띄우기
        btnHelp.setOnClickListener(v -> showHelpDialog());
    }

    private void showHelpDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("도움말")
                .setMessage("각 카테고리 별 버튼을 누르면 틀린 문제 목록을 확인할 수 있습니다.\n\n" +
                        "아래 복습 퀴즈 진행을 누르시면 틀린 문제에 대한 4지선다 퀴즈를 진행할 수 있으며, 선택지는 모두 틀린 문제 목록에서 출제됩니다.\n\n" +
                        "4문제 미만으로 문제 개수가 남은 경우, 선택지가 줄어들며, 1문제가 남은 경우 '수고하셨습니다'라는 문구가 선지에 함께 나타나 모든 문제 복습이 이루어졌음을 알려드립니다.")
                .setCancelable(true)
                .setPositiveButton("확인", (clickedDialog, which) -> clickedDialog.dismiss())
                .create();
        dialog.setOnShowListener(ignored -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setBackgroundTintList(
                    getColorStateList(R.color.button_background_tint));
            positiveButton.setTextColor(getColor(R.color.white));
        });
        dialog.show();
    }
}
