package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizMenuActivity extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_back;
    private Button btn_kanjiQuiz;
    private Button btn_meaningSoundQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_menu);
        Intent dataIntent = getIntent();
        String title = dataIntent.getStringExtra("folderName")+" ("+dataIntent.getStringExtra("folderNum")+")";
        ArrayList<String> list = dataIntent.getStringArrayListExtra("folderContents");

        ArrayList<String> kanji = new ArrayList<>();
        for (int i = 0; i < list.size(); i=i+3){
            kanji.add(list.get(i));
        }
        ArrayList<String> meaningSound = new ArrayList<>();
        for (int i = 0; i < list.size(); i=i+3){
            String str = list.get(i+1)+"/"+list.get(i+2);
            meaningSound.add(str);
        }


        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_kanjiQuiz = findViewById(R.id.btn_kanjiQuiz);
        btn_kanjiQuiz.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                intent.putExtra("type","kanjiQuiz");
                intent.putExtra("kanjiList",kanji);
                intent.putExtra("meaningSoundList",meaningSound);
                startActivity(intent);
             }
         });

        btn_meaningSoundQuiz = findViewById(R.id.btn_meaningSoundQuiz);
        btn_meaningSoundQuiz.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
             Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
             intent.putExtra("type","meaningSoundQuiz");
             intent.putExtra("kanjiList",kanji);
             intent.putExtra("meaningSoundList",meaningSound);
             startActivity(intent);
             }
         });
    }
}