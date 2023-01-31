package com.blackbox.pinspot;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.blackbox.pinspot.model.Pin;

import java.util.List;

public class PinRepository {
    private PinDAO pinDAO;
    private LiveData<List<Pin>> mAllPin;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    PinRepository(Application application) {
        PinRoomDB db = PinRoomDB.getDatabase(application);
        pinDAO = db.pinDao();
        mAllPin = pinDAO.getAllPin();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Pin>> getAllPin() {
        return mAllPin;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Pin pin) {
        PinRoomDB.databaseWriteExecutor.execute(() -> {
            pinDAO.insert(pin);
        });
    }
}
