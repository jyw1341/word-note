package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class WebImageActivity extends AppCompatActivity {
    private static final String TAG = WebImageActivity.class.getSimpleName();

    private RecyclerView rv_web;


    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_image);

        Intent dataIntent = getIntent();
        ArrayList<String> linkList = dataIntent.getStringArrayListExtra("linkList");

        List<WebItem> webItemList = new ArrayList<>();
        for (int i = 0; i < linkList.size(); i++){
            webItemList.add(new WebItem(linkList.get(i)));
        }

        rv_web = findViewById(R.id.rv_web);
        gridLayoutManager = new GridLayoutManager(this,3);
        rv_web.setLayoutManager(gridLayoutManager);
        rv_web.setAdapter(new WebImageAdapter(this,webItemList));
    }
}