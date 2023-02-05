package com.blackbox.pinspot.data.source.pin;

import com.blackbox.pinspot.model.Pin;

public abstract class BasePinRemoteDataSource {
    protected PinCallback pinCallback;

    public void setPinCallback(PinCallback pinCallback) {
        this.pinCallback = pinCallback;
    }

    public abstract void uploadPinImage(byte[] data, String photoName);
    public abstract void uploadPin(Pin pin);
}
