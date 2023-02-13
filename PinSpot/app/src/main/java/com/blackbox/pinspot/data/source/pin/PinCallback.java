package com.blackbox.pinspot.data.source.pin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.model.Pin;

import java.util.List;

public interface PinCallback {
    void onSuccessUploadingImage();
    void onFailureUploadingImage(String error);
    void onSuccessUploadingPin();
    void onFailureUploadingPin(String error);
    void onSuccessRetrievingFavPinList(List<Pin> pinList);
}
