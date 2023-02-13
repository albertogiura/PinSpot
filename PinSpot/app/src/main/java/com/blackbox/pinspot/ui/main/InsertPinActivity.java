package com.blackbox.pinspot.ui.main;

import static com.blackbox.pinspot.util.Constants.CAMERA_REQUEST_CODE;
import static com.blackbox.pinspot.util.Constants.DBIMAGES;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.data.repository.pin.IPinRepository;
import com.blackbox.pinspot.databinding.ActivityInsertPinBinding;
import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class InsertPinActivity extends AppCompatActivity {

    private static final String TAG = InsertPinActivity.class.getSimpleName();

    private ActivityInsertPinBinding binding;
    private String currentPhotoPath = "";
    private String randomPhotoFileName = UUID.randomUUID().toString();

    private PinViewModel pinViewModel;

    private ActivityResultLauncher<String[]> multiplePermissionLauncher;
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private int opCode = 0;

    private ActivityResultLauncher<Uri> mTakePicture;
    Uri tempImageUri;

    // Firebase Storage
    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES);
    private StorageReference storageRef = storage.getReference();

    // Image picker
    private ActivityResultLauncher<String> openLocalPhoto;

    private final String[] CAMERA_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final String[] GALLERY_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInsertPinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        IPinRepository pinRepository =
                ServiceLocator.getInstance().getPinRepository(this.getApplication());

        if (pinRepository != null) {
            pinViewModel = new ViewModelProvider(
                    this,
                    new PinViewModelFactory(pinRepository)).get(PinViewModel.class);
        } else {
            Snackbar.make(this.findViewById(android.R.id.content),
                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
        }

        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();

        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            for(Map.Entry<String, Boolean> set : isGranted.entrySet()) {
                Log.d(TAG, set.getKey() + " " + set.getValue());
            }
            if (!isGranted.containsValue(false)) {
                Log.d(TAG, "All permission have been granted");

                if (opCode == 1) {
                    takePicture();
                } else if (opCode == 0){
                    openLocalPhoto.launch("image/*");
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && opCode == 1) {
                    requestPermissions(CAMERA_PERMISSIONS, CAMERA_REQUEST_CODE);
                }
            }

        });

        tempImageUri = initTempUri();

        mTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                result -> {

                    if (result) {
                        binding.imageView.setImageURI(null); // This seems necessary for the image to change if the photo is taken again
                        binding.imageView.setImageURI(tempImageUri); // Retrieve the image contained in the specified URI as new picture for the imageView

                        // Retrieve taken photo from imageView, put it in a Bitmap and save picture locally
                        BitmapDrawable drawable = (BitmapDrawable) binding.imageView.getDrawable();
                        Bitmap bmp = drawable.getBitmap();
                        saveImageToExternalStorage(randomPhotoFileName, bmp);

                        // Enable upload button
                        binding.uploadPinButton.setEnabled(true);
                    }
                    else
                    {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Operation cancelled", Snackbar.LENGTH_SHORT).show();
                        binding.imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder_pin_photo));
                    }


                });

        openLocalPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    binding.imageView.setImageURI(result);
                    if(result != null){
                        binding.uploadPinButton.setEnabled(true);
                    }else{
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Operation cancelled", Snackbar.LENGTH_SHORT).show();
                        binding.imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder_pin_photo));
                    }
                }
        );

        binding.takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();
            }
        });


        binding.uploadPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.newPinTitleEdt.length() > 0){
                    binding.uploadProgressBar.setVisibility(View.VISIBLE);
                    uploadPicture();
                } else {
                    //TODO
                    Snackbar.make(view,
                            "Choose a title for the new pin", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        binding.choosePicFromLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean multiplePermissionsStatus =
                        ActivityCompat.checkSelfPermission(InsertPinActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(InsertPinActivity.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                if (multiplePermissionsStatus) {
                    Log.d(TAG, "All permissions needed to take a picture have been granted");
                    // If all permissions have been granted, execute the following logic
                    openLocalPhoto.launch("image/*");


                } else {
                    Log.d(TAG, "One or more permissions have not been granted");
                    // Notify the user with a new permission request
                    opCode = 0;
                    multiplePermissionLauncher.launch(GALLERY_PERMISSIONS);

                }

                if(ContextCompat.checkSelfPermission(InsertPinActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    openLocalPhoto.launch("image/*");

                }else{
                    //TODO
                    Snackbar.make(view,
                            "Read permission not granted", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        binding.closePinInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPinActivity.this.finish();
            }
        });

    }

    private void uploadPicture(){
        Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        pinViewModel.uploadImagePin(data, randomPhotoFileName).observe(this, result -> {
            if (result.isSuccess()){
                uploadPin();
                //TODO
                Snackbar.make(this.findViewById(android.R.id.content),
                        "Upload completed", Snackbar.LENGTH_SHORT).show();
            } else {
                binding.uploadProgressBar.setVisibility(View.GONE);
                Snackbar.make(this.findViewById(android.R.id.content),
                        "Photo upload failed", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadPin() {
        Bundle mypos = getIntent().getExtras();

        if (mypos != null) {
            Double lat = mypos.getDouble("latitude");
            Double lng = mypos.getDouble("longitude");

            Pin pin = new Pin(lat, lng, binding.newPinTitleEdt.getText().toString(), randomPhotoFileName);

            pinViewModel.uploadPin(pin).observe(this, result -> {
                if (result.isSuccess()){
                    binding.uploadProgressBar.setVisibility(View.GONE);
                    Snackbar.make(this.findViewById(android.R.id.content),
                            "Upload completed", Snackbar.LENGTH_SHORT).show();
                    InsertPinActivity.this.finish();
                } else {
                    binding.uploadProgressBar.setVisibility(View.GONE);
                    Snackbar.make(this.findViewById(android.R.id.content),
                            "Pin upload failed", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    private Uri initTempUri(){
        // Setup a temporary file to save taken picture
        File tempImagesDir = new File(getApplicationContext().getFilesDir(), "temp_images");
        if(!tempImagesDir.exists()){
            tempImagesDir.mkdir();
        }
        File tempImage = new File(tempImagesDir, "temp_image.jpg");
        return FileProvider.getUriForFile(getApplicationContext(), "com.blackbox.pinspot.fileprovider", tempImage);
    }

    private void takePicture() {
        // Using a boolean variable to determine if permissions have been granted
        boolean multiplePermissionsStatus =
                ActivityCompat.checkSelfPermission(InsertPinActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(InsertPinActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (multiplePermissionsStatus) {
            Log.d(TAG, "All permissions needed to take a picture have been granted");
            // If all permissions have been granted, execute the following logic

            // Call smartphone camera with the file path that has to be used to save the picture
            mTakePicture.launch(tempImageUri);

        } else {
            Log.d(TAG, "One or more permissions have not been granted");
            // Notify the user with a new permission request
            opCode = 1;
            multiplePermissionLauncher.launch(CAMERA_PERMISSIONS);

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
            bmp.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
            Objects.requireNonNull(outputStream);
            outputStream.close();
            Snackbar.make(this.findViewById(android.R.id.content),
                    "Image saved", Snackbar.LENGTH_SHORT).show();
            return true;

        }
        catch (Exception e){
            Snackbar.make(this.findViewById(android.R.id.content),
                    "Image not saved: \n" + e, Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        return false;
    }
}