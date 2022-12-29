package com.example.testfirebase.User;

import java.util.Objects;

public class LatitudeLongitude {

    private Double lat;
    private Double lon;

    //constructors

    public LatitudeLongitude(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public LatitudeLongitude() {
        new LatitudeLongitude(0.0, 0.0);
    }

    // equals() & toString()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatitudeLongitude that = (LatitudeLongitude) o;
        return Objects.equals(lat, that.lat) && Objects.equals(lon, that.lon);
    }

    @Override
    public String toString() {
        return "LatitudeLongitude{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    // getter and setter

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
