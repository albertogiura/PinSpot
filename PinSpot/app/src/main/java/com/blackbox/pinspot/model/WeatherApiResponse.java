package com.blackbox.pinspot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// This class represents the Weather API response to be converted using Gson

public class WeatherApiResponse {

    public WeatherApiResponse() {}

    @SerializedName("main")
    MainWeatherInfo mainWeatherInfo;

    @SerializedName("weather")
    Weather[] weather;

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public MainWeatherInfo getMainWeatherInfo() {
        return mainWeatherInfo;
    }

    public void setMainWeatherInfo(MainWeatherInfo mainWeatherInfo) {
        this.mainWeatherInfo = mainWeatherInfo;
    }

    public static final class MainWeatherInfo {
        @SerializedName("temp")
        @Expose
        private Double temp;

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }
    }

    public static final class Weather {
        @SerializedName("description")
        @Expose
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
