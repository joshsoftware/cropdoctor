package com.example.cropdoctor;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



public class Example {

    @SerializedName("main")
    Main main;
    @SerializedName("weather")
    ArrayList<Weather> weather = new ArrayList<Weather>();
    @SerializedName("sys")
     public Sys sys;
    @SerializedName("name")



    public Sys getSys() {
        return sys;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
