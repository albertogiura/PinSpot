package com.example.mymapwithmapview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//documentazione marker
//https://developers.google.com/maps/documentation/android-sdk/marker#maps_android_markers_tag_sample-java
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {//interfaccia necessaria per il listener, serve un import che fa andorid studio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        setContentView(R.layout.activity_main);

/*        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        MapView mapView = findViewById(R.id.mapv);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        // riprovare senza onResume() all'interno di un fragment

    }
    private Marker marker;
    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng milano = new LatLng(45, 9);

        marker = googleMap.addMarker(new MarkerOptions()
                .position(milano)
                .title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//cambio colore IMPORT NECESSARIO
                //BitmapDescriptorFactory.fromResource(R.drawable.arrow) cosi possiamo mettere la mappa
                .alpha(0.9f)//cambio opacità
                .flat(true)//In teoria dovremmo averlo così ma bho non cambia nulla a prima vista
                );
        marker.setTag(0);
        googleMap.setOnMarkerClickListener(this); //importante settare il listener NON DIMENTICARE

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(milano));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(milano, 20.0f));//per fare zoom con animazione

    }
    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(@NonNull final Marker marker) {
        //qui è la logica del click, faccio solo un toast per ora nulla di che, ma qui si carica il fragment
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();//questo è un tag che gli viene aggiunto come prorietà del marker

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}