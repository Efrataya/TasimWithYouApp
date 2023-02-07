package com.example.tasimwithyouapp;

public class User {
    public String id;
    public String name;
    public String address;
    public String email;
    public String password;
    public String flightNumber;
    public String flightDate;
    public String flightHour;
    public String flightDestination;

    public User(String id, String name, String address, String email, String password, String flightNumber, String flightDate, String flightHour, String flightDestination) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.flightHour = flightHour;
        this.flightDestination = flightDestination;
    }

    public User() {
    }
}
