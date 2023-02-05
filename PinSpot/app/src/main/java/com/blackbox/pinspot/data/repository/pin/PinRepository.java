package com.blackbox.pinspot.data.repository.pin;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.data.database.PinRoomDatabase;
import com.blackbox.pinspot.data.source.pin.BaseFavoritePinLocalDataSource;
import com.blackbox.pinspot.data.source.pin.BasePinRemoteDataSource;
import com.blackbox.pinspot.data.source.pin.PinCallback;
import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.model.Result;

import java.util.List;

public class PinRepository implements IPinRepository, PinCallback {

    private final BasePinRemoteDataSource pinRemoteDataSource;
    private final BaseFavoritePinLocalDataSource favoritePinLocalDataSource;
    private final MutableLiveData<List<Pin>> mAllFavPin;
    private final MutableLiveData<Result> pinMutableLiveData;
    private final MutableLiveData<Result> imageMutableLiveData;

    public PinRepository(Application application,
                         BasePinRemoteDataSource pinRemoteDataSource,
                         BaseFavoritePinLocalDataSource favoritePinLocalDataSource) {
        PinRoomDatabase db = PinRoomDatabase.getDatabase(application);
        this.pinRemoteDataSource = pinRemoteDataSource;
        this.pinRemoteDataSource.setPinCallback(this);
        this.favoritePinLocalDataSource = favoritePinLocalDataSource;
        this.favoritePinLocalDataSource.setPinCallback(this);
        mAllFavPin = new MutableLiveData<>();
        pinMutableLiveData = new MutableLiveData<>();
        imageMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Pin>> getAllFavoritePin() {
        retrieveFavPinList();
        return mAllFavPin;
    }

    @Override
    public MutableLiveData<Result> uploadPinImage(byte[] data, String photoName) {
        pinRemoteDataSource.uploadPinImage(data, photoName);
        return imageMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> uploadPin(Pin pin) {
        pinRemoteDataSource.uploadPin(pin);
        return pinMutableLiveData;
    }

    @Override
    public void insert(Pin pin) {
        favoritePinLocalDataSource.insertPin(pin);
    }

    @Override
    public void remove(Pin pin) {
        favoritePinLocalDataSource.deleteFavoritePin(pin);
    }

    @Override
    public void retrieveFavPinList() {
        favoritePinLocalDataSource.getFavoritePinList();
    }

    @Override
    public void onSuccessUploadingImage() {
        Result.PinResponseSuccess result = new Result.PinResponseSuccess();
        imageMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureUploadingImage(String error) {
        Result.Error result = new Result.Error(error);
        imageMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessUploadingPin() {
        Result.PinResponseSuccess result = new Result.PinResponseSuccess();
        pinMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureUploadingPin(String error) {
        Result.Error result = new Result.Error(error);
        pinMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessRetrievingFavPinList(List<Pin> pinList) {
        mAllFavPin.postValue(pinList);
    }

}
