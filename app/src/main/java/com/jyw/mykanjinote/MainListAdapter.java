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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainListAdapter extends BaseAdapter {

    private static final String TAG = MainListAdapter.class.getSimpleName();
    List<MainListData> folderName;
    Context context;
    LayoutInflater layoutInflater;

    Set<View> viewSet;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public MainListAdapter(Context context, List<MainListData> folderName){
        this.context = context;
        this.folderName = folderName;
        viewSet = new ArraySet<View>();
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
            view=layoutInflater.inflate(R.layout.main_list,null);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.textView_folderName= ((TextView)view.findViewById(R.id.textView_folderName));
            view.setTag(mainViewHolder);
        }
        else{
            mainViewHolder = (MainViewHolder)view.getTag();
        }

        final MainListData mainListData = folderName.get(position);

        PreferenceManager pm = new PreferenceManager(context);
        String str = mainListData.getFolderName();
        ArrayList<String> list = pm.getStringArrayPref(str);
        int num = list.size();

        mainViewHolder.textView_num = ((TextView)view.findViewById(R.id.textView_num));


        mainViewHolder.textView_folderName.setText(mainListData.getFolderName());
        mainViewHolder.textView_num.setText(Integer.toString(num));

        viewSet.add(view);

        Log.i(TAG,"Index : "+position+" : "+view+" Size "+viewSet.size());
        return view;
    }

    private static class MainViewHolder{
        public TextView textView_folderName;
        public TextView textView_num;
    }
}
