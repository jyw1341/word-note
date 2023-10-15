package com.jyw.mykanjinote;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private Context context;

    public NewsAdapter(List<NewsItem> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_container,
                parent,
                false);
        NewsAdapter.NewsViewHolder newsViewHolder = new NewsAdapter.NewsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewsPageActivity.class);
                intent.putExtra("title",newsList.get(newsViewHolder.getBindingAdapterPosition()).getTitle());
                intent.putExtra("content",newsList.get(newsViewHolder.getBindingAdapterPosition()).getContent());
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        NewsItem news = newsList.get(position);
        holder.tv_news_title.setText(news.getTitle());
        holder.setImage(news);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    protected static class NewsViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_news;
        private TextView tv_news_title;
        NewsViewHolder(@NonNull View view){
            super(view);
            iv_news = view.findViewById(R.id.iv_news);
            tv_news_title = view.findViewById(R.id.tv_news_title);
        }

        void setImage(NewsItem item){
            Picasso.get().load(item.getUrlToImage()).resize(1080,720).onlyScaleDown().into(iv_news);
        }
    }
}