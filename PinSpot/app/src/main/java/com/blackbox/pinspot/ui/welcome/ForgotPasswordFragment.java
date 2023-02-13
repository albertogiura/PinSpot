package com.blackbox.pinspot.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.data.repository.user.IUserRepository;
import com.blackbox.pinspot.databinding.FragmentForgotPasswordBinding;
import com.blackbox.pinspot.databinding.FragmentLoginBinding;
import com.blackbox.pinspot.databinding.FragmentSignUpBinding;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private FragmentForgotPasswordBinding binding;
    private UserViewModel userViewModel;

    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository();
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.passwordRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = String.valueOf(binding.recoveryEmailEdt.getText()).trim();
                if (isEmailOk(emailAddress)) {
                    userViewModel.forgotPassword(emailAddress).observe(getViewLifecycleOwner(), result -> {
                        if (result.isSuccess()) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                    "Password recovery email sent",
                                    Snackbar.LENGTH_SHORT).show();
                            Navigation.findNavController(requireView()).navigateUp();

                        } else {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                    "Error. Password recovery email sent not sent",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            "This is not a valid email address for recovery",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigateUp();
            }
        });
    }

    private boolean isEmailOk(String email) {
        if (!EmailValidator.getInstance().isValid((email))) {
            binding.recoveryEmailEdt.setError(getString(R.string.error_email));
            return false;
        } else {
            binding.recoveryEmailEdt.setError(null);
            return true;
        }
    }
}