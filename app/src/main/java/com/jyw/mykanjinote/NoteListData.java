package com.jyw.mykanjinote;

import java.util.ArrayList;

public class NoteListData {
    private String character;
    private String meaning;
    private String sound;
    private String memorize;
    private String time;
    private String folderName;
    private ArrayList<String> list = new ArrayList<String>();

    NoteListData(String character, String meaning, String sound, String memorize, String time,String folderName){
        this.character = character;
        this.meaning = meaning;
        this.sound = sound;
        this.memorize = memorize;
        this.time = time;
        this.folderName=folderName;
        this.list.add(character);
        this.list.add(meaning);
        this.list.add(sound);
        this.list.add(memorize);
    }

    public String getCharacter(){
        return this.character;
    }
    public String getMeaning(){
        return this.meaning;
    }
    public String getSound(){
        return this.sound;
    }
    public String getMemorize(){
        return this.memorize;
    }
    public ArrayList<String> getList() { return list;}
    public String getTime(){
        return this.time;
    }
    public String getFolderName(){return this.folderName;}
}
