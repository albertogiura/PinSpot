package com.example.myfragmentapp;

import java.util.Objects;

public class Place {
    private  String name;
    private Double lat;
    private Double lon;

    public Place() {
        this.name = "Null";
        this.lat = 0.0;
        this.lon = 0.0;
    }
    public Place(String name) {
        this.name = name;
        this.lat = 0.0;
        this.lon = 0.0;
    }
    public Place(String name, Double lat, Double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(name, place.name) && Objects.equals(lat, place.lat) && Objects.equals(lon, place.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lat, lon);
    }
}
