package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsPageActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_content,tv_translated,tv_japanese_title,tv_korean_title;
    private Button btn_translate;
    String translatedTitle,translatedContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        Intent dataIntent = getIntent();
        String title = dataIntent.getStringExtra("title");
        String content = dataIntent.getStringExtra("content");

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                finish();
             }
         });
        tv_korean_title = findViewById(R.id.tv_korean_title);
        tv_japanese_title = findViewById(R.id.tv_japanese_title);
        tv_japanese_title.setText(title);

        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(content);
        tv_translated = findViewById(R.id.tv_translated);



        btn_translate = findViewById(R.id.btn_translate);
        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_korean_title.setText(translatedTitle);
                tv_translated.setText(translatedContent);
            }
        });

        Handler mHandler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                btn_translate.setVisibility(View.VISIBLE);
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                NaverTranslator translator = new NaverTranslator();
                translatedTitle = translator.translate(title);
                translatedContent = translator.translate(content);
                mHandler.post(runnable);
            }
        }
        Thread thread = new Thread(new NewRunnable());
        thread.start();

    }
}