package com.blackbox.pinspot.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.databinding.ActivityMainBinding;
import com.blackbox.pinspot.repository.user.IUserRepository;
import com.blackbox.pinspot.ui.welcome.LoginActivity;
import com.blackbox.pinspot.ui.welcome.UserViewModel;
import com.blackbox.pinspot.ui.welcome.UserViewModelFactory;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    //private TextView email;
    //private ActivityMainBinding binding;
    //private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        /*
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(this.getApplication());
        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class); */
        /*email = findViewById(R.id.textView_user_email);
        email.setText(userViewModel.getLoggedUser().getEmail());*/

        /*userViewModel.getUserMutableLiveData(userViewModel.getLoggedUser().getEmail(), "12345678", true).observe(
                this, users -> {
                    binding.textViewUserEmail.setText(userViewModel.getLoggedUser().getEmail());
                    binding.textViewUserId.setText(userViewModel.getLoggedUser().getIdToken());
                });*/


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapFragment, R.id.favPlacesFragment,
                R.id.profileFragment).build();

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);


        // TODO transfer in profile fragment

        /*binding.textViewUserEmail.setText(userViewModel.getLoggedUser().getEmail());
        binding.textViewUserId.setText(userViewModel.getLoggedUser().getIdToken());

        binding.buttonUserLogout.setOnClickListener(v -> {
            userViewModel.logout().observe(this, result -> {
                if (result.isSuccess()) {
                    *//*Snackbar.make(v,
                            "Logout completed successfully",
                            Snackbar.LENGTH_SHORT).show();*//*
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    this.finish();
                    *//*Navigation.findNavController(view).navigate(
                            R.id.action_fragment_settings_to_welcomeActivity);*//*
                } else {
                    Snackbar.make(v,
                            this.getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        });*/
    }
}