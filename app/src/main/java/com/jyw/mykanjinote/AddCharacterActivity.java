package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;

import java.util.ArrayList;

public class AddCharacterActivity extends AppCompatActivity {

    EditText et_character,et_meaning,et_sound,et_memorize;
    Button btn_confirm, btn_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);

        PreferenceManager pm = new PreferenceManager(getApplicationContext());

        et_character = findViewById(R.id.et_character);
        et_meaning = findViewById(R.id.et_meaning);
        et_sound = findViewById(R.id.et_sound);
        et_memorize = findViewById(R.id.et_memorize);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_confirm=findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String character = et_character.getText().toString();
                String meaning = et_meaning.getText().toString();
                String sound = et_sound.getText().toString();
                String memorize = et_memorize.getText().toString();
                Intent data = getIntent();
                String folderName = data.getStringExtra("folderName");

                ArrayList<String> list = new ArrayList<>();
                pm.putBoolean(character,false);

                list.add(character);
                list.add(meaning);
                list.add(sound);
                list.add(memorize);

                long now = System.currentTimeMillis();
                String getTime = Long.toString(now);
                list.add(getTime);
                //image uri 저장 자리
                list.add("");
                //
                list.add("");
                list.add(folderName); //index 7 size 8

                JSONArray a = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    a.put(list.get(i));
                }

                String charContents = a.toString();

                if(pm.pref.getString(folderName,null)==null){
                    ArrayList<String> charList = new ArrayList<>();
                    //저장된 순서대로 삽입
                    if(pm.getInt("spinnerPosition")==0){
                        charList.add(charContents);
                    }
                    //최근 순서로 표시하기 위해 역순으로 삽입
                    else if(pm.getInt("spinnerPosition")==1){
                        charList.add(0,charContents);
                    }
                    pm.setStringArrayPref(folderName,charList);
                }
                else{
                    ArrayList<String> charList = pm.getStringArrayPref(folderName);
                    if(pm.getInt("spinnerPosition")==0){
                        charList.add(charContents);
                    }
                    else if(pm.getInt("spinnerPosition")==1){
                        charList.add(0,charContents);
                    }
                    pm.setStringArrayPref(folderName,charList);
                }
                finish();
            }
        });
    }
}