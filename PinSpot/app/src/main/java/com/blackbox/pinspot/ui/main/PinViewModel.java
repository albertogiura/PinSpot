package com.blackbox.pinspot.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blackbox.pinspot.data.repository.pin.IPinRepository;
import com.blackbox.pinspot.data.repository.pin.PinRepository;
import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.model.Result;

import java.util.List;

public class PinViewModel extends ViewModel {
    private IPinRepository iPinRepository;

    private LiveData<List<Pin>> mAllFavPin;
    private MutableLiveData<Result> pinMutableLiveData;
    private MutableLiveData<Result> imageMutableLiveData;

    public PinViewModel(IPinRepository iPinRepository) {
        this.iPinRepository = iPinRepository;
        mAllFavPin = iPinRepository.getAllFavoritePin();
    }

    LiveData<List<Pin>> getFavPinList() {
        fetchFavPin();
        return mAllFavPin;
    }

    public MutableLiveData<Result> uploadPin(Pin pin) {
            uploadPinOnCloud(pin);
        return pinMutableLiveData;
    }

    private void fetchFavPin() {
        mAllFavPin = iPinRepository.getAllFavoritePin();
    }

    private void uploadPinOnCloud(Pin pin) {
        pinMutableLiveData = iPinRepository.uploadPin(pin);
    }

    public MutableLiveData<Result> uploadImagePin(byte[] data, String photoName) {
        uploadImagePinOnCloud(data, photoName);
        return imageMutableLiveData;
    }

    private void uploadImagePinOnCloud(byte[] data, String photoName) {
        imageMutableLiveData = iPinRepository.uploadPinImage(data, photoName);
    }

    public void insert(Pin pin) { iPinRepository.insert(pin); }

    public void removeFavPin(Pin pin) {iPinRepository.remove(pin); }
}
