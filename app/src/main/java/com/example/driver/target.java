package com.example.driver;

public class target {


    private String latitude;
    private String longitude;
    private Driver driver;


    target(){

    }

    target(String latitude, String longitude,Driver driver){

        this.latitude = latitude;
        this.longitude = longitude;
        this.driver = driver;
    }


    public String getLatitude(){return latitude;}
    public String getLongitude(){return longitude;}
    public Driver getDriver(){return driver;}

}
