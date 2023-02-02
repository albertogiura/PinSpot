package com.blackbox.pinspot.data.source.weather;

import com.blackbox.pinspot.model.weather.WeatherApiResponse;

public interface WeatherCallback {
    void onSuccessFromRemote(WeatherApiResponse weatherApiResponse);
    void onFailureFromRemote(Exception exception);
}
