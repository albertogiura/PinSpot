package com.blackbox.pinspot.util;

import android.app.Application;

import com.blackbox.pinspot.data.repository.user.IUserRepository;
import com.blackbox.pinspot.data.repository.user.UserRepository;
import com.blackbox.pinspot.data.repository.weather.IWeatherRepositoryWithLiveData;
import com.blackbox.pinspot.data.repository.weather.WeatherRepositoryWithLiveData;
import com.blackbox.pinspot.data.service.WeatherApiService;
import com.blackbox.pinspot.data.source.user.BaseUserAuthRemoteDataSource;
import com.blackbox.pinspot.data.source.user.BaseUserDataRemoteDataSource;
import com.blackbox.pinspot.data.source.user.UserAuthRemoteDataSource;
import com.blackbox.pinspot.data.source.user.UserDataRemoteDataSource;
import com.blackbox.pinspot.data.source.weather.BaseWeatherRemoteDataSource;
import com.blackbox.pinspot.data.source.weather.WeatherRemoteDataSource;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public IUserRepository getUserRepository(Application application) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        BaseUserAuthRemoteDataSource userRemoteAuthDataSource =
                new UserAuthRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserDataRemoteDataSource(sharedPreferencesUtil);

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        /*BaseNewsLocalDataSource newsLocalDataSource =
                new NewsLocalDataSource(getNewsDao(application), sharedPreferencesUtil,
                        dataEncryptionUtil);*/

        return new UserRepository(userRemoteAuthDataSource,
                userDataRemoteDataSource);
    }

    public WeatherApiService getWeatherApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.OPENWEATHERMAP_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(WeatherApiService.class);
    }

    public IWeatherRepositoryWithLiveData getWeatherRepository(Application application) {
        BaseWeatherRemoteDataSource weatherRemoteDataSource =
                new WeatherRemoteDataSource("4f6ec18ab9eb724adb869edca9cbbf63");

        //weatherRemoteDataSource = new WeatherRemoteDataSource(application.getString(R.string.news_api_key));
        //weatherRemoteDataSource = new WeatherRemoteDataSource("4f6ec18ab9eb724adb869edca9cbbf63");

        return new WeatherRepositoryWithLiveData(weatherRemoteDataSource);
    }
}
