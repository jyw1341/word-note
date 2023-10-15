package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class bannerSettingActivity extends AppCompatActivity {

    private Button btn_banner_add,btn_banner_change,btn_banner_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_setting);

        btn_banner_add =findViewById(R.id.btn_banner_add);
        btn_banner_delete = findViewById(R.id.btn_banner_delete);

        btn_banner_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBannerActivity.class);
                startActivity(intent);
            }
        });

        btn_banner_delete.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BannerDeleteActivity.class);
                startActivity(intent);
             }
         });
    }
}