package com.example.tasimwithyouapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {

    private List<Flight> flights = new ArrayList<>();

    private HashMap<String, List<Long>> alerts = new HashMap<>();

    public String id;
    public String name;
    public String address;
    public String email;
    public String password;
    public Flight currentFlight;

    private HashMap<String, StationStatus> stationStatus = new HashMap<>();

    public User(String id,
                String name,
                String address,
                String email,
                String password,
                HashMap<String, List<Long>> alerts,
                HashMap<String, StationStatus> stationStatus) {
        this.id = id;
        this.name = name;
        this.alerts = alerts;
        this.address = address;
        this.email = email;
        this.password = password;
        this.stationStatus = stationStatus;
    }

    public User(User other) {
        this.id = other.id;
        this.name = other.name;
        this.address = other.address;
        this.email = other.email;
        this.password = other.password;
        this.currentFlight = other.currentFlight;
        this.alerts = new HashMap<>(other.alerts);
        this.stationStatus = new HashMap<>(other.stationStatus);
    }


    public HashMap<String, StationStatus> getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(HashMap<String, StationStatus> stationStatus) {
        this.stationStatus = stationStatus;
    }

    public void markStationComplete(String stationId) {
        try {
            String flightId = currentFlight.getFlightNumber();
            if (!stationStatus.containsKey(currentFlight.getFlightNumber())) {
                StationStatus status = new StationStatus();
                status.addStationStatus(stationId, true);
                stationStatus.put(flightId, status);
                return;
            }
            stationStatus.get(flightId)
                    .addStationStatus(stationId, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStationComplete(String stationId) {
        try {
            if (currentFlight == null) return false;
            String flightId = currentFlight.getFlightNumber();
            return stationStatus.containsKey(flightId)
                    && stationStatus.get(flightId).statusComplete(stationId);
        } catch (Exception e) {
            return false;
        }
    }

    public void addAlertTimes(ScheduelingType alertType, List<Long> alert) {
        alerts.put(alertType.getValue(), alert);
    }

    public boolean hasAlert(ScheduelingType alertType) {
        return alerts.containsKey(alertType.getValue());
    }

    public boolean hasAlertTime(ScheduelingType alertType, Long alert) {
        if (!hasAlert(alertType)) return false;
        return alerts.get(alertType.getValue()).contains(alert);
    }

    public boolean hasCustomNotification(ScheduelingType type) {
        if (!hasAlert(type)) return false;
        List<Long> list = alerts.get(type.getValue());
        if (list == null || list.size() == 0) return false;
        for (Long value : list) {
            if (value != ScheduelingType.ONE_DAY
                    && value != ScheduelingType.THREE_HOURS
                    && value != ScheduelingType.FIVE_HOURS
                    && value != ScheduelingType.ONE_MINUTE
                    && value != ScheduelingType.TWO_HOURS)
                return true;
        }
        return false;
    }

    public void addAlertTime(ScheduelingType type, Long alert) {
        if (hasAlert(type)) {
            alerts.get(type.getValue()).add(alert);
        } else {
            List<Long> list = new ArrayList<>();
            list.add(alert);
            alerts.put(type.getValue(), list);
        }
    }

    public HashMap<String, List<Long>> getAlerts() {
        return alerts;
    }

    public void setAlerts(HashMap<String, List<Long>> alerts) {
        this.alerts = alerts;
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

    public void removeAlertTime(ScheduelingType scheduelingType, long minute_milies) {
        if (hasAlert(scheduelingType)) {
            alerts.get(scheduelingType.getValue()).remove(minute_milies);
        }
    }

    public List<Long> getAlertTimes(ScheduelingType scheduelingType) {
        if (hasAlert(scheduelingType)) {
            return alerts.get(scheduelingType.getValue());
        }
        return new ArrayList<>();
    }
}
