package com.jyw.mykanjinote;

import android.content.Context;
import android.os.Build;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Set;

public class SettingListAdapter extends BaseAdapter {

    private static final String TAG = MainListAdapter.class.getSimpleName();
    List<MainListData> folderName;
    Context context;
    LayoutInflater layoutInflater;
    PreferenceManager pm;
    Set<View> viewSet;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SettingListAdapter(Context context, List<MainListData> folderName){
        this.context = context;
        this.folderName = folderName;
        viewSet = new ArraySet<View>();
        pm = new PreferenceManager(context);
    }

    @Override
    public int getCount() {
        return folderName.size();
    }

    @Override
    public Object getItem(int position) {
        return folderName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        MainViewHolder mainViewHolder;

        if(view==null) {
            layoutInflater = LayoutInflater.from((this.context));
            view=layoutInflater.inflate(R.layout.folder_setting,null);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.textView_folderName= ((TextView)view.findViewById(R.id.textView_folderName));
            mainViewHolder.textView_num = ((TextView)view.findViewById(R.id.textView_num));

            view.setTag(mainViewHolder);
        }
        else{
            mainViewHolder = (MainViewHolder)view.getTag();
        }

        final MainListData mainListData = folderName.get(position);

        mainViewHolder.textView_folderName.setText(mainListData.getFolderName());
        String str = Integer.toString(pm.getStringArrayPref(mainListData.getFolderName()).size());
        mainViewHolder.textView_num.setText(str);

        viewSet.add(view);

        Log.i(TAG,"Index : "+position+" : "+view+" Size "+viewSet.size());
        return view;
    }

    private static class MainViewHolder{
        public TextView textView_folderName;
        public TextView textView_num;
    }
}
