package com.example.myfragmentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

    private String placeName;
    private String address;
    private String city;

    public Place(String placeName, String address, String city) {
        this.placeName = placeName;
        this.address = address;
        this.city = city;
    }

    protected Place(Parcel in) {
        this.placeName = in.readString();
        this.address = in.readString();
        this.city = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.placeName);
        parcel.writeString(this.address);
        parcel.writeString(this.city);
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public String toString() {
        return "Place{" +
                "placeName='" + placeName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
