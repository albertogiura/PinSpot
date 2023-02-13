package com.blackbox.pinspot.data.source.weather;

import static com.blackbox.pinspot.util.Constants.API_KEY_ERROR;
import static com.blackbox.pinspot.util.Constants.RETROFIT_ERROR;

import android.util.Log;
import android.widget.Toast;

import com.blackbox.pinspot.data.service.WeatherApiService;
import com.blackbox.pinspot.model.WeatherApiResponse;
import com.blackbox.pinspot.ui.main.PinInfoFragment;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;

public class WeatherRemoteDataSource extends BaseWeatherRemoteDataSource {

    private final WeatherApiService weatherApiService;
    private final String apiKey;

    public WeatherRemoteDataSource(String apiKey) {
        this.apiKey = apiKey;
        this.weatherApiService = ServiceLocator.getInstance().getWeatherApiService();
    }

    public void getWeatherWithLatLng(Double lat, Double lng) {
        Call<WeatherApiResponse> weatherApiResponseCall = weatherApiService.getWeather(lat, lng, apiKey);
        weatherApiResponseCall.enqueue(new Callback<WeatherApiResponse>() {
            @Override
            public void onResponse(Call<WeatherApiResponse> call, retrofit2.Response<WeatherApiResponse> response) {
                if (response.code() == 404) {
                    Toast.makeText(PinInfoFragment.newInstance().requireContext(), "Please Enter a valid City", Toast.LENGTH_LONG).show();
                    weatherCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
                } else if (response.isSuccessful() && response.body() != null) {
                    weatherCallback.onSuccessFromRemote(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherApiResponse> call, Throwable t) {
                Toast.makeText(PinInfoFragment.newInstance().requireContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                weatherCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }

        });
    }
}
