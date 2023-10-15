package com.jyw.mykanjinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NoteListAllActivity extends AppCompatActivity {

    Spinner spinner_sortByTime;
    String folderName,charNum;

    private Button btn_back,btn_quiz,btn_play;
    private RecyclerView recyclerView_note;
    private TextView textView_folderName,textView_num;
    private static final String TAG = NoteListObject.class.getSimpleName();

    NoteListAllAdapter noteListAllAdapter;
    NoteListObject noteListObject;
    PreferenceManager pm;
    private static Handler handler;

    ArrayList<String> list;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list_all);

        Intent data = getIntent();
        folderName = data.getStringExtra("folderName");

        pm = new PreferenceManager(getApplicationContext());

        final String[] sort = {"오래된순","최신순"};
        spinner_sortByTime = findViewById(R.id.spinner_sortByTime);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,sort);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sortByTime.setAdapter(adapter1);

        spinner_sortByTime.setSelection(pm.getInt("spinnerPosition"));

        recyclerView_note = findViewById(R.id.recyclerView_note);

        list = data.getStringArrayListExtra("allCharList");
        noteListObject = new NoteListObject(getApplicationContext(),list,folderName);
        noteListAllAdapter = new NoteListAllAdapter(this,noteListObject.getData(),folderName);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_note.setLayoutManager(linearLayoutManager);
        recyclerView_note.setAdapter(noteListAllAdapter);

        textView_folderName = findViewById(R.id.textView_folderName);
        textView_folderName.setText(folderName);
        textView_num = findViewById(R.id.textView_num);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                charNum = Integer.toString(noteListAllAdapter.getItemCount());
                textView_num.setText(charNum);
            }
        };

        class NewRunnable implements  Runnable{
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(300);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        }
        Thread thread = new Thread(new NewRunnable());
        thread.start();


        spinner_sortByTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //저장된지 오래된 순서대로 정렬
                if(position==0&&pm.getInt("spinnerPosition")!=spinner_sortByTime.getSelectedItemPosition()){
                    Log.i(TAG,"sortByOld");
                    Comparator<NoteListData> old = new Comparator<NoteListData>() {
                        @Override
                        public int compare(NoteListData o1, NoteListData o2) {
                            int ret;
                            long time1 = Long.parseLong(o1.getTime());
                            long time2 = Long.parseLong(o2.getTime());

                            if(time1<time2)
                                ret=-1;
                            else if(time1==time2)
                                ret=0;
                            else
                                ret=1;
                            return ret;
                        }
                    };
                    Collections.sort(noteListObject.data,old);
                    ArrayList<String> list = pm.getStringArrayPref(folderName);
                    Collections.reverse(list);
                    pm.setStringArrayPref(folderName,list);
                    pm.putInt("spinnerPosition",spinner_sortByTime.getSelectedItemPosition());
                    noteListAllAdapter.notifyDataSetChanged();
                }
                //최근에 저장된 순서대로 정렬
                if(position==1&&pm.getInt("spinnerPosition")!=spinner_sortByTime.getSelectedItemPosition()){
                    Log.i(TAG,"sortByRecent");
                    Comparator<NoteListData> recent = new Comparator<NoteListData>() {
                        @Override
                        public int compare(NoteListData o1, NoteListData o2) {
                            int ret;
                            long time1 = Long.parseLong(o1.getTime());
                            long time2 = Long.parseLong(o2.getTime());

                            if(time1>time2)
                                ret=-1;
                            else if(time1==time2)
                                ret=0;
                            else
                                ret=1;
                            return ret;
                        }
                    };
                    Collections.sort(noteListObject.data,recent);
                    ArrayList<String> list = pm.getStringArrayPref(folderName);
                    Collections.reverse(list);
                    pm.setStringArrayPref(folderName,list);
                    pm.putInt("spinnerPosition",spinner_sortByTime.getSelectedItemPosition());
                    noteListAllAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_quiz = findViewById(R.id.btn_quiz);
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = noteListAllAdapter.getItemCount();
                if(count>=5){
                    int temp,x,y;
                    int[] data = new int[count];
                    for (int i = 0; i < count; i++){
                        data[i] = i;
                    }
                    for (int i = 0; i < count*2; i++){
                        x = (int)(Math.random()*count);
                        y = (int)(Math.random()*count);
                        if(x!=y){
                            temp = data[x];
                            data[x]=data[y];
                            data[y]=temp;
                        }
                    }
                    ArrayList<String> quizList = new ArrayList<>();
                    for (int i = 0; i < count; i++){
                        NoteListData noteListData = noteListAllAdapter.noteListData.get(data[i]);
                        quizList.add(noteListData.getCharacter());
                        quizList.add(noteListData.getMeaning());
                        quizList.add(noteListData.getSound());
                    }
                    Intent intent = new Intent(getApplicationContext(), QuizMenuActivity.class);
                    intent.putExtra("folderName",folderName);
                    intent.putExtra("folderNum",charNum);
                    intent.putExtra("folderContents",quizList);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"퀴즈를 하기 위해서는 목록에 5문제 이상이 존재해야 합니다",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
             int count = noteListAllAdapter.getItemCount();
             if(count>=5){
                 Intent intent = new Intent(getApplicationContext(), FlashCardActivity.class);
                 intent.putExtra("allCharList",list);
                 startActivity(intent);
             }else{
                 Toast.makeText(getApplicationContext(),"이어보기 하기 위해서는 목록에 5문제 이상이 존재해야 합니다",Toast.LENGTH_SHORT).show();
             }
             }
         });
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        ArrayList<String> folderNames = pm.getStringArrayPref("folderList");
        ArrayList<String> allCharList = new ArrayList<String>();
        for (int i = 0; i <folderNames.size(); i++){
            ArrayList<String> charList = pm.getStringArrayPref(folderNames.get(i));
            for (int j = 0; j <charList.size(); j++){
                allCharList.add(charList.get(j));
            }
        }
        noteListObject = new NoteListObject(getApplicationContext(),allCharList,folderName);
        noteListAllAdapter = new NoteListAllAdapter(this,noteListObject.getData(),folderName);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_note.setLayoutManager(linearLayoutManager);
        recyclerView_note.setAdapter(noteListAllAdapter);
    }
}
