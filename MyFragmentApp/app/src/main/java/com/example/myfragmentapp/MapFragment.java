package com.example.myfragmentapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ActivityResultLauncher<String> singlePermissionLauncher;
    private ActivityResultContracts.RequestPermission singlePermissionContract;

    private FusedLocationProviderClient fusedLocationClient;
    public LatLng mypos = new LatLng(0,0);
    private double lat =0;
    private  double lon =0;

    public ArrayList<myMarker> markerlist = new ArrayList<myMarker>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        singlePermissionContract = new ActivityResultContracts.RequestPermission();
        //multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();

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

        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    //public LatLng myPos;//la mia posizione
    //necessari per il permesso


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        singlePermissionContract = new ActivityResultContracts.RequestPermission();
        MapView mapView = view.findViewById(R.id.mapv);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();
        mapView.getMapAsync(this);


    }

    private Marker marker;//marker di test

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        //googleMap.setMaxZoomPreference(20.0f);
        //googleMap.setMinZoomPreference(14.0f);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            getLocation(googleMap);
            return;
        }
        // Turn on the My Location layer and the related control on the map.

        getLocation(googleMap);
        googleMap.setMyLocationEnabled(true);


        markerlist.add(new myMarker(45.01, 9, "pin1", "id1"));
        markerlist.add(new myMarker(45.02, 9, "pin2", "id2"));
        markerlist.add(new myMarker(45.03, 9, "pin3", "id3"));
        int size =  markerlist.size();
        for(int i =0 ;i<size;++i) {
            //POSSIBILE INIZIO METODO ISTANZIAZIONE MARKER

            LatLng markerpos = new LatLng(markerlist.get(i).getLat(),markerlist.get(i).getLon());
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(markerpos)
                    .title(markerlist.get(i).getTitle())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//cambio colore IMPORT NECESSARIO
                    //BitmapDescriptorFactory.fromResource(R.drawable.arrow) cosi possiamo mettere la mappa
                    .alpha(0.9f)//cambio opacità
                    .flat(true)//In teoria dovremmo averlo così ma bho non cambia nulla a prima vista
            );
            //marker.setTag(i);
            marker.setTag(markerlist.get(i).getIdPin());
            googleMap.setOnMarkerClickListener(this);
        }//importante settare il listener NON DIMENTICARE
       /* Location myLocation = googleMap.getMyLocation();//è deprecato, chiedere a ginelli se va bene
       LatLng myLatLng = new LatLng(myLocation.getLatitude(),
              myLocation.getLongitude());
       */ //googleMap.moveCamera(CameraUpdateFactory.newLatLng(milano));
        getLocation(googleMap);


      //per fare zoom con animazione
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
       /*
        //qui è la logica del click, faccio solo un toast per ora nulla di che, ma qui si carica il fragment
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();//questo è un tag che gli viene aggiunto come prorietà del marker

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(requireActivity(),
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times."
                    +"idmarker: ",
                    Toast.LENGTH_SHORT).show();
        }
*/
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).

        //ON CLICK ALTERNATIVO
        String idpin = (String) marker.getTag();

        Toast.makeText(requireActivity(),
                marker.getTitle() +
                        " pintag: " + idpin,
                Toast.LENGTH_SHORT).show();
        //Da qui accediamo al pin con il tag del pin
        return false;
    }
    private void getLocation(GoogleMap map) {

        boolean singlePermissionsStatus =
                ActivityCompat.checkSelfPermission(requireContext(),
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
                    .addOnSuccessListener(requireActivity(),  new OnSuccessListener<Location>() {
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Log.d(TAG, location.getLatitude() + " " + location.getLongitude());
                                LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());

                                mypos = currentPos;
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(mypos, 14.0f));
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

    private void GenerateMarkers()
    {

    }
}