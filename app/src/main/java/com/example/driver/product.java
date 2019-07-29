package com.example.driver;

import android.media.Image;

public class product {

    private  String name;
    private  String nationality;
    private  int pic;


    public product(String iname, String inationality, int imageId){

        name = iname;
        nationality = inationality;
        pic = imageId;
    }

    public String getname(){return name;}

    public String getnationality(){return nationality;}

    public int getpic(){return pic;}
}