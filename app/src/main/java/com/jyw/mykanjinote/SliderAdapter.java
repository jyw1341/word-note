package com.jyw.mykanjinote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2, Context context){
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    public void clearBanner(){
        sliderItems.clear();
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.slide_item_container,
                parent,
                false);
        SliderAdapter.SliderViewHolder sliderViewHolder = new SliderAdapter.SliderViewHolder(view);
        sliderViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SliderItem item = sliderItems.get(sliderViewHolder.getBindingAdapterPosition());
                String uri = item.getStringUri();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return sliderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  SliderAdapter.SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
        if(position == sliderItems.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SliderItem sliderItem){
            Picasso.get().load(sliderItem.getImage()).resize(1080,720).onlyScaleDown().into(imageView);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            Log.w("SliderAdapter","아이템 숫자"+sliderItems.size());
            notifyDataSetChanged();
        }
    };
}
