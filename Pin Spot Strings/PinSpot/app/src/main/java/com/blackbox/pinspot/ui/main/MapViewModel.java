package com.blackbox.pinspot.ui.main;

import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {
    private Double LastLat = 0.0;
    private Double LastLon = 0.0;

    public Double getLastLat() {
        return LastLat;
    }

    public Double getLastLon() {
        return LastLon;
    }

    public void setLastLat(Double lastLat) {
        LastLat = lastLat;
    }

    public void setLastLon(Double lastLon) {
        LastLon = lastLon;
    }
}
