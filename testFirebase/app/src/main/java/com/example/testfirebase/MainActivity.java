package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testfirebase.User.LatitudeLongitude;
import com.example.testfirebase.User.Pin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String DBREALTIME = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";

    private Button button;
    private TextView textView;
    private ImageView imageView;

    private FirebaseDatabase realTime = FirebaseDatabase.getInstance(DBREALTIME);  //connecting to real-time DB
    private DatabaseReference myRefRealTime;  //reference to real-time DB

    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES); //connecting to storage DB
    private StorageReference myRefStorage = storage.getReference();  // creating reference to storage DB
    private StorageReference everestRef = myRefStorage.child("everest.jpeg"); // creating reference to image
    private StorageReference everestImageRef = myRefStorage.child("images/everest.jpeg"); //creating reference to image path's

    private StorageReference pathReference = myRefStorage.child("images/mare.jpg");
    // Create a reference to a file from a Google Cloud Storage URI

    public void createObjects(){
        // initialize UI's object and DB utilities
        button = findViewById(R.id.button2);
        imageView = findViewById(R.id.everest);
        textView = findViewById(R.id.textView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createObjects();

        myRefRealTime = realTime.getReference("istanza"); //key

        //initializing object
        String key = realTime.getReference("istanza").push().getKey();
        LatitudeLongitude l=new LatitudeLongitude(55.0,55.0);
        l=new LatitudeLongitude(55.0,55.0);
        Pin pin = new Pin(l,"title", key,10); //data

        // uploading data key-value into real-time DB
        myRefRealTime.setValue(pin);

        button.setOnClickListener(new View.OnClickListener(){
            String data = null;
            Pin result = new Pin();
            @Override
            public void onClick(View view){

                //downloading sea picture from storage DB
                final long ONE_MEGABYTE = 1024 * 1024;
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(decodedByte);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                ValueEventListener postListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // downloading object(s) from real-time DB
                        result = dataSnapshot.getValue(Pin.class);
                        textView.setText(result.toString());

                        // uploading everest picture to storage DB
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = everestRef.putBytes(data); // uploading here
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                      //  Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                };
                myRefRealTime.addValueEventListener(postListener);

            }

        });
    }



}