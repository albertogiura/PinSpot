package com.blackbox.pinspot.data.source.weather;

import static com.blackbox.pinspot.util.Constants.API_KEY_ERROR;
import static com.blackbox.pinspot.util.Constants.RETROFIT_ERROR;

import android.util.Log;
import android.widget.Toast;

import com.blackbox.pinspot.data.repository.weather.WeatherRepositoryWithLiveData;
import com.blackbox.pinspot.data.service.WeatherApiService;
import com.blackbox.pinspot.model.weather.WeatherApiResponse;
import com.blackbox.pinspot.ui.main.PinInfoFragment;
import com.blackbox.pinspot.util.ServiceLocator;

import retrofit2.Call;
import retrofit2.Callback;

public class WeatherRemoteDataSource extends BaseWeatherRemoteDataSource {
    private String TAG = "ciao";

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
                    Log.d(TAG, "ciao mondo");

                    //Log.d(TAG, response.body().getWeather()[0].toString());

                    //Toast.makeText(PinInfoFragment.newInstance().requireContext(), response.code() + " ", Toast.LENGTH_LONG).show();
                    weatherCallback.onSuccessFromRemote(response.body());
                }
                /*WeatherApiResponse mydata = response.body();

                WeatherApiResponse.MainWeatherInfo mainWeatherInfo = mydata.getMainWeatherInfo();
                WeatherApiResponse.Weather[] weather = mydata.getWeather();


                Double temp = mainWeatherInfo.getTemp();

                String description = weather[0].getDescription();

                Integer temperature = (int) (temp - 273.15);*/

                /*SharedPreferences sharedPref = requireActivity().getSharedPreferences(
                        "settings", Context.MODE_PRIVATE); //DAMETTEREINUNACOSTANTE
                Boolean celsiusSettings = sharedPref.getBoolean("celsius", true);
                if(celsiusSettings == true){
                    binding.PinLatTextView.setText(String.valueOf(temperature) + " °C");
                }else{
                    binding.PinLatTextView.setText(String.valueOf(celsToFar(temperature)) + " °F");
                }

                binding.pinLongTextView.setText(description);*/
            }

            @Override
            public void onFailure(Call<WeatherApiResponse> call, Throwable t) {
                Toast.makeText(PinInfoFragment.newInstance().requireContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                weatherCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }

        });
    }
}
