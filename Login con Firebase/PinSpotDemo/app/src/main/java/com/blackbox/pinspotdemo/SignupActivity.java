package com.blackbox.pinspotdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.blackbox.pinspotdemo.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}