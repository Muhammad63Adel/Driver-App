package com.example.driver;


public class Driver {
    private String email ;
    private String name ;

    public Driver(){

    }

    public Driver(String email ,String name) {

        this.email = email;
        this.name = name;

    }

    String getemail(){return email;}
    String getname(){return name;}
}
