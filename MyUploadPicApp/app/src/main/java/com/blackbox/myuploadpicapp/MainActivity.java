package com.blackbox.myuploadpicapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blackbox.myuploadpicapp.model.user.LatitudeLongitude;
import com.blackbox.myuploadpicapp.model.user.Marker;
import com.blackbox.myuploadpicapp.model.user.Pin;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//inizio import random copiaincollati
//RIMUOVERE TUTTI QUELLI INUTILI
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button uploadButton;
    Button downloadPinButton;
    TextView pinFromDBTextView;
    TextView pinTitles;
    TextView pinPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = findViewById(R.id.uploadButton);
        downloadPinButton = findViewById(R.id.buttonGetPinFromDB);
        pinFromDBTextView = findViewById(R.id.pinFromDB_textview);
        pinTitles = findViewById(R.id.pinTitles_textview);
        pinPositions = findViewById(R.id.pinPos_textview);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //copia incolla brutale della doc
                double lat = 55.5074;
                double lng = 9.1279;
                String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
/*
                Map<String, Object> updates = new HashMap<>();
                updates.put("geohash", hash);
                updates.put("lat", lat);
                updates.put("lng", lng);

                DocumentReference londonRef = db.collection("cities").document("ON");
                londonRef.update(updates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
               //*/
                //fine brutalità
                LatitudeLongitude l= new LatitudeLongitude(55.0,55.0);
                Pin pin = new Pin(lat,lng, hash,"title", "pippo",10);

                db.collection("pins3")
                        .add(pin)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });

        downloadPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //METODO GET MARKERLIST
                final GeoLocation center = new GeoLocation(55.5074, 9.127);
                final double radiusInM = 5 * 1000;

// Each item in 'bounds' represents a startAt/endAt pair. We have to issue
// a separate query for each pair. There can be up to 9 pairs of bounds
// depending on overlap, but in most cases there are 4.

                List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
                final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
                for (GeoQueryBounds b : bounds) {
                    Query q = db.collection("pins3")
                            .orderBy("geoHash")
                            .startAt(b.startHash)
                            .endAt(b.endHash);

                    tasks.add(q.get());
                }

                // Collect all the query results together into a single list
                Tasks.whenAllComplete(tasks)
                        .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Task<?>>> t) {
                                List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                                for (Task<QuerySnapshot> task : tasks) {
                                    QuerySnapshot snap = task.getResult();
                                    for (DocumentSnapshot doc : snap.getDocuments()) {
                                        double lat = doc.getDouble("lat");
                                        double lng = doc.getDouble("lon");

                                        // We have to filter out a few false positives due to GeoHash
                                        // accuracy, but most will match
                                        GeoLocation docLocation = new GeoLocation(lat, lng);
                                        double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                        if (distanceInM <= radiusInM) {
                                            matchingDocs.add(doc);
                                        }
                                    }
                                }
                                 int size =  matchingDocs.size();
                                ArrayList<Marker> markerlist = new ArrayList<Marker>();
                                for(int i =0 ;i<size;++i){
                                    double lat =  matchingDocs.get(i).getDouble("lat");
                                    double lon =  matchingDocs.get(i).getDouble("lon");
                                    String title = matchingDocs.get(i).getString("title");
                                    String idpin = matchingDocs.get(i).getId();

                                    markerlist.add(new Marker(lat, lon, title, idpin ));
                                }
                                String testo = "";
                                for(int i =0 ;i<size;++i){
                                    testo = testo + markerlist.get(i).toString();

                                }

                                pinTitles.setText(testo);
                                //pinTitles.setText(Integer.toString(i));

                            }
                        });
                //METODO GETPIN
/*
                db.collection("pins2")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String result = "", titles = "", pos = "";
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        result += document.getData();
                                        titles += document.getString("title") + " ";
                                        //pos += document.get("position");

                                        //firstPinTitleValue.setText(document.get("title").toString());
                                    }
                                    pinFromDBTextView.setText(result);
                                    pinTitles.setText(titles);
                                    pinPositions.setText(pos);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });*/
            }
        });

    }
}