package com.example.myweather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherapi {
    @GET("weather")
    Call<Example> getweather(@Query("lat") Double lat,
                             @Query("lon") Double lon,
                             @Query("appid") String apikey);
}
