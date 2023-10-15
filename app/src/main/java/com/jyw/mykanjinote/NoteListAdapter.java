package com.jyw.mykanjinote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ContentsViewHolder> {

     List<NoteListData> noteListData;
    private Context context;
    private String folderName;
    private PreferenceManager pm;

    public NoteListAdapter(Context context, List<NoteListData> noteListData,String folderName){

        this.noteListData = noteListData;
        this.context =context;
        this.folderName = folderName;
        this.pm = new PreferenceManager(context);
    }
    @Override
    public ContentsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.note_list,parent,false);
        ContentsViewHolder contentsViewHolder = new ContentsViewHolder(view);
        contentsViewHolder.checkBox_memorized.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final NoteListData data = noteListData.get(contentsViewHolder.getBindingAdapterPosition());
                pm.putBoolean(data.getCharacter(),isChecked);


                Log.i("NoteListAdapter","체크박스"+isChecked);
                Log.i("NoteListAdapter","체크박스 셰어드 상태"+pm.getBoolean(data.getCharacter()));
            }
        });
        return contentsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContentsViewHolder holder, int position) {

        final NoteListData data = noteListData.get(position);

        holder.textView_char.setText(data.getCharacter());
        holder.textView_meaning.setText(data.getMeaning());
        holder.textView_sound.setText(data.getSound());
        holder.textView_memorize.setText(data.getMemorize());

        boolean isChecked = pm.getBoolean(data.getCharacter());
        holder.checkBox_memorized.setChecked(isChecked);

        String str = data.getTime();
        Long now = Long.parseLong(str);
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = "저장일 "+simpleDate.format(mDate);
        holder.textView_time.setText(getTime);
    }

    @Override
    public int getItemCount() {
        return noteListData.size();
    }

    class ContentsViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_char;
        public TextView textView_meaning;
        public TextView textView_sound;
        public TextView textView_memorize;
        public TextView textView_time;
        public ImageButton imageButton_delete;
        public CheckBox checkBox_memorized;

        ContentsViewHolder(View view){
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context,CharacterActivity.class);
                        intent.putExtra("folderName",folderName);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                    }
                }
            });

            textView_char=(TextView)view.findViewById(R.id.textView_char);
            textView_meaning=(TextView)view.findViewById(R.id.textView_meaning);
            textView_sound=(TextView)view.findViewById(R.id.textView_sound);
            textView_memorize=(TextView)view.findViewById(R.id.textView_memorize);
            textView_time = (TextView)view.findViewById(R.id.textView_time);
            imageButton_delete = (ImageButton)view.findViewById(R.id.imageButton_delete);
            imageButton_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        PreferenceManager pm = new PreferenceManager(context);
                        ArrayList<String> list = pm.getStringArrayPref(folderName);
                        //셰어드에 저장된 리스트에서 선택된 데이터 삭제
                        list.remove(position);
                        pm.setStringArrayPref(folderName,list);
                        //NoteListObject 의 데이터 리스트에서 선택된 뷰에 해당하는 데이터 삭제
                        noteListData.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

            checkBox_memorized = (CheckBox)view.findViewById(R.id.checkBox_memorized);
        }
    }
}


