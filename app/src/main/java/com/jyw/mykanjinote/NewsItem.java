package com.jyw.mykanjinote;

import java.io.Serializable;

public class NewsItem implements Serializable {

    private String title;
    private String urlToImage;
    private String content;

    NewsItem(String title, String urlToImage, String content){
        this.title = title;
        this.urlToImage =urlToImage;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getContent() {
        return content;
    }
}
