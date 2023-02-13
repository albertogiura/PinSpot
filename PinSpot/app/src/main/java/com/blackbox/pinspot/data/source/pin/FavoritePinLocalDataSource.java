package com.blackbox.pinspot.data.source.pin;

import com.blackbox.pinspot.data.database.PinDao;
import com.blackbox.pinspot.data.database.PinRoomDatabase;
import com.blackbox.pinspot.model.Pin;

import java.util.ArrayList;
import java.util.List;

public class FavoritePinLocalDataSource extends BaseFavoritePinLocalDataSource {

    private final PinDao pinDao;

    public FavoritePinLocalDataSource(PinRoomDatabase pinRoomDatabase) {
        this.pinDao = pinRoomDatabase.pinDao();
    }

    @Override
    public void getFavoritePinList() {
        PinRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Pin> list = pinDao.getAllFavPin();
            pinCallback.onSuccessRetrievingFavPinList(list);
        });
    }

    @Override
    public void deleteFavoritePin(Pin pin) {
        PinRoomDatabase.databaseWriteExecutor.execute(() -> {
            pinDao.delete(pin);
            getFavoritePinList();
        });
    }

    @Override
    public void insertPin(Pin pin) {
        PinRoomDatabase.databaseWriteExecutor.execute(() -> {
            pinDao.insert(pin);
            getFavoritePinList();
        });
    }
}
