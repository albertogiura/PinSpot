package com.blackbox.pinspot.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;

@Entity(tableName = "pin_table")
public class Pin implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo()
    public String link;

    @ColumnInfo()
    public String title;

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


    public Pin(double lat, double lon, String geoHash, String title, @NonNull String link) {
        this.lat = lat;
        this.lon = lon;
        this.geoHash = geoHash;
        this.title = title;
        this.link = link;
    }

    public Pin(double lat, double lon, String title, String link) {
        this(lat, lon, GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon)), title, link);
    }


    protected Pin(Parcel in) {
        link = in.readString();
        title = in.readString();
        geoHash = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<Pin> CREATOR = new Creator<Pin>() {
        @Override
        public Pin createFromParcel(Parcel in) {
            return new Pin(in);
        }

        @Override
        public Pin[] newArray(int size) {
            return new Pin[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(link);
        dest.writeString(title);
        dest.writeString(geoHash);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }
}
