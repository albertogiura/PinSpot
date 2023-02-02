package com.blackbox.pinspot.ui.welcome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.blackbox.pinspot.R;

public class LoginActivity extends AppCompatActivity {

    //private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //binding = ActivityLoginBinding.inflate(getLayoutInflater());
        /*NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.welcome_nav_graph);
        NavController navController = navHostFragment.getNavController();*/
    }
}