package com.blackbox.pinspotdemo.util;

import android.app.Application;

import com.blackbox.pinspotdemo.repository.user.IUserRepository;
import com.blackbox.pinspotdemo.repository.user.UserRepository;
import com.blackbox.pinspotdemo.source.user.BaseUserAuthRemoteDataSource;
import com.blackbox.pinspotdemo.source.user.BaseUserDataRemoteDataSource;
import com.blackbox.pinspotdemo.source.user.UserAuthRemoteDataSource;
import com.blackbox.pinspotdemo.source.user.UserDataRemoteDataSource;

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
}
