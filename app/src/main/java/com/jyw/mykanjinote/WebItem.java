package com.jyw.mykanjinote;

import android.net.Uri;

public class WebItem {
    private String imageUri;

    WebItem(String imageUri){
        this.imageUri = imageUri;
    }

    public Uri getUri(){
        return Uri.parse(imageUri);
    }

    public String getString(){
        return imageUri;
    }
}
