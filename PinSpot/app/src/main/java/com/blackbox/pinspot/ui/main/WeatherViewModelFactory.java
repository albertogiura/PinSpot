package com.blackbox.pinspot.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blackbox.pinspot.data.repository.weather.IWeatherRepositoryWithLiveData;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {
    private final IWeatherRepositoryWithLiveData iWeatherRepositoryWithLiveData;

    public WeatherViewModelFactory(IWeatherRepositoryWithLiveData iWeatherRepositoryWithLiveData) {
        this.iWeatherRepositoryWithLiveData = iWeatherRepositoryWithLiveData;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherViewModel(iWeatherRepositoryWithLiveData);
    }
}
