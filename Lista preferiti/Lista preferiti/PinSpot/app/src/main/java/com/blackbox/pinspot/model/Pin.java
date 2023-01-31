package com.blackbox.pinspot.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;

import java.util.Objects;

@Entity(tableName = "Pin_table")
public class Pin {

    // constructors
    @PrimaryKey
    @NonNull
    @ColumnInfo()
    public String link;

    @ColumnInfo()
    public String title;

    @ColumnInfo()
    public int likes;

    @ColumnInfo()
    public String geoHash;

    @ColumnInfo()
    public double lat;

    @ColumnInfo()
    public double lon;

    public Pin() {
       /* this.position = new LatitudeLongitude(0.0, 0.0);
        this.title = "";
        this.link = "";
        this.likes = 0;*/
    }


    public Pin(double lat, double lon, String geoHash, String title,@NonNull String link, int likes) {
        this.lat = lat;
        this.lon = lon;
        this.geoHash = geoHash;
        this.title = title;
        this.link = link;
        this.likes = likes;
    }

    public Pin(double lat, double lon, String title, String link, int likes) {
        this.lat = lat;
        this.lon = lon;
        this.geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon));
        this.title = title;
        this.link = link;
        this.likes = likes;
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
