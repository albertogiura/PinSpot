package com.blackbox.pinspot.data.repository.pin;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.blackbox.pinspot.data.database.PinDao;
import com.blackbox.pinspot.data.database.PinRoomDatabase;
import com.blackbox.pinspot.model.Pin;

import java.util.List;

public class PinRepository implements IPinRepository {

    private final PinDao pinDao;
    private LiveData<List<Pin>> mAllFavPin;

    public PinRepository(Application application) {
        PinRoomDatabase db = PinRoomDatabase.getDatabase(application);
        pinDao = db.pinDao();
        mAllFavPin = pinDao.getAllFavPin();
    }

    public LiveData<List<Pin>> getAllFavoritePin() {
        return mAllFavPin;
    }

    @Override
    public void insert(Pin pin) {
        PinRoomDatabase.databaseWriteExecutor.execute(() -> {
            pinDao.insert(pin);
        });
    }

    @Override
    public void remove(Pin pin) {
        PinRoomDatabase.databaseWriteExecutor.execute(() -> {
            pinDao.delete(pin);
        });
    }
}
