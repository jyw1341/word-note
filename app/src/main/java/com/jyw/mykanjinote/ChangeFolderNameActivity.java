package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ChangeFolderNameActivity extends AppCompatActivity {

    private Button btn_cancel, btn_confirm;
    EditText editText_folderName;
    private static final String TAG = AddFolderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_folder_name);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        editText_folderName = findViewById(R.id.editText_folderName);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderName = editText_folderName.getText().toString();
                PreferenceManager pm = new PreferenceManager(getApplicationContext());
                ArrayList<String> folderList = pm.getStringArrayPref("folderList");
                if(folderName.length()==0) {
                    Log.i(TAG," : no string");
                    Toast.makeText(ChangeFolderNameActivity.this, "1글자 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    if(folderList.contains(folderName)){
                        Toast.makeText(ChangeFolderNameActivity.this, "이미 같은 이름의 폴더가 존재합니다", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        finish();
                    }
                }
            }
        });
    }
}