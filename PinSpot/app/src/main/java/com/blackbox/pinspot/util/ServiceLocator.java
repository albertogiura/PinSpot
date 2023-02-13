package com.blackbox.pinspot.util;

import static com.blackbox.pinspot.util.Constants.WEATHER_API_KEY;

import android.app.Application;

import com.blackbox.pinspot.data.database.PinRoomDatabase;
import com.blackbox.pinspot.data.repository.pin.IPinRepository;
import com.blackbox.pinspot.data.repository.pin.PinRepository;
import com.blackbox.pinspot.data.repository.user.IUserRepository;
import com.blackbox.pinspot.data.repository.user.UserRepository;
import com.blackbox.pinspot.data.repository.weather.IWeatherRepository;
import com.blackbox.pinspot.data.repository.weather.WeatherRepository;
import com.blackbox.pinspot.data.service.WeatherApiService;
import com.blackbox.pinspot.data.source.pin.BaseFavoritePinLocalDataSource;
import com.blackbox.pinspot.data.source.pin.BasePinRemoteDataSource;
import com.blackbox.pinspot.data.source.pin.FavoritePinLocalDataSource;
import com.blackbox.pinspot.data.source.pin.PinRemoteDataSource;
import com.blackbox.pinspot.data.source.user.BaseUserAuthRemoteDataSource;
import com.blackbox.pinspot.data.source.user.UserAuthRemoteDataSource;
import com.blackbox.pinspot.data.source.weather.BaseWeatherRemoteDataSource;
import com.blackbox.pinspot.data.source.weather.WeatherRemoteDataSource;

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

    public IUserRepository getUserRepository() {
        BaseUserAuthRemoteDataSource userRemoteAuthDataSource =
                new UserAuthRemoteDataSource();

        return new UserRepository(userRemoteAuthDataSource);
    }

    public WeatherApiService getWeatherApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.OPENWEATHERMAP_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(WeatherApiService.class);
    }

    public IWeatherRepository getWeatherRepository() {
        BaseWeatherRemoteDataSource weatherRemoteDataSource =
                new WeatherRemoteDataSource(WEATHER_API_KEY);

        return new WeatherRepository(weatherRemoteDataSource);
    }

    public IPinRepository getPinRepository(Application application) {

        PinRoomDatabase db = PinRoomDatabase.getDatabase(application);

        BaseFavoritePinLocalDataSource baseFavoritePinLocalDataSource = new FavoritePinLocalDataSource(db);
        BasePinRemoteDataSource basePinRemoteDataSource = new PinRemoteDataSource();
        return new PinRepository(application, basePinRemoteDataSource, baseFavoritePinLocalDataSource);
    }
}
