package com.blackbox.pinspot.data.repository.pin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.model.Result;

import java.util.List;

public interface IPinRepository {
    LiveData<List<Pin>> getAllFavoritePin();
    MutableLiveData<Result> uploadPinImage(byte[] data, String photoName);
    MutableLiveData<Result> uploadPin(Pin pin);

    void insert(Pin pin);
    void remove(Pin pin);
    void retrieveFavPinList();
}
