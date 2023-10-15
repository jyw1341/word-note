package com.jyw.mykanjinote;

import android.content.Context;
import android.content.Intent;
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

public class NoteListAllAdapter extends RecyclerView.Adapter<NoteListAllAdapter.ContentsViewHolder> {

    List<NoteListData> noteListData;
    private Context context;
    private PreferenceManager pm;
    private String folderName;

    public NoteListAllAdapter(Context context, List<NoteListData> noteListData,String folderName){

        this.noteListData = noteListData;
        this.context =context;
        this.pm = new PreferenceManager(context);
        this.folderName = folderName;
    }

    @Override
    public ContentsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.note_list,parent,false);
        NoteListAllAdapter.ContentsViewHolder contentsViewHolder = new NoteListAllAdapter.ContentsViewHolder(view);
        contentsViewHolder.checkBox_memorized.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final NoteListData data = noteListData.get(contentsViewHolder.getBindingAdapterPosition());
                pm.putBoolean(data.getCharacter(),isChecked);

                if(folderName.equals("미암기")&& isChecked){
                    noteListData.remove(contentsViewHolder.getBindingAdapterPosition());
                    notifyDataSetChanged();
                }
                else if(folderName.equals("암기")&&!isChecked){
                    noteListData.remove(contentsViewHolder.getBindingAdapterPosition());
                    notifyDataSetChanged();
                }
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
                        NoteListData data = noteListData.get(position);
                        String folderName = data.getFolderName();
                        intent.putExtra("folderName",folderName);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                    }
                }
            });
            checkBox_memorized = (CheckBox)view.findViewById(R.id.checkBox_memorized);
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
                        NoteListData data = noteListData.get(position);
                        String folderName = data.getFolderName();
                        PreferenceManager pm = new PreferenceManager(context);
                        //삭제하려는 데이터가 저장되어있는 리스트
                        ArrayList<String> list = pm.getStringArrayPref(folderName);

                        //전체 폴더이름이 저장되어있는 리스트
                        ArrayList<String> folderList = pm.getStringArrayPref("folderList");
                        //삭제하려는 데이터가 있는 폴더의 인덱스를 구하고, 앞선 위치의 폴더들이 갖고 있는 데이터들의 사이즈를 구한다
                        int index = folderList.indexOf(folderName);
                        int num=0;
                        for (int i = 0; i < index; i++){
                            String str = folderList.get(i);
                            ArrayList<String> charList = pm.getStringArrayPref(str);
                            num = num+charList.size();
                        }
                        //num의 총합은 삭제하려는 데이터가 속한 폴더보다 인덱스상 앞에 존재하는 모든 데이터의 합이다.
                        //따라서 현재 포지션에서 num을 빼면 데이터가 실제 속해있는 리스트상에서의 인덱스를 구할 수 있다
                        int realPosition = position-num;

                        list.remove(realPosition);
                        pm.setStringArrayPref(folderName,list);
                        //NoteListObject 의 데이터 리스트에서 선택된 뷰에 해당하는 데이터 삭제
                        noteListData.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
