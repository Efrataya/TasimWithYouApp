package com.example.tasimwithyouapp.models;

import java.util.List;

public class CachedFlights {
    private List<Flight> cachedFlights;
    public CachedFlights(List<Flight> cachedFlights) {
        this.cachedFlights = cachedFlights;
    }
    public CachedFlights() {
    }
    public List<Flight> getCachedFlights() {
        return cachedFlights;
    }
    public void setCachedFlights(List<Flight> cachedFlights) {
        this.cachedFlights = cachedFlights;
    }
}
