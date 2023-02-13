package com.blackbox.pinspot.ui.main;

import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_SKIP;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.adapter.PinRecyclerViewAdapter;
import com.blackbox.pinspot.data.repository.pin.IPinRepository;
import com.blackbox.pinspot.databinding.FragmentFavPlacesBinding;
import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavPlacesFragment extends Fragment {

    private List<Pin> pinList = new ArrayList<>();
    private PinRecyclerViewAdapter mAdapter;
    private FragmentFavPlacesBinding binding;
    private PinViewModel pinViewModel;


    public FavPlacesFragment() {
        // Required empty public constructor
    }

    public static FavPlacesFragment newInstance() {
        return new FavPlacesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        // Inflate the layout for this fragment
        binding = FragmentFavPlacesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        Boolean skipSettings = sharedPref.getBoolean(SHARED_PREFERENCES_SKIP, false);
        if (skipSettings == false) {
            mAdapter = new PinRecyclerViewAdapter(requireContext(), pinList, requireActivity().getApplication(),
                    new PinRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onPinItemClick(Pin pin) {
                            if(isOnline()==true){
                                FavPlacesFragmentDirections.ActionFavPlacesFragmentToPinInfoFragment action =
                                        FavPlacesFragmentDirections.actionFavPlacesFragmentToPinInfoFragment(pin);
                                Navigation.findNavController(view).navigate(action);
                            }else {
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        "You are offline", Snackbar.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFavoriteButtonPressed(int position) {
                            pinViewModel.removeFavPin(pinList.get(position));
                        }
                    });
            binding.favPinRecyclerview.setAdapter(mAdapter);
            binding.favPinRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

            pinViewModel.getFavPinList().observe(getViewLifecycleOwner(), pins -> {
                pinList.clear();
                pinList.addAll(pins);
                mAdapter.notifyDataSetChanged();
            });
        }else{
            binding.LogInTxt.setVisibility(View.VISIBLE);
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}