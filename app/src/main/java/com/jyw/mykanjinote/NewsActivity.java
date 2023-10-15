package com.jyw.mykanjinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = NewsActivity.class.getSimpleName();
    private RecyclerView rc_news;
    private NewsAdapter newsAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<NewsItem> newsItem;
    RequestQueue queue;

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                finish();
             }
         });

        rc_news = findViewById(R.id.rc_news);
        mLayoutManager = new LinearLayoutManager(this);
        rc_news.setLayoutManager(mLayoutManager);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        getNews();

    }
        public void getNews(){
        String url ="";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arrayArticles = jsonObject.getJSONArray("articles");
                            newsItem = new ArrayList<>();
                            for (int i = 0; i < arrayArticles.length(); i++){
                                JSONObject objArticle = arrayArticles.getJSONObject(i);
                                if(!objArticle.getString("urlToImage").equals("null") && !objArticle.getString("description").equals("null")){
                                    newsItem.add(new NewsItem(objArticle.getString("title"),
                                            objArticle.getString("urlToImage"),
                                            objArticle.getString("description")
                                    ));
                                }
                            }
                            newsAdapter = new NewsAdapter(newsItem,getApplicationContext());
                            rc_news.setAdapter(newsAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            })
        { @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("User-Agent", "Mozilla/5.0");
            return headers;
        }};
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}