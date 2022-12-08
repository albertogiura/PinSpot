package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button bottone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "è stato chiamato il metodo onCreate()");
        bottone = findViewById(R.id.signUp);
        //Button signUp =  findViewById(R.id.signUp);

        bottone.setOnClickListener(view -> {
            Intent intent = new Intent(this, sign_up.class);
            startActivity(intent);

        });
    }


}