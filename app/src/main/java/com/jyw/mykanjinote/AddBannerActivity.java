package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddBannerActivity extends AppCompatActivity {

    private EditText et_bannerAdd_url,et_bannerAdd_position;
    private Button btn_bannerAdd_confirm,btn_bannerAdd_cancel;
    PreferenceManager pm;
    ArrayList<String> list;
    private static final String TAG = AddBannerActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banner);

        pm = new PreferenceManager(getApplicationContext());
        list = new ArrayList<>(pm.getStringArrayPref("bannerImageUrl"));
        et_bannerAdd_url = findViewById(R.id.et_bannerAdd_url);
        et_bannerAdd_position = findViewById(R.id.et_bannerAdd_position);

        btn_bannerAdd_cancel = findViewById(R.id.btn_bannerAdd_cancel);
        btn_bannerAdd_cancel.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                finish();
             }
         });

        btn_bannerAdd_confirm = findViewById(R.id.btn_bannerAdd_confirm);
        btn_bannerAdd_confirm.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String url = et_bannerAdd_url.getText().toString();
                String position = et_bannerAdd_position.getText().toString();
                if(position.equals("")){
                    list.add(url);
                } else {
                    int pos = Integer.parseInt(position)-1;
                    list.add(pos,url);
                }
             pm.setStringArrayPref("bannerImageUrl",list);
                et_bannerAdd_url.setText("");
                et_bannerAdd_position.setText("");
             Toast.makeText(getApplicationContext(),"추가 완료",Toast.LENGTH_SHORT).show();
             Log.w(TAG,"/"+list.toString());
         }

         });
    }
}