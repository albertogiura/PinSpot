package com.blackbox.pinspot.data.repository.pin;

import androidx.lifecycle.LiveData;

import com.blackbox.pinspot.model.Pin;

import java.util.List;

public interface IPinRepository {
    LiveData<List<Pin>> getAllFavoritePin();

    void insert(Pin pin);
    void remove(Pin pin);
}
