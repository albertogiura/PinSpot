package com.blackbox.myuploadpicapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blackbox.myuploadpicapp.model.user.LatitudeLongitude;
import com.blackbox.myuploadpicapp.model.user.Pin;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button uploadButton, manualUploadButton;
    Button downloadPinButton;
    TextView pinFromDBTextView;
    TextView pinTitles;
    TextView pinPositions;
    TextInputEditText latEdt, longEdt;
    Button buttonSwitchToInsertPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = findViewById(R.id.uploadButton);
        manualUploadButton = findViewById(R.id.manualUploadButton);
        downloadPinButton = findViewById(R.id.buttonGetPinFromDB);
        pinFromDBTextView = findViewById(R.id.pinFromDB_textview);
        latEdt = findViewById(R.id.lat_tiet);
        longEdt = findViewById(R.id.long_tiet);
        pinTitles = findViewById(R.id.pinTitles_textview);
        pinPositions = findViewById(R.id.pinPos_textview);
        buttonSwitchToInsertPin = findViewById(R.id.buttonGoToInsertPinActivity);

        buttonSwitchToInsertPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertPinActivity.class);
                startActivity(intent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LatitudeLongitude l= new LatitudeLongitude(55.0,55.0);
                double lat = 55.5074;
                double lng = 9.1279;
                String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
                Pin pin = new Pin(lat,lng, hash,"title", "pippo",10);

                db.collection("pins")
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

        manualUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latEdt.getText().length() > 0 && longEdt.getText().length() > 0){
                    //LatitudeLongitude l= new LatitudeLongitude(Double.valueOf(String.valueOf(latEdt.getText())), Double.valueOf(String.valueOf(longEdt.getText())));

                    double lat = Double.valueOf(String.valueOf(latEdt.getText()));
                    double lng = Double.valueOf(String.valueOf(longEdt.getText()));
                    String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
                    Pin pin = new Pin(lat, lng, hash,"title", "pippo",10);
                    //String s = "Latitudine: "+Double.valueOf(String.valueOf(latEdt.getText()))+" Longitudine: "+Double.valueOf(String.valueOf(longEdt.getText()));
                    String s = "Latitudine: "+lat+" Longitudine: "+lng;
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

                    db.collection("pins4")
                            .add(pin)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(MainActivity.this, "Pin added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(MainActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                }



            }
        });

        downloadPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("pins")
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
                        });
            }
        });

    }
}