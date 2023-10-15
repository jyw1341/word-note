package com.jyw.mykanjinote;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NoteListObject {
    Context context;
    ArrayList<String> charList;
    List<NoteListData> data;



    NoteListObject(Context context, String folderName){
        PreferenceManager pm = new PreferenceManager(context);
        this.context=context;
        data=new ArrayList<NoteListData>();
        charList = pm.getStringArrayPref(folderName);
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
            data.add(noteListData);
        }
    }

    NoteListObject(Context context,ArrayList<String> charList,String folderName){
        PreferenceManager pm = new PreferenceManager(context);
        this.context = context;
        this.charList = charList;
        this.data=new ArrayList<NoteListData>();

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

            if(folderName.equals("전체")) {
                NoteListData noteListData = new NoteListData(charContentsList.get(0), charContentsList.get(1), charContentsList.get(2), charContentsList.get(3), charContentsList.get(4), charContentsList.get(7));
                data.add(noteListData);
            }
            else if(folderName.equals("미암기")){

                if(!pm.getBoolean(charContentsList.get(0))){
                    NoteListData noteListData = new NoteListData(charContentsList.get(0), charContentsList.get(1), charContentsList.get(2), charContentsList.get(3), charContentsList.get(4), charContentsList.get(7));
                    data.add(noteListData);
                }
            }
            else if(folderName.equals("암기")){
                if(pm.getBoolean(charContentsList.get(0))){
                    NoteListData noteListData = new NoteListData(charContentsList.get(0), charContentsList.get(1), charContentsList.get(2), charContentsList.get(3), charContentsList.get(4), charContentsList.get(7));
                    data.add(noteListData);
                }
            }
        }
        Log.i("NoteListObject",": data load complete");
    }

    public List<NoteListData> getData(){
        return this.data;
    }

    public int getDataSize(){return  this.data.size();}
}
