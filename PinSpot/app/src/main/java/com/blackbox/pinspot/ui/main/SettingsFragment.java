package com.blackbox.pinspot.ui.main;

import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_CELSIUS;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_SKIP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.data.repository.user.IUserRepository;
import com.blackbox.pinspot.databinding.FragmentSettingsBinding;
import com.blackbox.pinspot.ui.welcome.LoginActivity;
import com.blackbox.pinspot.ui.welcome.UserViewModel;
import com.blackbox.pinspot.ui.welcome.UserViewModelFactory;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentSettingsBinding binding;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository();
        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        Boolean skipSettings = sharedPref.getBoolean(SHARED_PREFERENCES_SKIP, false);

        if(skipSettings==false) {
            binding.textViewUserEmail.setText(userViewModel.getLoggedUser().getEmail());


            binding.buttonUserLogout.setOnClickListener(v -> {
                userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                    if (result.isSuccess()) {

                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();

                    } else {
                        Snackbar.make(v,
                                this.getString(R.string.unexpected_error),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
            });
        }else{
           binding.textViewUserEmail.setText("To use this function you need to be logged in the app");
           binding.buttonUserLogout.setText("Log In");

           binding.buttonUserLogout.setOnClickListener(v -> {
               Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginActivity);
           });
        }

        Boolean celsiusSettings = sharedPref.getBoolean(SHARED_PREFERENCES_CELSIUS, true);

        binding.switchDegree.setChecked(celsiusSettings);

        binding.switchDegree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context context = getActivity();

                SharedPreferences sharedPref = context.getSharedPreferences(
                        SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(SHARED_PREFERENCES_CELSIUS, isChecked);
                editor.apply();

            }
        });
    }
}