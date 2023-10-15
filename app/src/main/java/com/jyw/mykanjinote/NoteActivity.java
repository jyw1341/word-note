package com.jyw.mykanjinote;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NoteActivity extends AppCompatActivity{

    Spinner spinner_sortByTime;
    String folderName,charNum;

    private Button btn_back,btn_play,btn_quiz;
    private RecyclerView recyclerView_note;
    private TextView textView_folderName,textView_num;
    private static final String TAG = NoteListObject.class.getSimpleName();

    NoteListAdapter noteListAdapter;
    NoteListObject noteListObject;
    PreferenceManager pm;
    private static Handler handler;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

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
        noteListObject = new NoteListObject(getApplicationContext(),folderName);
        list = pm.getStringArrayPref(folderName);
        noteListAdapter = new NoteListAdapter(this,noteListObject.getData(),folderName);
        Log.i(TAG,"어댑터 실행 : "+noteListObject.getDataSize());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_note.setLayoutManager(linearLayoutManager);
        recyclerView_note.setAdapter(noteListAdapter);

        textView_folderName = findViewById(R.id.textView_folderName);
        textView_folderName.setText(folderName);
        textView_num = findViewById(R.id.textView_num);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                charNum = Integer.toString(noteListAdapter.getItemCount());
                textView_num.setText(charNum);
            }
        };

        class NewRunnable implements  Runnable{
            @Override
            public void run() {
                while(true){
                    try{
                      Thread.sleep(500);
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
                    noteListAdapter.notifyDataSetChanged();
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
                    noteListAdapter.notifyDataSetChanged();
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCharacterActivity.class);
                intent.putExtra("folderName",folderName);
                intent.putExtra("charNum",noteListObject.getDataSize());
                ActivityResultLauncher.launch(intent);
            }
        });
        btn_quiz = findViewById(R.id.btn_quiz);
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = noteListAdapter.getItemCount();
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
                        NoteListData noteListData = noteListAdapter.noteListData.get(data[i]);
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
                int count = noteListAdapter.getItemCount();
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

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()== Activity.RESULT_OK){
                Intent data = result.getData();
                assert data != null;
                String charNum = Integer.toString(data.getIntExtra("charResult",0));
                ArrayList<String> charContentsList = data.getStringArrayListExtra("charList");
                NoteListData noteListData = new NoteListData(charContentsList.get(0),charContentsList.get(1),charContentsList.get(2),charContentsList.get(3),charContentsList.get(4),charContentsList.get(7));
                //저장된 순서대로 삽입
                if(pm.getInt("spinnerPosition")==0){
                    noteListObject.data.add(noteListData);
                }
                //최근 순서로 표시하기 위해 역순으로 삽입
                else if(pm.getInt("spinnerPosition")==1){
                    noteListObject.data.add(0,noteListData);
                }
                noteListAdapter.notifyDataSetChanged();
                textView_num.setText(charNum);
            }
        }
    });
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        Log.i(TAG," : "+pm.getInt("spinnerPosition"));
        noteListObject.data.clear();
        ArrayList<String> charList = pm.getStringArrayPref(folderName);
        for(int i=0;i<charList.size();i++) {

            String charContents = charList.get(i);

            ArrayList<String> charContentsList = new ArrayList<String>();
            if (charContents != null) {
                try {
                    JSONArray a = new JSONArray(charContents);
                    for (int j = 0; j < a.length(); j++) {
                        String url = a.optString(j);
                        charContentsList.add(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            NoteListData noteListData = new NoteListData(charContentsList.get(0),charContentsList.get(1),charContentsList.get(2),charContentsList.get(3),charContentsList.get(4),charContentsList.get(7));
            noteListObject.data.add(noteListData);
        }
        noteListAdapter.notifyDataSetChanged();
    }
}