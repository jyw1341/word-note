package com.jyw.mykanjinote;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class FolderSettingActivity extends AppCompatActivity {

    private ListView listView_setting;
    private SettingListAdapter folderSettingAdapter;
    MainListObject mainListObject;

    private Button btn_select_all,btn_delete,btn_modify;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_setting);

        listView_setting=findViewById(R.id.listView_setting);
        mainListObject = new MainListObject(getApplicationContext());
        folderSettingAdapter = new SettingListAdapter(this, mainListObject.getData());
        listView_setting.setAdapter(folderSettingAdapter);

        btn_select_all = findViewById(R.id.btn_select_all);
        btn_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = folderSettingAdapter.getCount();
                for (int i = 0; i < count; i++){
                    listView_setting.setItemChecked(i,true);
                }
            }
        });

        //폴더 삭제 과정
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView_setting.getCheckedItemPositions();
                int count = folderSettingAdapter.getCount();

                for (int i = count-1; i >=0 ; i--){
                    if(checkedItems.get(i)){
                        PreferenceManager pm = new PreferenceManager(getApplicationContext());
                        ArrayList<String> folderList = pm.getStringArrayPref("folderList");
                        int folderIndex = folderList.indexOf(mainListObject.data.get(i).getFolderName());
                        //셰어드에서 폴더이름 삭제
                        if(folderIndex!=-1){
                            folderList.remove(folderIndex);
                            pm.setStringArrayPref("folderList",folderList);
                        }
                        pm.editor.remove(mainListObject.data.get(i).getFolderName());
                        pm.editor.apply();
                        //오브젝트 클래스의 데이터 리스트에서 삭제. 리스트 새로고침 됨.
                        mainListObject.data.remove(i);
                    }
                }
                listView_setting.clearChoices();
                folderSettingAdapter.notifyDataSetChanged();
        }
        });

        btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView_setting.getCheckedItemPositions();
                int count = folderSettingAdapter.getCount();

                Log.i("FolderSettingActivity","Entrance : 1");
                if(checkedItems.size()==1){
                    Log.i("FolderSettingActivity","Entrance : 2");
                    for (int j = count-1; j >=0 ;j--) {
                        if (checkedItems.get(j)) {
                            Log.i("FolderSettingActivity", "Entrance : 3");
                            AlertDialog.Builder builder = new AlertDialog.Builder(FolderSettingActivity.this);
                            final EditText editText = new EditText(getApplicationContext());
                            builder.setTitle("폴더 이름 변경");
                            builder.setView(editText);
                            int finalJ = j;
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PreferenceManager pm = new PreferenceManager(getApplicationContext());
                                    ArrayList<String> folderList = pm.getStringArrayPref("folderList");
                                    String str = editText.getText().toString();

                                    //기존 폴더가 보유한 단어 개수 삭제 후 새폴더 이름으로 다시 저장
                                    int num = pm.getInt(folderList.get(finalJ)+"num");
                                    pm.editor.remove(folderList.get(finalJ)+"num");
                                    pm.putInt(str+"num",num);
                                    //기존 폴더가 갖고 있던 단어 리스트 이전
                                    ArrayList<String> al = pm.getStringArrayPref(folderList.get(finalJ));
                                    pm.editor.remove(folderList.get(finalJ));
                                    pm.setStringArrayPref(str,al);
                                    pm.editor.apply();
                                    //폴더 이름 변경
                                    folderList.set(finalJ, str);
                                    pm.setStringArrayPref("folderList", folderList);
                                    mainListObject.data.get(finalJ).changeFolderName(str);
                                }
                            });
                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder.show();
                        }
                    }
                    listView_setting.clearChoices();
                    folderSettingAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void back(View view){
        finish();
    }
}