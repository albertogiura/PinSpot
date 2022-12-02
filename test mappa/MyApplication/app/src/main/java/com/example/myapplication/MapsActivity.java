package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
//import androidx.databinding.tool.store.Location;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ActivityResultLauncher<String> singlePermissionLauncher;
    private ActivityResultContracts.RequestPermission singlePermissionContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FusedLocationProviderClient fusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        singlePermissionContract = new ActivityResultContracts.RequestPermission();

        singlePermissionLauncher = registerForActivityResult(singlePermissionContract, isGranted -> {
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                Log.d(TAG, "Single permission has been granted");

            } else {
                // Explain to the user that the feature is unavailable because the feature requires
                // a permission that the user has denied. At the same time, respect
                // the user's decision. Don't link to system settings in an effort to convince the
                // user to change their decision.
                Log.d(TAG, "Single permission has not been granted");
            }
        });

        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            for(Map.Entry<String, Boolean> set : isGranted.entrySet()) {
                Log.d(TAG, set.getKey() + " " + set.getValue());
            }
            if (!isGranted.containsValue(false)) {
                Log.d(TAG, "All permission have been granted");

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private int getLocationLatitude() {
       int r = 0;
        boolean PermissionsStatus =
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;



        // Use multiplePermissionsStatus if you want to try the logic
        // of requesting more than one permission
        if (PermissionsStatus) {
            Log.d(TAG, "getLocation(): All permissions have been granted");
            // Doc is here: https://developer.android.com/training/location/retrieve-current
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                //r = (int) location.getLatitude();
                            }
                        }
                    });
        } else {
            Log.d(TAG, "One or more permissions have not been granted");
            // Use multiplePermissionLauncher and PERMISSIONS variable as argument of method launch
            // if you want to try the logic of requesting more than one permission
            singlePermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        return r;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        int lat = getLocationLatitude();

        mMap = googleMap;
        Object mLocationManager = this.getSystemService(LOCATION_SERVICE);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, 1);
        LatLng milano = new LatLng(45, 9);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(milano));
    }
}