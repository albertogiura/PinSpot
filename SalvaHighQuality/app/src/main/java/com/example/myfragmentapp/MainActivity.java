package com.example.myfragmentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfragmentapp.util.Permission;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import com.example.myfragmentapp.User.LatitudeLongitude;
import com.example.myfragmentapp.User.Pin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
    private Button button1;
    private ImageView myImage1;
    private String currentPhotoPath;


    private Button button;
    private TextView textView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapFragment, R.id.favPlacesFragment,
                R.id.profileFragment).build();

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);
        button1=findViewById(R.id.button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String filename="photo";
                File storageDirectory=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile= File.createTempFile(filename, ".jpeg", storageDirectory);
                    currentPhotoPath=imageFile.getAbsolutePath();
                    Uri imageUri=FileProvider.getUriForFile(MainActivity.this,
                            "com.example.myfragmentapp",imageFile);
                    Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(cameraIntent, REQUEST_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/


                Intent openInsertPlace = new Intent(MainActivity.this, InsertPlaceActivity.class);
                startActivity(openInsertPlace);

            }
        });
    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        /*Bitmap photo= BitmapFactory.decodeFile(currentPhotoPath);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);


        byte[] byteArray = stream.toByteArray();*/
        //Intent openInsertPlace = new Intent(MainActivity.this, provainsertplace.class);
        //openInsertPlace.putExtra("picture", byteArray);
        //startActivity(openInsertPlace);



        //}





}