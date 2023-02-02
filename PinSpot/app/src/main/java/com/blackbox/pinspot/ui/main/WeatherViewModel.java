package com.blackbox.pinspot.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blackbox.pinspot.data.repository.weather.IWeatherRepositoryWithLiveData;
import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.weather.WeatherApiResponse;

public class WeatherViewModel extends ViewModel {

    private static final String TAG = WeatherViewModel.class.getSimpleName();

    private final IWeatherRepositoryWithLiveData weatherRepositoryWithLiveData;
    private MutableLiveData<Result> pinWeatherMutableLiveData;

    public WeatherViewModel(IWeatherRepositoryWithLiveData weatherRepositoryWithLiveData) {
        this.weatherRepositoryWithLiveData = weatherRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getPinWeather(Double lat, Double lng) {
        if (pinWeatherMutableLiveData == null) {
            retrievePinWeather(lat, lng);
        }
        return pinWeatherMutableLiveData;
    }

    private void retrievePinWeather(Double lat, Double lng) {
        pinWeatherMutableLiveData = weatherRepositoryWithLiveData.retrieveWeather(lat, lng);
    }
}
