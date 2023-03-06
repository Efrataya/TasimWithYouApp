package com.example.tasimwithyouapp;

public class Flight {

    private String id;
    private String userId;
    private String flightNumber;
    private String flightDate;
    private String flightDestination;
    private String terminal;


    public Flight(String id, String userId, String flightNumber, String flightDate, String flightDestination, String terminal) {
        this.id = id;
        this.userId = userId;
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.flightDestination = flightDestination;
        this.terminal = terminal;
    }

    public Flight() {}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }


    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getFlightDestination() {
        return flightDestination;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public void setFlightDestination(String flightDestination) {
        this.flightDestination = flightDestination;

    }
}
