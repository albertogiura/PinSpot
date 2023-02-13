package com.blackbox.pinspot.data.source.pin;

import com.blackbox.pinspot.model.Pin;

import java.util.List;

public abstract class BaseFavoritePinLocalDataSource {
    protected PinCallback pinCallback;

    public void setPinCallback(PinCallback pinCallback) {
        this.pinCallback = pinCallback;
    }

    public abstract void getFavoritePinList();
    public abstract void deleteFavoritePin(Pin pin);
    public abstract void insertPin(Pin pin);
}
