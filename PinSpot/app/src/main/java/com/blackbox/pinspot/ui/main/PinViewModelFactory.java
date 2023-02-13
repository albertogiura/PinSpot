package com.blackbox.pinspot.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blackbox.pinspot.data.repository.pin.IPinRepository;
import com.blackbox.pinspot.data.repository.pin.PinRepository;

public class PinViewModelFactory implements ViewModelProvider.Factory{

    private final IPinRepository iPinRepository;

    public PinViewModelFactory(IPinRepository iPinRepository) {
        this.iPinRepository = iPinRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PinViewModel(iPinRepository);
    }
}
