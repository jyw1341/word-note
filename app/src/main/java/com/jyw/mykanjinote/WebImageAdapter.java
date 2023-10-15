package com.jyw.mykanjinote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WebImageAdapter extends RecyclerView.Adapter<WebImageAdapter.WebViewHolder> {
    private List<WebItem> webItems;
    private Context context;

    public WebImageAdapter(Context context,List<WebItem> webItemList){
        this.webItems=webItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public WebImageAdapter.WebViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.web_item_container,
                parent,
                false);

        WebImageAdapter.WebViewHolder webViewHolder = new WebImageAdapter.WebViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CharacterActivity.class);
                String str = webItems.get(webViewHolder.getBindingAdapterPosition()).getString();
                intent.putExtra("stringUri",str);
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                ((Activity) context).finish();
            }
        });
        return webViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WebImageAdapter.WebViewHolder holder, int position) {
        holder.setImage(webItems.get(position));
    }

    @Override
    public int getItemCount() {
        return webItems.size();
    }

    protected static class WebViewHolder extends RecyclerView.ViewHolder{
        private ImageView rc_item;
        WebViewHolder(@NonNull View itemView){
            super(itemView);
            rc_item = itemView.findViewById(R.id.rc_item);
        }
        void setImage(WebItem webItem){
            Picasso.get().load(webItem.getUri()).resize(1080,720).onlyScaleDown().into(rc_item);
        }
    }
}
