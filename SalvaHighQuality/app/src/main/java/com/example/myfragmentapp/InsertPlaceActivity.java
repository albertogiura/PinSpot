package com.example.myfragmentapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*public class InsertPlaceActivity extends AppCompatActivity {

    Button button2;
    Button button3;
    Button downloadButton;
    EditText nome,descrizione;
    private ImageView image;
    private static final int REQUEST_CODE=1;
    private static final int Read_Permission=101;
    private static final int SELECT_IMAGE=100;
    String url = "api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}";
    String apikey = "4f6ec18ab9eb724adb869edca9cbbf63";
    LocationManager manager;
    LocationListener locationListener;

    private static final String DBREALTIME = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";
    private FirebaseDatabase realTime = FirebaseDatabase.getInstance(DBREALTIME);  //connecting to real-time DB
    private DatabaseReference myRefRealTime;  //reference to real-time DB

    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES); //connecting to storage DB
    private StorageReference myRefStorage = storage.getReference();  // creating reference to storage DB
    private StorageReference everestRef = myRefStorage.child("cucina.jpeg"); // creating reference to image
    private StorageReference everestImageRef = myRefStorage.child("images/everest.jpeg"); //creating reference to image path's

    private StorageReference pathReference = myRefStorage.child("images/mare.jpg");
    private static int CAMERA_PERMISSION_CODE = 100;

    ImageView imageView;
    Button takephoto;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertplace);
        Bundle extras = getIntent().getExtras();
        //byte[] byteArray = extras.getByteArray("picture");
        downloadButton=findViewById(R.id.buttonDownload);

        imageView = findViewById(R.id.myImage);
        takephoto = findViewById(R.id.camera);

       if(ContextCompat.checkSelfPermission(InsertPlaceActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(InsertPlaceActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);

        }
        if (iscamerapresent()) {
            Log.i("Camera phone", "Camera detected");
            camerapermission();
        } else {
            Log.i("Camera phone", "Camera not detected");
        }
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(InsertPlaceActivity.this, "there is no support this action",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            imageView.setImageBitmap(bitmap);

                        }
                    }
                });
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
        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        //image.setImageBitmap(bmp);
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


    private boolean iscamerapresent() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void camerapermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }



} */

public class InsertPlaceActivity extends AppCompatActivity {
    private static int CAMERA_PERMISSION_CODE = 100;
    private static final String DBREALTIME = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";
    private FirebaseDatabase realTime = FirebaseDatabase.getInstance(DBREALTIME);  //connecting to real-time DB
    private DatabaseReference myRefRealTime;  //reference to real-time DB

    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES); //connecting to storage DB
    private StorageReference myRefStorage = storage.getReference();  // creating reference to storage DB
    private StorageReference everestRef = myRefStorage.child("cell1.jpeg"); // creating reference to image
    private StorageReference everestImageRef = myRefStorage.child("images/cell2.jpeg"); //creating reference to image path's

    private StorageReference pathReference = myRefStorage.child("images/cell3.jpg");

    ImageView imageView;
    Button takephoto,home;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertplace2);

        imageView = findViewById(R.id.imageView);

        takephoto = findViewById(R.id.button);
        home = findViewById(R.id.home);

        if (iscamerapresent()) {
            Log.i("Camera phone", "Camera detected");
            camerapermission();
        } else {
            Log.i("Camera phone", "Camera not detected");
        }

        myRefRealTime = realTime.getReference("istanza2"); //key

        //initializing object
        String key = realTime.getReference("istanza2").push().getKey();
        LatitudeLongitude l=new LatitudeLongitude(55.0,55.0);
        l=new LatitudeLongitude(55.0,55.0);
        Pin pin = new Pin(l,"title", key,10); //data

        // uploading data key-value into real-time DB
        myRefRealTime.setValue(pin);
        // uploading data key-value into real-time DB
        myRefRealTime.setValue(pin);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            imageView.setImageBitmap(bitmap);

                        }
                    }
                });
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(InsertPlaceActivity.this, "there is no support this action",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
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
                        //textView.setText(result.toString());

                        // uploading everest picture to storage DB
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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


    }

    private boolean iscamerapresent() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void camerapermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }
}