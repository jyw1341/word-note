package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private TextView tv_count,tv_description,tv_kanji,tv_quizNum,tv_result1,tv_result2,tv_result3,tv_result4,tv_result5,tv_char1,tv_char2,
            tv_char3,tv_char4,tv_char5,tv_meaningSound1,tv_meaningSound2,tv_meaningSound3,tv_meaningSound4,tv_meaningSound5;
    private CheckBox ckBox_1,ckBox_2,ckBox_3,ckBox_4,ckBox_5;
    private static Handler mHandler;
    private Button btn_start,btn_back,btn_replay,btn_answer1, btn_answer2,btn_answer3,btn_answer4;
    private static final String TAG = QuizActivity.class.getSimpleName();
    Thread t;
    ArrayList<String> question,answerChoice;
    private LinearLayout layout_result;

    private int quest_num=0; //문제 인덱스 번호
    final int TOTAL_QUESTION =5; //전체 문제의 수

    PreferenceManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent dataIntent = getIntent();
        String type = dataIntent.getStringExtra("type");

        //리스트를 복사한다. 선언만 하고 그냥 넣으면 참조만 하니까 조심
        pm = new PreferenceManager(getApplicationContext());
        if(type.equals("kanjiQuiz")){
            question = new ArrayList<>(dataIntent.getStringArrayListExtra("kanjiList"));
            answerChoice = new ArrayList<>(dataIntent.getStringArrayListExtra("meaningSoundList"));
        } else if(type.equals("meaningSoundQuiz")){
            question = new ArrayList<>(dataIntent.getStringArrayListExtra("meaningSoundList"));
            answerChoice = new ArrayList<>(dataIntent.getStringArrayListExtra("kanjiList"));
        }

        tv_count = findViewById(R.id.tv_count);
        tv_description = findViewById(R.id.tv_description);
        tv_kanji = findViewById(R.id.tv_kanji);
        tv_quizNum = findViewById(R.id.tv_quizNum);

        layout_result = findViewById(R.id.layout_result);
        tv_result1 = findViewById(R.id.tv_result1);
        tv_result2 = findViewById(R.id.tv_result2);
        tv_result3 = findViewById(R.id.tv_result3);
        tv_result4 = findViewById(R.id.tv_result4);
        tv_result5 = findViewById(R.id.tv_result5);
        tv_char1 = findViewById(R.id.tv_char1);
        tv_char2 = findViewById(R.id.tv_char2);
        tv_char3 = findViewById(R.id.tv_char3);
        tv_char4 = findViewById(R.id.tv_char4);
        tv_char5 = findViewById(R.id.tv_char5);
        tv_meaningSound1 = findViewById(R.id.tv_meaningSound1);
        tv_meaningSound2 = findViewById(R.id.tv_meaningSound2);
        tv_meaningSound3 = findViewById(R.id.tv_meaningSound3);
        tv_meaningSound4 = findViewById(R.id.tv_meaningSound4);
        tv_meaningSound5 = findViewById(R.id.tv_meaningSound5);
        ckBox_1 = findViewById(R.id.ckBox_1);
        ckBox_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pm.putBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(0),isChecked);
            }
        });
        ckBox_2 = findViewById(R.id.ckBox_2);
        ckBox_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pm.putBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(1),isChecked);
            }
        });
        ckBox_3 = findViewById(R.id.ckBox_3);
        ckBox_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pm.putBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(2),isChecked);
            }
        });
        ckBox_4 = findViewById(R.id.ckBox_4);
        ckBox_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pm.putBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(3),isChecked);
            }
        });
        ckBox_5 = findViewById(R.id.ckBox_5);
        ckBox_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pm.putBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(4),isChecked);
            }
        });


        tv_description.setText("알맞은 답을 고르세요");

        btn_answer1 = findViewById(R.id.btn_answer1);
        btn_answer2 = findViewById(R.id.btn_answer2);
        btn_answer3 = findViewById(R.id.btn_answer3);
        btn_answer4 = findViewById(R.id.btn_answer4);

        disabledAllButton();


        btn_answer1.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                selectAnswer(1);
            }
         });


        btn_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAnswer(2);
            }
        });

        btn_answer3.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                selectAnswer(3);
             }
         });

        btn_answer4.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                selectAnswer(4);
             }
         });


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_replay = findViewById(R.id.btn_replay);
        btn_replay.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                for (int i = 0; i < question.size(); i++){
                    int rand = (int)(Math.random()*question.size());
                    String temp = question.get(i);
                    question.set(i, question.get(rand));
                    question.set(rand,temp);

                    temp = answerChoice.get(i);
                    answerChoice.set(i, answerChoice.get(rand));
                    answerChoice.set(rand,temp);
                }
                quest_num=0;
                layout_result.setVisibility(View.INVISIBLE);
                btn_back.setVisibility(View.INVISIBLE);
                btn_back.setEnabled(false);
                btn_replay.setVisibility(View.INVISIBLE);
                btn_replay.setEnabled(false);
                btn_start.setEnabled(true);
                tv_description.setVisibility(View.VISIBLE);
                tv_count.setVisibility(View.VISIBLE);
                tv_kanji.setVisibility(View.VISIBLE);
                btn_start.performClick();
             }
         });

        btn_start = findViewById(R.id.btn_start);
        btn_start.setText("시작");
        btn_start.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
             String quizNum = Integer.toString(quest_num+1)+"/"+TOTAL_QUESTION;
             tv_quizNum.setText(quizNum);
             //버튼에 있는 답안지 초기화
             tv_description.setTextColor(Color.BLACK);
             tv_description.setText("알맞은 답을 고르세요");
             btn_answer1.setText("");
             btn_answer2.setText("");
             btn_answer3.setText("");
             btn_answer4.setText("");
             enabledAllButton();
             btn_start.setVisibility(View.INVISIBLE);
             btn_start.setEnabled(false);

             ArrayList<String> example = new ArrayList<>(answerChoice);

             btn_answer1.setTextSize(24);
             btn_answer2.setTextSize(24);
             btn_answer3.setTextSize(24);
             btn_answer4.setTextSize(24);


             String correct_answer = example.get(quest_num);
             //똑같은 보기가 나오지 않도록 답안지 리스트에서 정답삭제
             example.remove(quest_num);
             //보기 셔플
             Collections.shuffle(example);
          
             tv_kanji.setText(question.get(quest_num));
             switch (quest_num){
                 case 0 :
                    tv_char1.setText(question.get(quest_num));
                     ckBox_1.setChecked(pm.getBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(0)));
                    break;
                 case 1 :
                    tv_char2.setText(question.get(quest_num));
                     ckBox_2.setChecked(pm.getBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(1)));
                    break;
                 case 2 :
                     tv_char3.setText(question.get(quest_num));
                     ckBox_3.setChecked(pm.getBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(2)));
                     break;
                 case 3 :
                     tv_char4.setText(question.get(quest_num));
                     ckBox_4.setChecked(pm.getBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(3)));
                     break;
                 case 4 :
                     tv_char5.setText(question.get(quest_num));
                     ckBox_5.setChecked(pm.getBoolean(dataIntent.getStringArrayListExtra("kanjiList").get(4)));
                     break;
             }
             //정답 배정용 난수
             int rand = (int)(Math.random()*4+1);
             switch(rand){
                 case 1:
                     btn_answer1.setText(correct_answer);
                     btn_answer2.setText(example.get(0));
                     btn_answer3.setText(example.get(1));
                     btn_answer4.setText(example.get(2));
                     break;
                 case 2 :
                     btn_answer2.setText(correct_answer);
                     btn_answer1.setText(example.get(0));
                     btn_answer3.setText(example.get(1));
                     btn_answer4.setText(example.get(2));
                     break;
                 case 3 :
                     btn_answer3.setText(correct_answer);
                     btn_answer1.setText(example.get(0));
                     btn_answer2.setText(example.get(1));
                     btn_answer4.setText(example.get(2));
                     break;
                 case 4 :
                     btn_answer4.setText(correct_answer);
                     btn_answer1.setText(example.get(0));
                     btn_answer2.setText(example.get(2));
                     btn_answer3.setText(example.get(1));
                     break;
                 default:
                     break;
                 }
                 //삭제했던 보기 다시 추가
                 example.add(correct_answer);
                 quest_num++;
             

             mHandler = new Handler() {
                 int num = 10;
                 @Override
                 public void handleMessage(Message msg) {
                     String count = Integer.toString(num);
                     if(num==0){
                         if(quest_num<TOTAL_QUESTION){
                             btn_start.setVisibility(View.VISIBLE);
                             btn_start.setEnabled(true);
                             btn_start.setText("다음 문제");
                             String timeOver = "시간 초과\n정답 :"+answerChoice.get(quest_num-1);
                             tv_description.setTextColor(Color.RED);
                             tv_description.setText(timeOver);

                             switch (quest_num-1){
                                 case 0 :
                                     tv_result1.setText("오답");
                                     tv_meaningSound1.setText(answerChoice.get(quest_num-1));
                                     tv_result1.setTextColor(Color.RED);
                                     break;
                                 case 1 :
                                     tv_result2.setText("오답");
                                     tv_meaningSound2.setText(answerChoice.get(quest_num-1));
                                     tv_result2.setTextColor(Color.RED);
                                     break;
                                 case 2 :
                                     tv_result3.setText("오답");
                                     tv_meaningSound3.setText(answerChoice.get(quest_num-1));
                                     tv_result3.setTextColor(Color.RED);
                                     break;
                                 case 3 :
                                     tv_result4.setText("오답");
                                     tv_meaningSound4.setText(answerChoice.get(quest_num-1));
                                     tv_result4.setTextColor(Color.RED);
                                     break;
                                 case 4 :
                                     tv_result5.setText("오답");
                                     tv_meaningSound5.setText(answerChoice.get(quest_num-1));
                                     tv_result5.setTextColor(Color.RED);
                                     break;
                             }
                             disabledAllButton();

                         }
                         else{
                             btn_back.setVisibility(View.VISIBLE);
                             btn_back.setEnabled(true);
                             btn_replay.setVisibility(View.VISIBLE);
                             btn_replay.setEnabled(true);
                             layout_result.setVisibility(View.VISIBLE);
                             tv_description.setVisibility(View.INVISIBLE);
                             tv_count.setVisibility(View.INVISIBLE);
                             tv_kanji.setVisibility(View.INVISIBLE);
                         }
                     }
                     tv_count.setText(count);
                     if(num==0){
                         tv_count.setText("");
                     }
                     num--;
                 }
             };


             class NewRunnable implements Runnable {
                 @Override
                 public void run() {
                     int num = 10;
                     while (num>=0) {
                         mHandler.sendEmptyMessage(0);
                         try {
                             num--;
                             Thread.sleep(1000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                             break;
                         }
                     }
                 }
             }
             NewRunnable nr = new NewRunnable() ;
             t = new Thread(nr) ;
             t.start() ;
         }
         });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(t!=null){
            t.interrupt();
        }
    }

    private void selectAnswer(int select_num){
        String answerText="";
        switch (select_num){
            case 1 :
                answerText = btn_answer1.getText().toString();
                break;
            case 2:
                answerText = btn_answer2.getText().toString();
                break;
            case 3:
                answerText = btn_answer3.getText().toString();
                break;
            case 4:
                answerText = btn_answer4.getText().toString();
                break;
        }

        //시작하기 버튼을 누르면 quest_num 에 바로 1이 더해지기 때문에 -1을 해서 인덱스 번호를 맞춰야함
        if(answerText.equals(answerChoice.get(quest_num-1))){
            //시간초를 멈추기 위해 스레드 인터럽트
            t.interrupt();
            tv_description.setText("정답입니다");
            tv_description.setTextColor(Color.BLUE);
            switch (quest_num-1){
                case 0 :
                    tv_result1.setText("정답");
                    tv_meaningSound1.setText(answerChoice.get(quest_num-1));
                    tv_result1.setTextColor(Color.BLUE);
                    break;
                case 1 :
                    tv_result2.setText("정답");
                    tv_meaningSound2.setText(answerChoice.get(quest_num-1));
                    tv_result2.setTextColor(Color.BLUE);
                    break;
                case 2 :
                    tv_result3.setText("정답");
                    tv_meaningSound3.setText(answerChoice.get(quest_num-1));
                    tv_result3.setTextColor(Color.BLUE);
                    break;
                case 3 :
                    tv_result4.setText("정답");
                    tv_meaningSound4.setText(answerChoice.get(quest_num-1));
                    tv_result4.setTextColor(Color.BLUE);
                    break;
                case 4 :
                    tv_result5.setText("정답");
                    tv_meaningSound5.setText(answerChoice.get(quest_num-1));
                    tv_result5.setTextColor(Color.BLUE);
                    break;
            }
        }
        else{
            t.interrupt();
            String wrongDescription = "오답입니다\n정답 : "+answerChoice.get(quest_num-1);
            tv_description.setTextColor(Color.RED);
            tv_description.setText(wrongDescription);
            switch (quest_num-1){
                case 0 :
                    tv_result1.setText("오답");
                    tv_meaningSound1.setText(answerChoice.get(quest_num-1));
                    tv_result1.setTextColor(Color.RED);
                    break;
                case 1 :
                    tv_result2.setText("오답");
                    tv_meaningSound2.setText(answerChoice.get(quest_num-1));
                    tv_result2.setTextColor(Color.RED);
                    break;
                case 2 :
                    tv_result3.setText("오답");
                    tv_meaningSound3.setText(answerChoice.get(quest_num-1));
                    tv_result3.setTextColor(Color.RED);
                    break;
                case 3 :
                    tv_result4.setText("오답");
                    tv_meaningSound4.setText(answerChoice.get(quest_num-1));
                    tv_result4.setTextColor(Color.RED);
                    break;
                case 4 :
                    tv_result5.setText("오답");
                    tv_meaningSound5.setText(answerChoice.get(quest_num-1));
                    tv_result5.setTextColor(Color.RED);
                    break;
            }
        }

        if(quest_num<TOTAL_QUESTION) {
            btn_start.setVisibility(View.VISIBLE);
            btn_start.setEnabled(true);
            btn_start.setText("다음 문제");
        }else{
            //마지막 문제의 답을 골랐을 경우 뒤로가기 활성화
            btn_back.setVisibility(View.VISIBLE);
            btn_back.setEnabled(true);
            btn_replay.setVisibility(View.VISIBLE);
            btn_replay.setEnabled(true);
            layout_result.setVisibility(View.VISIBLE);
            tv_description.setVisibility(View.INVISIBLE);
            tv_count.setVisibility(View.INVISIBLE);
            tv_kanji.setVisibility(View.INVISIBLE);
        }
        disabledAllButton();
    }

    private void enabledAllButton(){
        btn_answer1.setEnabled(true);
        btn_answer2.setEnabled(true);
        btn_answer3.setEnabled(true);
        btn_answer4.setEnabled(true);
    }

    private void disabledAllButton(){
        btn_answer1.setEnabled(false);
        btn_answer2.setEnabled(false);
        btn_answer3.setEnabled(false);
        btn_answer4.setEnabled(false);
    }
}