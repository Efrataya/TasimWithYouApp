package com.example.tasimwithyouapp;

public class User {
    public String id;
    public String name;
    public String address;
    public String email;
    public String password;


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
