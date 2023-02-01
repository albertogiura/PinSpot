package com.blackbox.pinspot.data.source.weather;

public abstract class BaseWeatherRemoteDataSource {
    protected WeatherCallback weatherCallback;

    public void setWeatherCallback(WeatherCallback weatherCallback) {
        this.weatherCallback = weatherCallback;
    }

    public abstract void getWeatherWithLatLng(Double lat, Double lng);
}
