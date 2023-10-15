
package com.jyw.mykanjinote;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardView cardView_all,cardView_not_memorized,cardView_memorized;
    private ImageButton imageButton_setting,imageButton_add;
    private TextView textView_all_number,textView_not_memorized_number,textView_memorized_number;
    private ListView listView;
    private MainListAdapter mainAdapter;
    private Button btn_news,btn_banner;
    MainListObject mainListObject;
    private static final String TAG = MainActivity.class.getSimpleName();

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    SliderAdapter sliderAdapter;
    PreferenceManager pm;
    List<SliderItem> sliderItems;
    ArrayList<String> bannerList;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = new PreferenceManager(getApplicationContext());

        if(pm.getStringArrayPref("folderList").size()==0) {
            ArrayList<String> folderList = new ArrayList<>();
            folderList.add("기본 단어장");
            pm.setStringArrayPref("folderList",folderList);
            pm.editor.apply();
        }

        listView=findViewById(R.id.listView_main);
        mainListObject = new MainListObject(getApplicationContext());
        mainAdapter = new MainListAdapter(this, mainListObject.getData());
        listView.setAdapter(mainAdapter);

        //폴더 추가하기
        imageButton_add = findViewById(R.id.imageButton_add);
        imageButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddFolderActivity.class);
                ActivityResultLauncher.launch(intent);
            }
        });

        imageButton_setting = findViewById(R.id.imageButton_setting);
        imageButton_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FolderSettingActivity.class);
                startActivity(intent);
            }
        });

        //리스트뷰 아이템클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,NoteActivity.class);
                MainListData mainListData = (MainListData)parent.getItemAtPosition(position);
                String folderName = mainListData.getFolderName();
                intent.putExtra("folderName", folderName);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });



        textView_all_number = findViewById(R.id.textView_all_number);

        cardView_all = findViewById(R.id.CardView_all);
        cardView_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NoteListAllActivity.class);
                ArrayList<String> folderNames = pm.getStringArrayPref("folderList");
                ArrayList<String> allCharList = new ArrayList<String>();
                for (int i = 0; i <folderNames.size(); i++){
                    ArrayList<String> charList = pm.getStringArrayPref(folderNames.get(i));
                    for (int j = 0; j <charList.size(); j++){
                        allCharList.add(charList.get(j));
                    }
                }
                Log.i(TAG,"allCharList.size : "+ allCharList.size());
                intent.putExtra("folderName","전체");
                intent.putExtra("allCharList",allCharList);
                startActivity(intent);
            }
        });

        textView_not_memorized_number=findViewById(R.id.textView_not_memorized_number);
        cardView_not_memorized = findViewById(R.id.CardView_not_memorized);
        cardView_not_memorized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NoteListAllActivity.class);
                ArrayList<String> folderNames = pm.getStringArrayPref("folderList");
                ArrayList<String> allCharList = new ArrayList<String>();
                for (int i = 0; i <folderNames.size(); i++){
                    ArrayList<String> charList = pm.getStringArrayPref(folderNames.get(i));
                    for (int j = 0; j <charList.size(); j++){
                        allCharList.add(charList.get(j));
                    }
                }
                Log.i(TAG,"allCharList.size : "+ allCharList.size());
                intent.putExtra("folderName","미암기");
                intent.putExtra("allCharList",allCharList);
                startActivity(intent);
            }
        });

        textView_memorized_number = findViewById(R.id.textView_memorized_number);
        cardView_memorized = findViewById(R.id.CardView_memorized);
        cardView_memorized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NoteListAllActivity.class);
                ArrayList<String> folderNames = pm.getStringArrayPref("folderList");
                ArrayList<String> allCharList = new ArrayList<String>();
                for (int i = 0; i <folderNames.size(); i++){
                    ArrayList<String> charList = pm.getStringArrayPref(folderNames.get(i));
                    for (int j = 0; j <charList.size(); j++){
                        allCharList.add(charList.get(j));
                    }
                }
                Log.i(TAG,"allCharList.size : "+ allCharList.size());
                intent.putExtra("folderName","암기");
                intent.putExtra("allCharList",allCharList);
                startActivity(intent);
            }
        });

        viewPager2 = findViewById(R.id.vp_commercials);

        bannerList = new ArrayList<>(pm.getStringArrayPref("bannerImageUrl"));
        if(bannerList.size()==0){
            ArrayList<String> bannerImageUrl = new ArrayList<>();
            bannerImageUrl.add("https://images.unsplash.com/photo-1528164344705-47542687000d?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1069&q=80");
            bannerImageUrl.add("https://images.unsplash.com/photo-1461727885569-b2ddec0c4328?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1049&q=80");
            bannerImageUrl.add("https://images.unsplash.com/photo-1610792472360-75c15db37d5e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80");
            bannerImageUrl.add("https://images.unsplash.com/photo-1613355583011-e48de090f4da?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1189&q=80");
            pm.setStringArrayPref("bannerImageUrl",bannerImageUrl);
            Log.w(TAG,"imageLoaded");
            sliderItems = new ArrayList<>();
            bannerList = new ArrayList<>(pm.getStringArrayPref("bannerImageUrl"));
            Log.w(TAG,"/"+bannerList.toString());
            for (int i = 0; i < bannerList.size(); i++){
                sliderItems.add(new SliderItem(bannerList.get(i),"https://www.jlpt.or.kr/index.html"));
            }
            sliderAdapter = new SliderAdapter(sliderItems, viewPager2,getApplicationContext());
            viewPager2.setAdapter(sliderAdapter);
        }



        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);
            }
        });

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,3000);

        //전체 한자 개수 초기화
        int allFolderNum =0;
        int allFalseNum =0;
        int allTrueNum = 0;
        ArrayList<String> folderNames = pm.getStringArrayPref("folderList");
        for (int i = 0; i <folderNames.size(); i++){
            ArrayList<String> list = pm.getStringArrayPref(folderNames.get(i));
            allFolderNum = allFolderNum+list.size();

            if(list.size()>0){
                for (int j = 0; j < list.size(); j++){
                    String charContents = list.get(j);
                    ArrayList<String> charContentsList = new ArrayList<String>();
                    if (charContents != null) {
                        try {
                            JSONArray a = new JSONArray(charContents);
                            for (int k = 0; k < a.length(); k++) {
                                String url = a.optString(k);
                                charContentsList.add(url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(pm.getBoolean(charContentsList.get(0))){
                        allTrueNum++;
                    }
                    else{
                        allFalseNum++;
                    }
                }
            }
        }
        String allNum = Integer.toString(allFolderNum);
        String trueNum = Integer.toString(allTrueNum);
        String falseNum = Integer.toString(allFalseNum);
        textView_all_number.setText(allNum);
        textView_memorized_number.setText(trueNum);
        textView_not_memorized_number.setText(falseNum);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sliderItems = new ArrayList<>();
        bannerList = new ArrayList<>(pm.getStringArrayPref("bannerImageUrl"));
        Log.w(TAG,"/"+bannerList.toString());
        for (int i = 0; i < bannerList.size(); i++){
            sliderItems.add(new SliderItem(bannerList.get(i),"https://www.jlpt.or.kr/index.html"));
        }
        sliderAdapter = new SliderAdapter(sliderItems, viewPager2,getApplicationContext());
        viewPager2.setAdapter(sliderAdapter);
        Log.i(TAG,": onRestart");
        mainListObject.refresh();
        mainAdapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()== Activity.RESULT_OK){
                Intent data = result.getData();
                assert data != null;
                String name = data.getStringExtra("folderName");
                mainListObject.addData(name);
                mainAdapter.notifyDataSetChanged();
            }
        }
    });
}