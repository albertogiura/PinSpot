package com.blackbox.pinspot.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blackbox.pinspot.data.repository.weather.IWeatherRepository;
import com.blackbox.pinspot.model.Result;

public class WeatherViewModel extends ViewModel {

    private static final String TAG = WeatherViewModel.class.getSimpleName();

    private final IWeatherRepository weatherRepository;
    private MutableLiveData<Result> pinWeatherMutableLiveData;

    public WeatherViewModel(IWeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public MutableLiveData<Result> getPinWeather(Double lat, Double lng) {
            retrievePinWeather(lat, lng);
        return pinWeatherMutableLiveData;
    }

    private void retrievePinWeather(Double lat, Double lng) {
        pinWeatherMutableLiveData = weatherRepository.retrieveWeather(lat, lng);
    }
}
