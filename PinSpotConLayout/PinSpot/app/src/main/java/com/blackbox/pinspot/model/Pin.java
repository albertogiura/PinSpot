package com.blackbox.pinspot.model;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;

public class Pin {
    private double lat, lon;
    private String geoHash;
    private String title, link;
    private int likes;
    // to do aggiungere linke altre robe utili
    // constructors

    public Pin() {
       /* this.position = new LatitudeLongitude(0.0, 0.0);
        this.title = "";
        this.link = "";
        this.likes = 0;*/
    }


    public Pin(double lat, double lon, String geoHash, String title, String link, int likes) {
        this.lat = lat;
        this.lon = lon;
        this.geoHash = geoHash;
        this.title = title;
        this.link = link;
        this.likes = likes;
    }

    public Pin(double lat, double lon, String title, String link, int likes) {
        this(lat, lon, GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon)), title, link, likes);
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

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
