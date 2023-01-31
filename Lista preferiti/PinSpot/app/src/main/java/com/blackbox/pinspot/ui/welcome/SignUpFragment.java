package com.blackbox.pinspot.ui.welcome;

import static com.blackbox.pinspot.util.Constants.EMAIL_ADDRESS;
import static com.blackbox.pinspot.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.ID_TOKEN;
import static com.blackbox.pinspot.util.Constants.MINIMUM_PASSWORD_LENGTH;
import static com.blackbox.pinspot.util.Constants.PASSWORD;
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
import com.blackbox.pinspot.model.User;
import com.blackbox.pinspot.ui.main.MainActivity;
import com.blackbox.pinspot.util.DataEncryptionUtil;
import com.google.android.material.snackbar.Snackbar;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SignUpFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private FragmentSignUpBinding binding;
    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;

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
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
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
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    saveLoginData(email, password, user.getIdToken());
                                    userViewModel.setAuthenticationError(false);
                                    Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_mainActivity);
                                    requireActivity().finish();
                                    //retrieveUserInformationAndStartActivity(user, R.id.action_loginFragment_to_mainActivity);
                                } else {
                                    userViewModel.setAuthenticationError(true);
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

    private void retrieveUserInformationAndStartActivity(User user, int destination) {
        binding.progressBar.setVisibility(View.GONE);
        Navigation.findNavController(requireView()).navigate(destination);

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

    private void saveLoginData(String email, String password, String idToken) {
        try {
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS, email);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD, password);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN, idToken);
            dataEncryptionUtil.writeSecreteDataOnFile(ENCRYPTED_DATA_FILE_NAME,
                    email.concat(":").concat(password));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}