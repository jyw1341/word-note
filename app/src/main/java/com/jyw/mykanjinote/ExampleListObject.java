package com.jyw.mykanjinote;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ExampleListObject {
    Context context;
    ArrayList<String> charList;
    List<ExampleData> exampleDataList;
    ArrayList<String> charContentsList;
    PreferenceManager pm;
    String folderName;

    private final int BASIC_LIST_SIZE=8;

    ExampleListObject(Context context, String folderName, int position){
        pm = new PreferenceManager(context);
        this.context=context;
        this.folderName = folderName;
        exampleDataList=new ArrayList<ExampleData>();
        //셰어드에 있는 한자 정보 스트링에 저장
        charList = pm.getStringArrayPref(folderName);
        String charContents = charList.get(position);
        charContentsList = new ArrayList<String>();
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

        //인덱스 0~7까지 채워져있음
        for(int i=0;i<charContentsList.size()-BASIC_LIST_SIZE;i++) {
            ExampleData exampleData = new ExampleData(charContentsList.get(i+BASIC_LIST_SIZE));
            exampleDataList.add(exampleData);
            Log.i("ExampleListObject","example add : "+exampleData.getExample());
        }
    }
    public List<ExampleData> getDataList(){return exampleDataList;}

    public void addExample(String example,int position){
        ExampleData exampleData = new ExampleData(example);
        exampleDataList.add(exampleData);

        charContentsList.add(example);
        JSONArray a = new JSONArray();
        for (int i = 0; i < charContentsList.size(); i++) {
            a.put(charContentsList.get(i));
        }
        Log.i("ExampleListObject","예문을 추가했습니다 현재 리스트 사이즈 : "+example+"/"+charContentsList.size());
        String charContents = a.toString();
        charList.set(position,charContents);
        pm.setStringArrayPref(folderName,charList);
    }
}
