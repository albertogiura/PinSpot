package com.blackbox.myuploadpicapp;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blackbox.myuploadpicapp.databinding.ActivityInsertPinBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class InsertPinActivity extends AppCompatActivity {

    private static final String TAG = InsertPinActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST_CODE = 1000;

    private ActivityInsertPinBinding binding;
    private String currentPhotoPath = "";

    private ActivityResultLauncher<String[]> multiplePermissionLauncher;
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private ActivityResultLauncher<Uri> mTakePicture;
    Uri tempImageUri;

    // Firebase Storage
    private static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";
    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES);
    private StorageReference storageRef = storage.getReference();

    private final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_insert_pin);
        binding = ActivityInsertPinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();

        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            for(Map.Entry<String, Boolean> set : isGranted.entrySet()) {
                Log.d(TAG, set.getKey() + " " + set.getValue());
            }
            if (!isGranted.containsValue(false)) {
                Log.d(TAG, "All permission have been granted");
                takePicture();
            }
        });

        tempImageUri = initTempUri();
        //registerTakePictureLauncher();

        mTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                result -> {

                    if (result) {
                        binding.imageView.setImageURI(null); // This seems necessary for the image to change if the photo is taken again
                        binding.imageView.setImageURI(tempImageUri); // Retrieve the image contained in the specified URI as new picture for the imageView

                        // Retrieve taken photo from imageView, put it in a Bitmap and save picture locally
                        BitmapDrawable drawable = (BitmapDrawable) binding.imageView.getDrawable();
                        Bitmap bmp = drawable.getBitmap();
                        saveImageToExternalStorage(UUID.randomUUID().toString(), bmp);

                        // Enable upload button
                        binding.uploadPicButton.setEnabled(true);
                    }
                    else
                    {
                        // Just in case the user ended up not taking a photo
                        Toast.makeText(this,"Operation cancelled", Toast.LENGTH_SHORT).show();
                    }


                });

        /*binding.takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();
            }
        });*/

        binding.takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();
            }
        });


        /*
        mTakePicture = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {


                    }
                });
         */

        binding.uploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.uploadProgressBar.setVisibility(View.VISIBLE);
                uploadPicture();
            }
        });

    }

    private void uploadPicture(){
        // uploading everest picture to storage DB
        Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //takenPhotoRef = storageRef.child(pin.getLink()+".jpeg"); TODO

        StorageReference takenPhotoRef = storageRef.child("caricata.jpeg");
        UploadTask uploadTask = takenPhotoRef.putBytes(data); // uploading here
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(InsertPinActivity.this,"Upload error", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                binding.uploadProgressBar.setVisibility(View.GONE);
                Toast.makeText(InsertPinActivity.this,"Upload completed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Uri initTempUri(){
        // Setup a temporary file to save taken picture
        File tempImagesDir = new File(getApplicationContext().getFilesDir(), "temp_images");
        if(!tempImagesDir.exists()){
            tempImagesDir.mkdir();
        }
        File tempImage = new File(tempImagesDir, "temp_image.jpg");
        return FileProvider.getUriForFile(getApplicationContext(), "com.blackbox.myuploadpicapp.fileprovider", tempImage);
    }

    /*private void registerTakePictureLauncher(){


        binding.takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTakePicture.launch(tempImageUri);
            }
        });
    }*/

    private void takePicture() {
        // Using a boolean variable to determine if permissions have been granted
        boolean multiplePermissionsStatus =
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (multiplePermissionsStatus) {
            Log.d(TAG, "All permissions needed to take a picture have been granted");
            // If all permissions have been granted, execute the following logic

            mTakePicture.launch(tempImageUri); // Call smartphone camera with the file path that has to be used to save the picture

            /*mTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                    result -> {
                        binding.imageView.setImageURI(tempImageUri);
                    });*/

        } else {
            Log.d(TAG, "One or more permissions have not been granted");
            // Notify the user with a new permission request
            multiplePermissionLauncher.launch(PERMISSIONS);

        }
    }

    private boolean saveImageToExternalStorage(String imgName, Bitmap bmp){

        Uri ImageCollection = null;
        ContentResolver resolver = getContentResolver();

        // Determine correct path to be used according to the current Android SDK build version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            ImageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

        }else {

            ImageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        // Getting ready a new file to save the bitmap image in the smartphone gallery
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri imageUri = resolver.insert(ImageCollection, contentValues);

        try {
            // Put bitmap image into the file without losing quality
            OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            //Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(imgPath));
            //BitmapDrawable drawable = (BitmapDrawable) binding.imageView.getDrawable();
            //Bitmap bmp = drawable.getBitmap();
            bmp.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
            Objects.requireNonNull(outputStream);
            outputStream.close();
            Toast.makeText(this,"Image saved", Toast.LENGTH_SHORT).show();
            return true;

        }
        catch (Exception e){

            Toast.makeText(this,"Image not saved: \n" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        return false;


    }
}