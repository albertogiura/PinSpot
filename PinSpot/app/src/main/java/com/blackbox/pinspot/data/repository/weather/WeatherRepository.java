package com.blackbox.pinspot.data.repository.weather;

import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.data.source.weather.BaseWeatherRemoteDataSource;
import com.blackbox.pinspot.data.source.weather.WeatherCallback;
import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.WeatherApiResponse;

public class WeatherRepository implements IWeatherRepository, WeatherCallback {

    private static final String TAG = WeatherRepository.class.getSimpleName();

    private final MutableLiveData<Result> pinWeatherMutableLiveData;
    private final BaseWeatherRemoteDataSource weatherRemoteDataSource;

    public WeatherRepository(BaseWeatherRemoteDataSource baseWeatherRemoteDataSource) {
        pinWeatherMutableLiveData = new MutableLiveData<>();
        this.weatherRemoteDataSource = baseWeatherRemoteDataSource;
        this.weatherRemoteDataSource.setWeatherCallback(this);
    }

    public MutableLiveData<Result> retrieveWeather(Double lat, Double lng) {
        weatherRemoteDataSource.getWeatherWithLatLng(lat, lng);
        return pinWeatherMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(WeatherApiResponse weatherApiResponse) {
        if (pinWeatherMutableLiveData.getValue() != null && pinWeatherMutableLiveData.getValue().isSuccess()) {
            ((Result.WeatherResponseSuccess)pinWeatherMutableLiveData
                    .getValue()).getData().setMainWeatherInfo(weatherApiResponse.getMainWeatherInfo());

            ((Result.WeatherResponseSuccess)pinWeatherMutableLiveData
                    .getValue()).getData().setWeather(weatherApiResponse.getWeather());

            Result.WeatherResponseSuccess result = new Result.WeatherResponseSuccess(weatherApiResponse);
            pinWeatherMutableLiveData.postValue(result);
        } else {
            Result.WeatherResponseSuccess result = new Result.WeatherResponseSuccess(weatherApiResponse);
            pinWeatherMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        pinWeatherMutableLiveData.postValue(result);
    }
}
