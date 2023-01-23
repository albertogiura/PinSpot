package com.blackbox.pinspot.ui.welcome;

import static com.blackbox.pinspot.util.Constants.EMAIL_ADDRESS;
import static com.blackbox.pinspot.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.blackbox.pinspot.util.Constants.ID_TOKEN;
import static com.blackbox.pinspot.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.blackbox.pinspot.util.Constants.INVALID_USER_ERROR;
import static com.blackbox.pinspot.util.Constants.PASSWORD;
import static com.blackbox.pinspot.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blackbox.pinspot.R;

import com.blackbox.pinspot.databinding.FragmentLoginBinding;
import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.User;
import com.blackbox.pinspot.repository.user.IUserRepository;
import com.blackbox.pinspot.ui.main.MainActivity;
import com.blackbox.pinspot.util.DataEncryptionUtil;
import com.blackbox.pinspot.util.ServiceLocator;
import com.blackbox.pinspot.util.SharedPreferencesUtil;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final boolean USE_NAVIGATION_COMPONENT = true;
    private FragmentLoginBinding binding;

    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;

    private UserViewModel userViewModel;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private DataEncryptionUtil dataEncryptionUtil;

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
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());

        oneTapClient = Identity.getSignInClient(requireActivity());
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
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
                Log.d(TAG, "result.getResultCode() == Activity.RESULT_OK");
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(activityResult.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate with Firebase.
                        userViewModel.getGoogleUserMutableLiveData(idToken).observe(getViewLifecycleOwner(), authenticationResult -> {
                            if (authenticationResult.isSuccess()) {
                                User user = ((Result.UserResponseSuccess) authenticationResult).getData();
                                saveLoginData(user.getEmail(), null, user.getIdToken());
                                userViewModel.setAuthenticationError(false);
                                retrieveUserInformationAndStartActivity(user, R.id.action_loginFragment_to_mainActivity);
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
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void retrieveUserInformationAndStartActivity(User user, int destination) {
        binding.progressBar.setVisibility(View.VISIBLE);
        startActivityBasedOnCondition(MainActivity.class, destination);

        //Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainActivity);

        /*userViewModel.getUserFavoriteNewsMutableLiveData(user.getIdToken()).observe(
                getViewLifecycleOwner(), userFavoriteNewsRetrievalResult -> {
                    progressIndicator.setVisibility(View.GONE);
                    // Cambiare destinazione
                    // startActivityBasedOnCondition(NewsPreferencesActivity.class, destination);
                }
        );*/
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
            SharedPreferencesUtil sharedPreferencesUtil =
                    new SharedPreferencesUtil(requireActivity().getApplication());

                startActivityBasedOnCondition(MainActivity.class,
                        R.id.action_loginFragment_to_mainActivity);

            /*
            if (sharedPreferencesUtil.readStringData(SHARED_PREFERENCES_FILE_NAME) != null &&
                    sharedPreferencesUtil.readStringSetData(SHARED_PREFERENCES_FILE_NAME) != null) {
                // Da cambiare destinazione
                startActivityBasedOnCondition(MainActivityWithBottomNavigationView.class,
                        R.id.navigate_to_mainActivityWithBottomNavigationView);
            } else {
            startActivityBasedOnCondition(MainActivity.class,
                    R.id.action_loginFragment_to_mainActivity);
            // Cambiare destinazione
                }
             */
        }


        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());

        try {
            Log.d(TAG, "Email address from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS));
            Log.d(TAG, "Password from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD));
            Log.d(TAG, "Token from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN));
            Log.d(TAG, "Login data from encrypted file: " + dataEncryptionUtil.
                    readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

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
                                User user = ((Result.UserResponseSuccess) result).getData();
                                saveLoginData(email, password, user.getIdToken());
                                userViewModel.setAuthenticationError(false);
                                //Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainActivity);
                                retrieveUserInformationAndStartActivity(user, R.id.action_loginFragment_to_mainActivity);
                            } else {
                                userViewModel.setAuthenticationError(true);
                                binding.progressBar.setVisibility(View.GONE);
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) result).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                    /*userViewModel.getUserMutableLiveData(email, password, true).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    saveLoginData(email, password, user.getIdToken());
                                    userViewModel.setAuthenticationError(false);
                                    //Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainActivity);
                                    retrieveUserInformationAndStartActivity(user, R.id.action_loginFragment_to_mainActivity);
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    binding.progressBar.setVisibility(View.GONE);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });*/
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
                        Log.d(TAG, "onSuccess from oneTapClient.beginSignIn(BeginSignInRequest)");
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
                String email;
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = String.valueOf(binding.edtEmail.getText());
                if(emailAddress!=null && isEmailOk(emailAddress)){
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "EMAIL SENT",
                                                    Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

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
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
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


    private void saveLoginData(String email, String password, String idToken) {
        try {
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS, email);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD, password);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN, idToken);

            if (password != null) {
                dataEncryptionUtil.writeSecreteDataOnFile(ENCRYPTED_DATA_FILE_NAME,
                        email.concat(":").concat(password));
            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

}