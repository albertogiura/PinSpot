package com.example.myloginapp;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class DynamicColor extends Application {
    @Override
    public void onCreate() {
// Apply dynamic color
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}