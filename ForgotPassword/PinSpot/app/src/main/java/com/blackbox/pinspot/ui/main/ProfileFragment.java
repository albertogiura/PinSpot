package com.blackbox.pinspot.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.databinding.FragmentProfileBinding;
import com.blackbox.pinspot.repository.user.IUserRepository;
import com.blackbox.pinspot.ui.welcome.LoginActivity;
import com.blackbox.pinspot.ui.welcome.UserViewModel;
import com.blackbox.pinspot.ui.welcome.UserViewModelFactory;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

public class ProfileFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textViewUserEmail.setText(userViewModel.getLoggedUser().getEmail());
        binding.textViewUserId.setText(userViewModel.getLoggedUser().getIdToken());

        binding.buttonUserLogout.setOnClickListener(v -> {
            userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccess()) {
                    /*Snackbar.make(v,
                    "Logout completed successfully",
                            Snackbar.LENGTH_SHORT).show();*/
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                    /*Navigation.findNavController(view).navigate(
                    R.id.action_fragment_settings_to_welcomeActivity);*/
                } else {
                    Snackbar.make(v,
                            this.getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }
}