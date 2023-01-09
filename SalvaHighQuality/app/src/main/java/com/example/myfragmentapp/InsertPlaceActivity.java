package com.example.myfragmentapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.lang.Object;

import java.io.OutputStream;
import java.util.Objects;
import com.example.myfragmentapp.User.LatitudeLongitude;
import com.example.myfragmentapp.User.Pin;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.example.myfragmentapp.User.LatitudeLongitude;
import com.example.myfragmentapp.User.Pin;
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

import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;


public class InsertPlaceActivity extends AppCompatActivity {

    Button button2;
    Button button3;
    Button downloadButton;
    private ImageView image;
    private static final int REQUEST_CODE=1;
    private static final int Read_Permission=101;
    private static final int SELECT_IMAGE=100;

    private static final String DBREALTIME = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";
    private FirebaseDatabase realTime = FirebaseDatabase.getInstance(DBREALTIME);  //connecting to real-time DB
    private DatabaseReference myRefRealTime;  //reference to real-time DB

    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES); //connecting to storage DB
    private StorageReference myRefStorage = storage.getReference();  // creating reference to storage DB
    private StorageReference everestRef = myRefStorage.child("cucina.jpeg"); // creating reference to image
    private StorageReference everestImageRef = myRefStorage.child("images/everest.jpeg"); //creating reference to image path's

    private StorageReference pathReference = myRefStorage.child("images/mare.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertplace);
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");
        downloadButton=findViewById(R.id.buttonDownload);


       if(ContextCompat.checkSelfPermission(InsertPlaceActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(InsertPlaceActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);

        }
        myRefRealTime = realTime.getReference("istanza"); //key

        //initializing object
        String key = realTime.getReference("istanza").push().getKey();
        LatitudeLongitude l=new LatitudeLongitude(55.0,55.0);
        l=new LatitudeLongitude(55.0,55.0);
        Pin pin = new Pin(l,"title", key,10); //data

        // uploading data key-value into real-time DB
        myRefRealTime.setValue(pin);

        button3=findViewById(R.id.gallery);
        button3.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent,"Select Image"),
                    SELECT_IMAGE
            );
        });
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        image =findViewById(R.id.myImage);
        image.setImageBitmap(bmp);
        button2=findViewById(R.id.buttonHome);
        button2.setOnClickListener(new View.OnClickListener() {
            String data = null;
            Pin result = new Pin();
            @Override
            public void onClick(View view) {
                //downloading sea picture from storage DB
                final long ONE_MEGABYTE = 1024 * 1024;
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image.setImageBitmap(decodedByte);
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
                        //textView.setText(result.toString());

                        // uploading everest picture to storage DB
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        everestRef = myRefStorage.child(pin.getLink()+".jpeg");
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
                Intent openHome = new Intent(InsertPlaceActivity.this, MainActivity.class);
                startActivity(openHome);
            }
        });

        downloadButton.setOnClickListener(view -> saveImage());


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if( requestCode== REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                saveImage();
            }else{
                Toast.makeText(InsertPlaceActivity.this, "Please provide required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage() {

       Uri images;
        ContentResolver contentResolver= getContentResolver();

       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
       }else{
            images= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
         }

        ContentValues contentValues= new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis()+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/"+ "images/*");

        Uri uri= contentResolver.insert(images, contentValues);

       try {
            BitmapDrawable bitmapDrawable= (BitmapDrawable) image.getDrawable();
            Bitmap bitmap=bitmapDrawable.getBitmap();


            OutputStream outputStream= contentResolver.openOutputStream((Uri) Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            Objects.requireNonNull(outputStream);

            Toast.makeText(this, "Images Saved Successfully", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(this, "Images not Saved ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_IMAGE && null !=data){
            Uri uri= data.getData();
            image.setImageURI(uri);
        }
    }


}