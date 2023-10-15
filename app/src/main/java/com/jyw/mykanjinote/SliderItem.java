package com.jyw.mykanjinote;

public class SliderItem {


    private String imageUrl;
    private String stringUri;

    SliderItem(String imageUrl,String stringUri){
        this.imageUrl = imageUrl;
        this.stringUri = stringUri;
    }
    public String getImage(){
        return imageUrl;
    }
    public String getStringUri() {return stringUri;}
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
