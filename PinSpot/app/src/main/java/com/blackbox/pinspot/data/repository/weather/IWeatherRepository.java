package com.blackbox.pinspot.data.repository.weather;

import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.model.Result;

public interface IWeatherRepository {
    public MutableLiveData<Result> retrieveWeather(Double lat, Double lng);
}
