package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class BannerDeleteActivity extends AppCompatActivity {

    private EditText et_bannerDelete_position;
    private Button btn_bannerDelete_confirm,btn_bannerDelete_cancel;
    PreferenceManager pm;
    ArrayList<String> list;
    private static final String TAG = BannerDeleteActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_delete);

        pm = new PreferenceManager(getApplicationContext());
        list = new ArrayList<>(pm.getStringArrayPref("bannerImageUrl"));
        et_bannerDelete_position = findViewById(R.id.et_bannerDelete_position);
        btn_bannerDelete_cancel = findViewById(R.id.btn_bannerDelete_cancel);
        btn_bannerDelete_cancel.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                finish();
             }
         });

        btn_bannerDelete_confirm = findViewById(R.id.btn_bannerDelete_confirm);
        btn_bannerDelete_confirm.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                int position = Integer.parseInt(et_bannerDelete_position.getText().toString())-1;
                list.remove(position);
                pm.setStringArrayPref("bannerImageUrl",list);
                et_bannerDelete_position.setText("");
                Log.w(TAG,"/"+list.toString());
                Toast.makeText(getApplicationContext(),"삭제 완료. 인덱스 번호 : "+position+" 현재 리스트 사이즈 : /"+list.size(),Toast.LENGTH_SHORT).show();
             }
         });
    }
}