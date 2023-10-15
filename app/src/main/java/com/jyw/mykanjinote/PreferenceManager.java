package com.jyw.mykanjinote;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    PreferenceManager(Context context){
        this.context = context;
        pref =context.getSharedPreferences("files", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setStringArrayPref(String key, ArrayList<String> values) {
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public ArrayList<String> getStringArrayPref(String key) {

        String json = pref.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public int getInt(String key){
        return pref.getInt(key,0);
    }

    public void putInt(String key,int num){
        editor.putInt(key, num);
        editor.apply();
    }

    public void putBoolean(String key,boolean bool){
        editor.putBoolean(key, bool);
        editor.apply();
    }

    public boolean getBoolean(String key){
        return pref.getBoolean(key,false);
    }
}
