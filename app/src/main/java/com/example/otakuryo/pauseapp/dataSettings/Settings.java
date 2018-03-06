package com.example.otakuryo.pauseapp.dataSettings;

/**
 * Created by Otakuryo on 14/01/2018.
 */

public class Settings {
    int img_settings;
    String title_settings;
    String des_settings;
    Boolean switch_isVisible = false;
    boolean switch_position = false;

    public Settings(int img_settings,String title_settings,String des_settings,boolean switch_isVisible){
        this.img_settings=img_settings;
        this.title_settings=title_settings;
        this.des_settings=des_settings;
        this.switch_isVisible=switch_isVisible;
    }

    public void settitle_settings(String title_settings){this.title_settings=title_settings;}

    public int getImg_settings(){
        return img_settings;
    }
    public String gettitle_settings(){
        return title_settings;
    }
    public String getdes_settings(){
        return des_settings;
    }

    public Boolean getSwitch_isVisible() {
        return switch_isVisible;
    }
    public boolean getSwitch_position(){return switch_position;}

    public void setSwitch_position(boolean switch_position) {
        this.switch_position = switch_position;
    }
}
