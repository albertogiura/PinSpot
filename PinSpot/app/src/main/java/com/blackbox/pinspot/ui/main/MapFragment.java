package com.blackbox.pinspot.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.model.LatitudeLongitude;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = view.findViewById(R.id.mapv);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private Marker marker;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatitudeLongitude pin = new LatitudeLongitude(45.0, 9.0);

        marker = googleMap.addMarker(new MarkerOptions()
                .position(pin.getLatLngObj())
                .title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//cambio colore IMPORT NECESSARIO
                //BitmapDescriptorFactory.fromResource(R.drawable.arrow) cosi possiamo mettere la mappa
                .alpha(0.9f)//cambio opacità
                .flat(true)//In teoria dovremmo averlo così ma bho non cambia nulla a prima vista
        );
        marker.setTag(0);
        googleMap.setOnMarkerClickListener(this); //importante settare il listener NON DIMENTICARE

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(milano));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin.getLatLngObj(), 20.0f));//per fare zoom con animazione
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        //qui è la logica del click, faccio solo un toast per ora nulla di che, ma qui si carica il fragment
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();//questo è un tag che gli viene aggiunto come prorietà del marker

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(requireActivity(),
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