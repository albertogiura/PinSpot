package com.blackbox.pinspot.data.repository.weather;

import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.data.source.weather.BaseWeatherRemoteDataSource;
import com.blackbox.pinspot.data.source.weather.WeatherCallback;
import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.weather.WeatherApiResponse;

public class WeatherRepositoryWithLiveData implements IWeatherRepositoryWithLiveData, WeatherCallback{

    private static final String TAG = WeatherRepositoryWithLiveData.class.getSimpleName();

    private final MutableLiveData<Result> pinWeatherMutableLiveData;
    //private final MutableLiveData<WeatherApiResponse.MainWeatherInfo> pinWeatherMainInfoMutableLiveData;
    //private final MutableLiveData<WeatherApiResponse.Weather> pinWeatherDescriptionMutableLiveData;
    private final BaseWeatherRemoteDataSource weatherRemoteDataSource;

    public WeatherRepositoryWithLiveData(BaseWeatherRemoteDataSource baseWeatherRemoteDataSource) {
        //pinWeatherDescriptionMutableLiveData = new MutableLiveData<>();
        //pinWeatherMainInfoMutableLiveData = new MutableLiveData<>();
        pinWeatherMutableLiveData = new MutableLiveData<>();
        this.weatherRemoteDataSource = baseWeatherRemoteDataSource;
        this.weatherRemoteDataSource.setWeatherCallback(this);
    }

    public MutableLiveData<Result> retrieveWeather(Double lat, Double lng, Boolean firstLoading) {
        /*if (firstLoading) {
            weatherRemoteDataSource.getWeatherWithLatLng(lat, lng);
        } else {
            return pinWeatherMutableLiveData;
        }*/
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



        /*if (pinWeatherMutableLiveData.getValue() != null && pinWeatherMutableLiveData.getValue().isSuccess()) {
            WeatherApiResponse.MainWeatherInfo mainWeatherInfo = ((Result.WeatherResponseSuccess)pinWeatherMutableLiveData
                    .getValue()).getData().getMainWeatherInfo();
            weatherApiResponse.setMainWeatherInfo(mainWeatherInfo);

            WeatherApiResponse.Weather[] weather = ((Result.WeatherResponseSuccess)pinWeatherMutableLiveData
                    .getValue()).getData().getWeather();
            weatherApiResponse.setWeather(weather);
            Result.WeatherResponseSuccess result = new Result.WeatherResponseSuccess(weatherApiResponse);
            pinWeatherMutableLiveData.postValue(result);
        } else {
            Result.WeatherResponseSuccess result = new Result.WeatherResponseSuccess(weatherApiResponse);
            pinWeatherMutableLiveData.postValue(result);
        }*/


            /*WeatherApiResponse.MainWeatherInfo mainWeatherInfo = ((Result.WeatherResponseSuccess)pinWeatherMutableLiveData
                    .getValue()).getData().getMainWeatherInfo();;
            weatherApiResponse.setMainWeatherInfo(mainWeatherInfo);

            WeatherApiResponse.Weather[] weather = ((Result.WeatherResponseSuccess)pinWeatherMutableLiveData
                    .getValue()).getData().getWeather();
            weatherApiResponse.setWeather(weather);
            Result.WeatherResponseSuccess result = new Result.WeatherResponseSuccess(weatherApiResponse);
            pinWeatherMutableLiveData.postValue(result);*/



        /*WeatherApiResponse.MainWeatherInfo mainWeatherInfo = weatherApiResponse.getMainWeatherInfo();
        weatherApiResponse.setMainWeatherInfo(mainWeatherInfo);
        WeatherApiResponse.Weather[] weather = weatherApiResponse.getWeather();
        weatherApiResponse.setWeather(weather);
        //pinWeatherMainInfoMutableLiveData.postValue(weatherApiResponse.getMainWeatherInfo());
        //pinWeatherDescriptionMutableLiveData.postValue(weatherApiResponse.getWeather()[0]);
        Result.WeatherResponseSuccess result = new Result.WeatherResponseSuccess(weatherApiResponse);
        pinWeatherMutableLiveData.postValue(result);*/
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        pinWeatherMutableLiveData.postValue(result);
    }
}
