package com.jyw.mykanjinote;

public class MainListData implements Comparable<MainListData>{
    private String folderName;
    private int num;

    MainListData(String folderName, int num){
        this.folderName = folderName;
        this.num=num;
    }

    public String getFolderName(){
        return this.folderName;
    }

    public void changeFolderName(String str){
        this.folderName = str;
    }

    public int getNum(){
        return this.num;
    }

    public void setNum(int num){
        this.num = num;
    }

    @Override
    public int compareTo(MainListData o) {
        return 0;
    }
}
