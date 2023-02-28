package com.example.tasimwithyouapp;

public class Flight {

    private String id;
    private String flightNumber;
    private long flightDate;
    private String flightDestination;


    public Flight(String flightNumber, long flightDate, String flightDestination) {
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.flightDestination = flightDestination;
    }

    public Flight() {}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }


    public long getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(long flightDate) {
        this.flightDate = flightDate;
    }

    public String getFlightDestination() {
        return flightDestination;
    }

    public void setFlightDestination(String flightDestination) {
        this.flightDestination = flightDestination;
    }
}
