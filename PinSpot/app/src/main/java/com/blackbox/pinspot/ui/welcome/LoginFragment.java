package com.blackbox.pinspot.ui.welcome;

import static com.blackbox.pinspot.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.blackbox.pinspot.util.Constants.INVALID_USER_ERROR;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_SKIP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blackbox.pinspot.R;

import com.blackbox.pinspot.data.repository.user.IUserRepository;
import com.blackbox.pinspot.databinding.FragmentLoginBinding;
import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.User;
import com.blackbox.pinspot.ui.main.MainActivity;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.validator.routines.EmailValidator;


public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    // Just for debug purposes
    private static final boolean USE_NAVIGATION_COMPONENT = true;
    private FragmentLoginBinding binding;

    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;

    private UserViewModel userViewModel;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository();
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        oneTapClient = Identity.getSignInClient(requireActivity());
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        startIntentSenderForResult = new ActivityResultContracts.StartIntentSenderForResult();

        activityResultLauncher = registerForActivityResult(startIntentSenderForResult, activityResult -> {
            if (activityResult.getResultCode() == Activity.RESULT_OK) {

                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(activityResult.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate with Firebase Auth.
                        userViewModel.getGoogleUserMutableLiveData(idToken).observe(getViewLifecycleOwner(), authenticationResult -> {
                            if (authenticationResult.isSuccess()) {
                                userViewModel.setAuthenticationError(false);
                                binding.progressBar.setVisibility(View.GONE);
                                startActivityBasedOnCondition(MainActivity.class, R.id.action_loginFragment_to_mainActivity);
                            } else {
                                userViewModel.setAuthenticationError(true);
                                binding.progressBar.setVisibility(View.GONE);
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) authenticationResult).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    if(isOnline()==false){
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                "You are offline", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                requireActivity().getString(R.string.unexpected_error),
                                Snackbar.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (userViewModel.getLoggedUser() != null) {
                startActivityBasedOnCondition(MainActivity.class,
                        R.id.action_loginFragment_to_mainActivity);
        }
        binding.skipButton.setOnClickListener(v -> {
            SharedPreferences sharedPref = requireActivity().getSharedPreferences(
                    SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SHARED_PREFERENCES_SKIP, true);
            editor.apply();
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainActivity);
        });

        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            // Start login if email and password are ok
            if (isEmailOk(email) & isPasswordOk(password)) {
                if (!userViewModel.isAuthenticationError()) {
                    binding.progressBar.setVisibility(View.VISIBLE);

                    userViewModel.getUserMutableLiveData(email, password, true).observe(getViewLifecycleOwner(), new Observer<Result>() {
                        @Override
                        public void onChanged(Result result) {
                            if (result.isSuccess()) {
                                userViewModel.setAuthenticationError(false);
                                binding.progressBar.setVisibility(View.VISIBLE);
                                SharedPreferences sharedPref = requireActivity().getSharedPreferences(
                                        SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean(SHARED_PREFERENCES_SKIP, false);
                                editor.apply();
                                startActivityBasedOnCondition(MainActivity.class, R.id.action_loginFragment_to_mainActivity);
                            } else {
                                userViewModel.setAuthenticationError(true);
                                binding.progressBar.setVisibility(View.GONE);
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) result).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    userViewModel.getUser(email, password, true);
                }
            } else {
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.buttonGoogleLogin.setOnClickListener(v -> oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        SharedPreferences sharedPref = requireActivity().getSharedPreferences(
                                SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(SHARED_PREFERENCES_SKIP, false);
                        editor.apply();
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent()).build();
                        activityResultLauncher.launch(intentSenderRequest);
                    }
                })
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());

                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                requireActivity().getString(R.string.error_no_google_account_found_message),
                                Snackbar.LENGTH_SHORT).show();
                    }
                }));

        binding.buttonOpenSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
        });

        binding.buttonForgotLoginPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.setAuthenticationError(false);
    }

    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case INVALID_CREDENTIALS_ERROR:
                return requireActivity().getString(R.string.error_login_password_message);
            case INVALID_USER_ERROR:
                return requireActivity().getString(R.string.error_login_user_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }


    private void startActivityBasedOnCondition(Class<?> destinationActivity, int destination) {
        if (USE_NAVIGATION_COMPONENT) {
            Navigation.findNavController(requireView()).navigate(destination);
        } else {
            Intent intent = new Intent(requireContext(), destinationActivity);
            startActivity(intent);
        }
        requireActivity().finish();
    }


    private boolean isEmailOk(String email) {
        if (!EmailValidator.getInstance().isValid((email))) {
            binding.edtEmail.setError(getString(R.string.error_email));
            return false;
        } else {
            binding.edtEmail.setError(null);
            return true;
        }
    }


    private boolean isPasswordOk(String password) {
        // Check if the password length is correct
        if (password.isEmpty()) {
            binding.edtPassword.setError(getString(R.string.error_password));
            return false;
        } else {
            binding.edtPassword.setError(null);
            return true;
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