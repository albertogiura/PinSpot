package com.blackbox.pinspot.ui.welcome;

import static com.blackbox.pinspot.util.Constants.MINIMUM_PASSWORD_LENGTH;
import static com.blackbox.pinspot.util.Constants.USER_COLLISION_ERROR;
import static com.blackbox.pinspot.util.Constants.WEAK_PASSWORD_ERROR;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.databinding.FragmentSignUpBinding;
import com.blackbox.pinspot.model.Result;
import com.google.android.material.snackbar.Snackbar;
import org.apache.commons.validator.routines.EmailValidator;



public class SignUpFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private FragmentSignUpBinding binding;
    private UserViewModel userViewModel;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSignup.setOnClickListener(v -> {
            String email = binding.edtEmailSignup.getText().toString().trim();
            String password = binding.edtPasswordSignup.getText().toString().trim();

            if (isEmailOk(email) & isPasswordOk(password)) {
                binding.progressBar.setVisibility(View.VISIBLE);
                if (!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email, password, false).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    userViewModel.setAuthenticationError(false);
                                    binding.progressBar.setVisibility(View.GONE);
                                    Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_mainActivity);
                                    requireActivity().finish();
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    binding.progressBar.setVisibility(View.GONE);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email, password, false);
                }
                binding.progressBar.setVisibility(View.GONE);
            } else {
                userViewModel.setAuthenticationError(true);
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        "Check for data error", Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.buttonBackToLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment);
        });
    }

    private String getErrorMessage(String message) {
        switch(message) {
            case WEAK_PASSWORD_ERROR:
                return requireActivity().getString(R.string.error_password);
            case USER_COLLISION_ERROR:
                return requireActivity().getString(R.string.error_user_collision_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }


    private boolean isEmailOk(String email) {
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
        if (!EmailValidator.getInstance().isValid((email))) {
            binding.edtEmailSignup.setError(getString(R.string.error_email));
            return false;
        } else {
            binding.edtEmailSignup.setError(null);
            return true;
        }
    }


    private boolean isPasswordOk(String password) {
        // Check if the password length is correct
        if (password.isEmpty() || password.length() < MINIMUM_PASSWORD_LENGTH) {
            binding.edtPasswordSignup.setError(getString(R.string.error_password));
            return false;
        } else {
            binding.edtPasswordSignup.setError(null);
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}