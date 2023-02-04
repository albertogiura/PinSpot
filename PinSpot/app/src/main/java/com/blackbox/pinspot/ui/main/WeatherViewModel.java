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
    private boolean firstLoading;

    public WeatherViewModel(IWeatherRepositoryWithLiveData weatherRepositoryWithLiveData) {
        this.weatherRepositoryWithLiveData = weatherRepositoryWithLiveData;
        this.firstLoading = true;
    }

    public MutableLiveData<Result> getPinWeather(Double lat, Double lng, boolean firstLoading) {
        //if (pinWeatherMutableLiveData == null) {
            retrievePinWeather(lat, lng, firstLoading);
        //}
        return pinWeatherMutableLiveData;
    }

    private void retrievePinWeather(Double lat, Double lng, Boolean firstLoading) {
        pinWeatherMutableLiveData = weatherRepositoryWithLiveData.retrieveWeather(lat, lng, firstLoading);
    }

    public boolean isFirstLoading() {
        return firstLoading;
    }

    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }
}
