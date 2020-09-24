package com.example.cropdoctor;

import java.util.ArrayList;

public class Application {
    //Coord CoordObject;
    ArrayList<Object> weather = new ArrayList<Object>();
    private String base;
    Main MainObject;
    private float visibility;
    Wind WindObject;
  //  Clouds CloudsObject;
    private float dt;
    Sys SysObject;
    Weather wea;
    private float id;

  /*  public Coord getCoordObject() {
        return CoordObject;
    }

    public void setCoordObject(Coord coordObject) {
        CoordObject = coordObject;
    }*/

    public ArrayList<Object> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Object> weather) {
        this.weather = weather;
    }

    public Main getMainObject() {
        return MainObject;
    }

    public void setMainObject(Main mainObject) {
        MainObject = mainObject;
    }

    public Wind getWindObject() {
        return WindObject;
    }

    public void setWindObject(Wind windObject) {
        WindObject = windObject;
    }

   /* public Clouds getCloudsObject() {
        return CloudsObject;
    }

    public void setCloudsObject(Clouds cloudsObject) {
        CloudsObject = cloudsObject;
    }*/

    public Sys getSysObject() {
        return SysObject;
    }

    public void setSysObject(Sys sysObject) {
        SysObject = sysObject;
    }

    public Weather getWea() {
        return wea;
    }

    public void setWea(Weather wea) {
        this.wea = wea;
    }

    private String name;
    private float cod;


    // Getter Methods

   // public Coord getCoord() {
     //   return CoordObject;
    //}

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return MainObject;
    }

    public float getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return WindObject;
    }

   // public Clouds getClouds() {
     //   return CloudsObject;
    //}

    public float getDt() {
        return dt;
    }

    public Sys getSys() {
        return SysObject;
    }

    public float getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getCod() {
        return cod;
    }

    // Setter Methods

   /* public void setCoord( Coord coordObject ) {
        this.CoordObject = coordObject;
    }*/

    public void setBase( String base ) {
        this.base = base;
    }

    public void setMain( Main mainObject ) {
        this.MainObject = mainObject;
    }

    public void setVisibility( float visibility ) {
        this.visibility = visibility;
    }

    public void setWind( Wind windObject ) {
        this.WindObject = windObject;
    }

   /* public void setClouds( Clouds cloudsObject ) {
        this.CloudsObject = cloudsObject;
    }*/

    public void setDt( float dt ) {
        this.dt = dt;
    }

    public void setSys( Sys sysObject ) {
        this.SysObject = sysObject;
    }

    public void setId( float id ) {
        this.id = id;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setCod( float cod ) {
        this.cod = cod;
    }
}