package com.blackbox.pinspot.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.annotations.NotNull;

@Entity
public class Favourties {


    @NonNull
    @PrimaryKey(autoGenerate = false)
    String geoHash;

    @ColumnInfo(name = "latitude")
    public double lat;

    @ColumnInfo(name = "longitude")
    public double lon;

    @ColumnInfo()
    public String link;

    @ColumnInfo()
    public String title;

    @ColumnInfo()
    public int likes;

    public Favourties(String geoHash, double lat, double lon, String link, String title, int likes) {
        this.geoHash = geoHash;
        this.lat = lat;
        this.lon = lon;
        this.link = link;
        this.title = title;
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Favourties{" +
                "geoHash='" + geoHash + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", likes=" + likes +
                '}';
    }
}
