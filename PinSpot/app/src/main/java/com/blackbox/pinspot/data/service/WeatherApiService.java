package com.blackbox.pinspot.data.service;

import com.blackbox.pinspot.model.WeatherApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("weather")
    Call<WeatherApiResponse> getWeather(@Query("lat") Double lat,
                                        @Query("lon") Double lon,
                                        @Query("appid") String apikey);
}
