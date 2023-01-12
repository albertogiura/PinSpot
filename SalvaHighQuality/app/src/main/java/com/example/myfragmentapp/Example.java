package com.example.myfragmentapp;

import com.google.gson.annotations.SerializedName;

public class Example {
   @SerializedName("main")
    Main main;








    @SerializedName("weather")
    Weather[] weather;







    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }





}
