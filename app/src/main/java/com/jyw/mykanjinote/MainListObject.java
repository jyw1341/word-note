package com.jyw.mykanjinote;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MainListObject{

    Context context;
    ArrayList<String> folderNames;
    List<MainListData> data;

    MainListObject(Context context){
        this.context=context;
        data=new ArrayList<MainListData>();
        PreferenceManager pm = new PreferenceManager(context);
        folderNames = pm.getStringArrayPref("folderList");
        for(int i=0;i<folderNames.size();i++) {
            MainListData mainListData = new MainListData(folderNames.get(i),0);
            data.add(mainListData);
        }
    }

    public List<MainListData> getData(){
        return this.data;
    }

    public void addData(String name){
        MainListData mainListData = new MainListData(name, 0);
        data.add(mainListData);
    }

    public void refresh(){
        //메인액티비티 리스트 새로고침 메소드
        data.clear();
        PreferenceManager pm = new PreferenceManager(context);
        folderNames = pm.getStringArrayPref("folderList");
        //전체 한자 개수 새로고침
        for(int i=0;i<folderNames.size();i++) {
            MainListData mainListData = new MainListData(folderNames.get(i), 0);
            data.add(mainListData);
        }
    }
}
