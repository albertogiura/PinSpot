package com.example.testfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.testfirebase.User.LatitudeLongitude;
import com.example.testfirebase.User.Pin;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    private Button bottone;
    private FirebaseDatabase database = FirebaseDatabase.getInstance(URL);
    private  DatabaseReference myRef;
    private TextView textVieww;

    public void create(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create();
        // Write a message to the database

       /* myRef = database.getReference("messagi");//key
        myRef.setValue("Hello, World!");//value
        myRef = database.getReference("messagi1");
        myRef.setValue("Hello, World!");myRef = database.getReference("messagi2");

        myRef = database.getReference("messadgi");
        myRef.setValue("Hello, Wofirebased!");*/
        myRef = database.getReference("istanza");
        LatitudeLongitude l=new LatitudeLongitude(55.0,55.0);
        Pin pin = new Pin(l,"io","tuammamma",-5);
        myRef.setValue(pin);


        textVieww = findViewById(R.id.textView);

        bottone = findViewById(R.id.button2);
        bottone.setOnClickListener(new View.OnClickListener(){
            String data = null;
            Pin result = new Pin();
            @Override
            public void onClick(View view){
                 //create();
                ValueEventListener postListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        result = dataSnapshot.getValue(Pin.class);
                        Pin prova=result;
                        textVieww.setText(result.toString());
                        // ..
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                      //  Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                };
                myRef.addValueEventListener(postListener);

            }

        });
    }



}