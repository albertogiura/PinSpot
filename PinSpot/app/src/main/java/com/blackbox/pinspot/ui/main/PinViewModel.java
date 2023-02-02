package com.blackbox.pinspot.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.blackbox.pinspot.data.repository.pin.IPinRepository;
import com.blackbox.pinspot.data.repository.pin.PinRepository;
import com.blackbox.pinspot.model.Pin;

import java.util.List;

public class PinViewModel extends ViewModel {
    private IPinRepository iPinRepository;

    private final LiveData<List<Pin>> mAllFavPin;

    public PinViewModel(IPinRepository iPinRepository) {
        this.iPinRepository = iPinRepository;
        mAllFavPin = iPinRepository.getAllFavoritePin();
    }

    LiveData<List<Pin>> getFavPinList() { return mAllFavPin; }

    public void insert(Pin pin) { iPinRepository.insert(pin); }

    public void removeFavPin(Pin pin) {iPinRepository.remove(pin); }
}
