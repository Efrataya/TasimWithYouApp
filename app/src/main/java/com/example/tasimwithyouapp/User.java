package com.example.tasimwithyouapp;

import java.io.Serializable;

public class User implements Serializable {
    public static User currentUser;
    public String id;
    public String name;
    public String address;
    public String email;
    public String password;
    public Flight currentFlight;


    public User(String id, String name, String address, String email, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;

    }

    public User() {
    }
}
