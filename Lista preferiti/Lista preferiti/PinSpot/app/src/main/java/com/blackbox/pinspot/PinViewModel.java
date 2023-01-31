package com.blackbox.pinspot;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.blackbox.pinspot.model.Pin;

import java.util.List;

public class PinViewModel extends AndroidViewModel {
    private PinRepository mRepository;

    private final LiveData<List<Pin>> mAllPin;

    public PinViewModel (Application application) {
        super(application);
        mRepository = new PinRepository(application);
        mAllPin = mRepository.getAllPin();
    }

    LiveData<List<Pin>> getAllPin() { return mAllPin; }

    public void insert(Pin pin) { mRepository.insert(pin); }
}
