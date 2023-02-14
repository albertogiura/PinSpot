package com.blackbox.pinspot.ui.main;

import static com.blackbox.pinspot.util.Constants.DBIMAGES;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_CELSIUS;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_SKIP;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.data.repository.pin.IPinRepository;
import com.blackbox.pinspot.data.repository.weather.IWeatherRepository;
import com.blackbox.pinspot.databinding.FragmentPinInfoBinding;
import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.WeatherApiResponse;
import com.blackbox.pinspot.util.GlideApp;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PinInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinInfoFragment extends Fragment {
    public class MyGoToLoginAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(R.id.action_pinInfoFragment_to_loginActivity);
        }
    }
    private List<Pin> pinList = new ArrayList<>();
    private WeatherViewModel weatherViewModel;
    private PinViewModel pinViewModel;


    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES);

    private FragmentPinInfoBinding binding;

    public PinInfoFragment() {
        // Required empty public constructor
    }

    public static PinInfoFragment newInstance() {
        return new PinInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        IWeatherRepository weatherRepositoryWithLiveData =
                ServiceLocator.getInstance().getWeatherRepository();

        if (weatherRepositoryWithLiveData != null) {

            weatherViewModel = new ViewModelProvider(
                    requireActivity(),
                    new WeatherViewModelFactory(weatherRepositoryWithLiveData)).get(WeatherViewModel.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
        }

        IPinRepository pinRepository =
                ServiceLocator.getInstance().getPinRepository(requireActivity().getApplication());

        if (pinRepository != null) {
            
            pinViewModel = new ViewModelProvider(
                    requireActivity(),
                    new PinViewModelFactory(pinRepository)).get(PinViewModel.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPinInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        Pin pin = PinInfoFragmentArgs.fromBundle(getArguments()).getPin();

        binding.closePinInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        pinViewModel.getFavPinList().observe(getViewLifecycleOwner(), pins -> {
            pinList.clear();
            pinList.addAll(pins);

            Pin currentPin = PinInfoFragmentArgs.fromBundle(getArguments()).getPin();
            setImageViewButtonFavoritePin(currentPin);
        });

        binding.addPinToFavFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = requireActivity().getSharedPreferences(
                        SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                Boolean skipSettings = sharedPref.getBoolean(SHARED_PREFERENCES_SKIP, false);
                if (skipSettings == false) {
                if (pin != null){

                    if (pinList.contains(pin)) {
                        pinViewModel.removeFavPin(pin);
                        Snackbar.make(v, "Pin removed from favorite list",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                        binding.addPinToFavFab.setImageDrawable(
                                AppCompatResources.getDrawable(requireActivity().getApplication(),
                                        R.drawable.not_starred_fav_pin_foreground));
                    } else {
                        pinViewModel.insert(pin);
                        Snackbar.make(v, "Pin added to favorite list",
                                        Snackbar.LENGTH_SHORT)
                                .show();
                        binding.addPinToFavFab.setImageDrawable(
                                AppCompatResources.getDrawable(requireActivity().getApplication(),
                                        R.drawable.fav_pin_starred_foreground));
                    }

                } else {
                    Snackbar.make(v, "Error in adding pin to favorite list",
                                    Snackbar.LENGTH_SHORT)
                            .show();


                }

                }else{
                    Snackbar.make(v,
                                "To use this function you need to be logged in the app",
                                    Snackbar.LENGTH_SHORT).setAction("Go to", new PinInfoFragment.MyGoToLoginAction())
                            .show();
                }
            }
        });

        if (pin != null){
            binding.PinTitleTextView.setText(pin.getTitle());
            setImageViewButtonFavoritePin(pin);
            // Retrieve the photo associated to a provided pin via its link attribute from
            // Firebase Storage and displays it with Glide support
            StorageReference storageReference = storage.getReference().child("pinPhotos/"+pin.getLink()+".jpeg");
            GlideApp.with(binding.pinPhotoImageView.getContext())
                    .load(storageReference)
                    .placeholder(R.drawable.placeholder_pin_photo)
                    .into(binding.pinPhotoImageView);

            Double latitude = pin.getLat();
            Double longitude = pin.getLon();

            weatherViewModel.getPinWeather(latitude, longitude).observe(getViewLifecycleOwner(),
                    result -> {
                        if (result.isSuccess()){
                            WeatherApiResponse weatherApiResponse = ((Result.WeatherResponseSuccess) result).getData();
                            Double temp = weatherApiResponse.getMainWeatherInfo().getTemp();
                            String description = weatherApiResponse.getWeather()[0].getDescription();


                            Integer temperature = (int) (temp - 273.15); //kelvin to celsius
                            SharedPreferences sharedPref = requireContext().getSharedPreferences(
                                    SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                            Boolean celsiusSettings = sharedPref.getBoolean(SHARED_PREFERENCES_CELSIUS, true);
                            if(celsiusSettings == true){
                                binding.textViewTemperature.setText(String.valueOf(temperature) + " °C");
                            }else{
                                binding.textViewTemperature.setText(String.valueOf(celsToFar(temperature)) + " °F");
                            }
                            //binding.textViewTemperature.setText(String.valueOf(temp));
                            binding.textViewWeatherDescription.setText(description);

                        } else {

                            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
                        }
                    });

        } else{
            binding.PinTitleTextView.setText("NULL");
        }

    }

    private int celsToFar(int c){
        return (int) ((c * 1.8) + 32);
    }

    private void setImageViewButtonFavoritePin(Pin pin) {
        if (pinList.contains(pin)) {
            binding.addPinToFavFab.setImageDrawable(
                    AppCompatResources.getDrawable(requireActivity().getApplication(),
                            R.drawable.fav_pin_starred_foreground));
        } else {
            binding.addPinToFavFab.setImageDrawable(
                    AppCompatResources.getDrawable(requireActivity().getApplication(),
                            R.drawable.not_starred_fav_pin_foreground));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}