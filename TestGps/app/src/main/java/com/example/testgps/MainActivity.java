package com.example.testgps;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> singlePermissionLauncher;
    private ActivityResultContracts.RequestPermission singlePermissionContract;

/*
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;

    private final String[] PERMISSIONS = {

            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
*/
    private FusedLocationProviderClient fusedLocationClient;


    private Button bottone;
    private TextView latText;
    private TextView longText;

    public LatLng mypos = new LatLng(0,0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        singlePermissionContract = new ActivityResultContracts.RequestPermission();
        //multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();

        singlePermissionLauncher = registerForActivityResult(singlePermissionContract, isGranted -> {
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                Log.d(TAG, "Single permission has been granted");
                getLocation();
            } else {
                // Explain to the user that the feature is unavailable because the feature requires
                // a permission that the user has denied. At the same time, respect
                // the user's decision. Don't link to system settings in an effort to convince the
                // user to change their decision.
                Log.d(TAG, "Single permission has not been granted");
            }
        });
      /*  multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            for(Map.Entry<String, Boolean> set : isGranted.entrySet()) {
                Log.d(TAG, set.getKey() + " " + set.getValue());
            }
            if (!isGranted.containsValue(false)) {
                Log.d(TAG, "All permission have been granted");
                getLocation();
            }
        });
*/
        bottone = findViewById(R.id.button);
        latText = findViewById(R.id.textView);
        longText = findViewById(R.id.textView2);
        bottone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                if(mypos!=null){
                    //int i = 1;
                    latText.setText(Integer.toString((int) mypos.latitude));
                    longText.setText(Integer.toString((int) mypos.longitude));
                }

            }
        });
    }
    private void getLocation() {

        boolean singlePermissionsStatus =
               ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
/*
        boolean multiplePermissionsStatus =
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
*/
        // Use multiplePermissionsStatus if you want to try the logic
        // of requesting more than one permission
        if (singlePermissionsStatus) {
            Log.d(TAG, "getLocation(): All permissions have been granted");
            // Doc is here: https://developer.android.com/training/location/retrieve-current
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this,  new OnSuccessListener<Location>() {
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Log.d(TAG, location.getLatitude() + " " + location.getLongitude());
                                LatLng milano = new LatLng(location.getLatitude(), location.getLongitude());

                                mypos = milano;
                                //int i = 1;
                            }
                        }
                    });
        } else {
            Log.d(TAG, "One or more permissions have not been granted");
            // Use multiplePermissionLauncher and PERMISSIONS variable as argument of method launch
            // if you want to try the logic of requesting more than one permission
            singlePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            //multiplePermissionLauncher.launch(PERMISSIONS);
        }

    }

}