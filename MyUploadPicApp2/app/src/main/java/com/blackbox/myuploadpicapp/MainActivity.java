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

import com.blackbox.myuploadpicapp.model.user.LatitudeLongitude;
import com.blackbox.myuploadpicapp.model.user.Pin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button uploadButton;
    Button downloadPinButton;
    TextView pinFromDBTextView;
    TextView pinTitles;
    TextView pinPositions;
    Button buttonSwitchToInsertPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = findViewById(R.id.uploadButton);
        downloadPinButton = findViewById(R.id.buttonGetPinFromDB);
        pinFromDBTextView = findViewById(R.id.pinFromDB_textview);
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
                LatitudeLongitude l= new LatitudeLongitude(55.0,55.0);
                Pin pin = new Pin(l, "title", "pippo",10);

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