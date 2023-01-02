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

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";
    private Button bottone;
    private FirebaseDatabase database = FirebaseDatabase.getInstance(URL);
    private  DatabaseReference myRef;
    private TextView textVieww;
    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES);
    private StorageReference storageRef = storage.getReference();
    private StorageReference everestRef = storageRef.child("everest.jpeg");
    private StorageReference everestImageRef = storageRef.child("images/everest.jpeg");
    private ImageView imageView;
    private StorageReference pathReference = storageRef.child("images/mare.jpg");
    // Create a reference to a file from a Google Cloud Storage URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create();
       /* myRef = database.getReference("messagi");//key
        myRef.setValue("Hello, World!");//value
        myRef = database.getReference("messagi1");
        myRef.setValue("Hello, World!");myRef = database.getReference("messagi2");

        myRef = database.getReference("messadgi");
        myRef.setValue("Hello, Wofirebased!");*/
        myRef = database.getReference("istanza");
        LatitudeLongitude l=new LatitudeLongitude(55.0,55.0);
        myRef = database.getReference("istanzo");
        l=new LatitudeLongitude(55.0,55.0);
        Pin pin = new Pin(l,"io","èièèo",-5);
        myRef.setValue(pin);

        imageView = findViewById(R.id.everest);
        textVieww = findViewById(R.id.textView);

        bottone = findViewById(R.id.button2);
        bottone.setOnClickListener(new View.OnClickListener(){
            String data = null;
            Pin result = new Pin();
            @Override
            public void onClick(View view){
                 //create();
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

                        // Get Post object and use the values to update the UI
                        result = dataSnapshot.getValue(Pin.class);
                        Pin prova=result;
                        textVieww.setText(result.toString());
                        // ..
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = everestRef.putBytes(data);
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
                myRef.addValueEventListener(postListener);

            }

        });
    }



}