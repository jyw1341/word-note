package com.jyw.mykanjinote;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ExampleListAdapter extends RecyclerView.Adapter<ExampleListAdapter.ContentsViewHolder>{
    private List<ExampleData> exampleData;
    private Context context;
    private String folderName;
    private int listPosition;
    private final int BASIC_LIST_SIZE=8;

    public ExampleListAdapter(Context context,List<ExampleData> exampleData,String folderName, int listPosition){
        this.exampleData = exampleData;
        this.context =context;
        this.folderName = folderName;
        this.listPosition = listPosition;
    }
    @Override
    public ExampleListAdapter.ContentsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.example_list,parent,false);
        return new ContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExampleListAdapter.ContentsViewHolder holder, int position) {
        final ExampleData data = exampleData.get(position);

        String num = Integer.toString(position+1);
        holder.textView_num.setText(num);
        holder.textView_example.setText(data.getExample());
    }

    @Override
    public int getItemCount() {
        return exampleData.size();
    }

    class ContentsViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_num;
        public TextView textView_example;
        public Button btn_delete;

        ContentsViewHolder(View view){
            super(view);

            textView_num = (TextView)view.findViewById(R.id.textView_num);
            textView_example =(TextView)view.findViewById(R.id.textView_example);
            btn_delete = (Button)view.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    Log.i("ExampleListAdapter","삭제 : "+exampleData.get(position).getExample());
                    exampleData.remove(position);
                    notifyDataSetChanged();

                    PreferenceManager pm = new PreferenceManager(context);
                    ArrayList<String> charList = pm.getStringArrayPref(folderName);

                    String charContents = charList.get(listPosition);
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
                    Log.i("ExampleListAdapter","셰어드에서 삭제 : "+charContentsList.get(BASIC_LIST_SIZE+position));
                    charContentsList.remove(BASIC_LIST_SIZE+position);
                    Log.i("ExampleListAdapter","예문이 모두 삭제되었습니다 현재 리스트 사이즈: "+charContentsList.size());
                    JSONArray b = new JSONArray();
                    for (int i = 0; i < charContentsList.size(); i++) {
                        b.put(charContentsList.get(i));
                    }
                    charContents = b.toString();
                    charList.set(listPosition,charContents);
                    pm.setStringArrayPref(folderName,charList);
                }
            });
        }
    }
}
