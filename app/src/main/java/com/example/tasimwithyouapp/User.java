package com.example.tasimwithyouapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class User implements Serializable {
    public static User currentUser;
    private List<Flight> flights = new ArrayList<>();
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

    public void add(Flight flight) {
        flights.add(flight);
    }

    public void removeAllFlight() {
        flights.clear();
    }

    public List<Flight> allFlights() {
        return flights;
    }
}
