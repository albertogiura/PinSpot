package com.blackbox.myuploadpicapp.model.user;

import java.util.Objects;

public class Marker {
    private  double lat;
    private double lon;
    private String title;
    private String idPin;

    public Marker(double lat, double lon, String title, String idPin) {
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.idPin = idPin;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", title='" + title + '\'' +
                ", idPin='" + idPin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marker marker = (Marker) o;
        return Double.compare(marker.lat, lat) == 0 && Double.compare(marker.lon, lon) == 0 && Objects.equals(title, marker.title) && Objects.equals(idPin, marker.idPin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, title, idPin);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdPin() {
        return idPin;
    }

    public void setIdPin(String idPin) {
        this.idPin = idPin;
    }
}
