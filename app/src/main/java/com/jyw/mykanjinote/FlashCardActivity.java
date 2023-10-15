package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class FlashCardActivity extends AppCompatActivity {

    private ImageView imageView,iv_back;
    private Button btn_start,btn_replay,btn_shuffle;
    private TextView tv_character,tv_meaning,tv_sound,tv_memorize;
    private LinearLayout layout_flashCard;
    ArrayList<String> list;
    public int num=0;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        Intent data = getIntent();
        list = data.getStringArrayListExtra("allCharList");

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                btn_start.setVisibility(View.INVISIBLE);
                btn_start.setEnabled(false);
                layout_flashCard.setVisibility(View.VISIBLE);
                thread = new Thread(new NewRunnable());
                btn_shuffle.setVisibility(View.INVISIBLE);
                btn_shuffle.setEnabled(false);
                thread.start();
             }
         });

        btn_shuffle = findViewById(R.id.btn_shuffle);
        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i<list.size();i++){
                    int rand = (int)(Math.random()* list.size());
                    String temp = list.get(i);
                    list.set(i,list.get(rand));
                    list.set(rand,temp);
                    Toast.makeText(getApplicationContext(),"섞기 완료",Toast.LENGTH_SHORT).show();
                    Log.i("FlashCard","/"+list.toString());
                }
            }
        });

        btn_replay = findViewById(R.id.btn_replay);
        btn_replay.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                num=0;
                btn_replay.setVisibility(View.INVISIBLE);
                btn_replay.setEnabled(false);
                thread = new Thread(new NewRunnable());
                thread.start();
             }
         });

        layout_flashCard = findViewById(R.id.layout_flashCard);

        tv_character = findViewById(R.id.textView_char);
        tv_meaning = findViewById(R.id.textView_meaning);
        tv_sound = findViewById(R.id.textView_sound);
        tv_memorize = findViewById(R.id.textView_memorize);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                if(thread!=null){
                    thread.interrupt();
                }
                finish();
             }
         });

    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String charContents = list.get(num);
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

            tv_character.setText(charContentsList.get(0));
            tv_meaning.setText(charContentsList.get(1));
            tv_sound.setText(charContentsList.get(2));
            tv_memorize.setText(charContentsList.get(3));
            imageView = findViewById(R.id.imageView);
            Log.e("FlashCardActivity","/"+charContentsList.toString());

            String imageString = charContentsList.get(5);
            if(!imageString.equals("")){
                Uri imageUri = Uri.parse(charContentsList.get(5));
                imageView.setImageURI(imageUri);
            } else if(!charContentsList.get(6).equals("")){
                loadImgArr(charContentsList.get(6));
            } else{
                imageView.setImageResource(R.drawable.transperent);
            }
            num++;
            if(num==list.size()){
                btn_replay.setVisibility(View.VISIBLE);
                btn_replay.setEnabled(true);
                btn_shuffle.setVisibility(View.VISIBLE);
                btn_shuffle.setEnabled(true);
            }
        }
    };

    class NewRunnable implements Runnable {
        @Override
        public void run() {
            while (num<list.size()) {
                // 메인 스레드에 runnable 전달.
                runOnUiThread(runnable) ;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private void loadImgArr(String filename){
        try{
            File storageDir = new File(getFilesDir()+"/capture");

            File file = new File(storageDir,filename);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"이미지 로드 실패",Toast.LENGTH_SHORT).show();
        }
    }


}