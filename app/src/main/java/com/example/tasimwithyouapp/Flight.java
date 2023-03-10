package com.example.tasimwithyouapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Flight implements Serializable {
    static public Flight temp;
    static private ArrayList<Flight> flights = new ArrayList<>();
    static public void add(Flight flight) {
        flights.add(flight);
    }
    static Flight getFlight(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    private String userId;
    private String flightNumber;
    private String flightDate;
    private String flightDestination;
    private String terminal;
    private String airline;
    private String arrivalDate;


    public Flight(String userId, String flightNumber, String flightDate,String arrivalDate, String flightDestination, String terminal, String airline) {
        this.userId = userId;
        this.flightNumber = flightNumber;
        this.arrivalDate = arrivalDate;
        this.flightDate = flightDate;
        this.flightDestination = flightDestination;
        this.terminal = terminal;
        this.airline = airline;
    }

    public Flight() {}


    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
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

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
    public void setFlightDestination(String flightDestination) {
        this.flightDestination = flightDestination;
    }

    @Override
    public String toString() {
        return "FN=" + flightNumber +
                ", FD=" + flightDestination;
    }
}
