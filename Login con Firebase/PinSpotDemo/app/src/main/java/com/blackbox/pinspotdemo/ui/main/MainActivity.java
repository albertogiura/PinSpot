package com.blackbox.pinspotdemo.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blackbox.pinspotdemo.R;
import com.blackbox.pinspotdemo.databinding.ActivityMainBinding;
import com.blackbox.pinspotdemo.repository.user.IUserRepository;
import com.blackbox.pinspotdemo.ui.welcome.LoginActivity;
import com.blackbox.pinspotdemo.ui.welcome.UserViewModel;
import com.blackbox.pinspotdemo.ui.welcome.UserViewModelFactory;
import com.blackbox.pinspotdemo.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    //private TextView email;
    private ActivityMainBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);*/
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(this.getApplication());
        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        /*email = findViewById(R.id.textView_user_email);
        email.setText(userViewModel.getLoggedUser().getEmail());*/

        /*userViewModel.getUserMutableLiveData(userViewModel.getLoggedUser().getEmail(), "12345678", true).observe(
                this, users -> {
                    binding.textViewUserEmail.setText(userViewModel.getLoggedUser().getEmail());
                    binding.textViewUserId.setText(userViewModel.getLoggedUser().getIdToken());
                });*/

        binding.textViewUserEmail.setText(userViewModel.getLoggedUser().getEmail());
        binding.textViewUserId.setText(userViewModel.getLoggedUser().getIdToken());

        binding.buttonUserLogout.setOnClickListener(v -> {
            userViewModel.logout().observe(this, result -> {
                if (result.isSuccess()) {
                    /*Snackbar.make(v,
                            "Logout completed successfully",
                            Snackbar.LENGTH_SHORT).show();*/
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    this.finish();
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