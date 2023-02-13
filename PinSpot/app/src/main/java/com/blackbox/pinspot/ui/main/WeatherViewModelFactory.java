package com.blackbox.pinspot.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blackbox.pinspot.data.repository.weather.IWeatherRepository;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {
    private final IWeatherRepository iWeatherRepository;

    public WeatherViewModelFactory(IWeatherRepository iWeatherRepository) {
        this.iWeatherRepository = iWeatherRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherViewModel(iWeatherRepository);
    }
}
