package com.example.tasimwithyouapp.models;

import java.util.HashMap;

public class StationStatus {
    private HashMap<String,Boolean> stationStatus = new HashMap<>();


    public StationStatus(HashMap<String, Boolean> stationStatus) {
        this.stationStatus = stationStatus;
    }

    public StationStatus(StationStatus other) {
        this.stationStatus = new HashMap<>(other.stationStatus);
    }

    public HashMap<String, Boolean> getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(HashMap<String, Boolean> stationStatus) {
        this.stationStatus = stationStatus;
    }

    public void addStationStatus(String stationId, Boolean status) {
        stationStatus.put(stationId, status);
    }

    public boolean statusComplete(String stationId) {
        stationId = "S" + stationId;
        return stationStatus.containsKey(stationId)
                && stationStatus.get(stationId) == true;
    }

    public StationStatus() {}


}
