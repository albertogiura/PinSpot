package com.example.myweather;

import com.google.gson.annotations.SerializedName;

public class Example {
   @SerializedName("main")
    Main main;



    @SerializedName("wind")
    Wind wind;




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


    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }





}
