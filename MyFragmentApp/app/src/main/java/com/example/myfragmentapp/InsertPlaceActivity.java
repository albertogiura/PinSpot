package com.example.myfragmentapp;

import androidx.annotation.NonNull;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.Objects;


public class InsertPlaceActivity extends AppCompatActivity {


    private Button button2;
    private Button downloadButton;
    private ImageView image;
    private static final int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertplace);
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        image = (ImageView) findViewById(R.id.myImage);
        image.setImageBitmap(bmp);
        button2=findViewById(R.id.buttonHome);
        downloadButton=findViewById(R.id.buttonDownload);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openHome = new Intent(InsertPlaceActivity.this, MainActivity.class);

                startActivity(openHome);
            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(InsertPlaceActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    saveImage();
                }
                else{
                    ActivityCompat.requestPermissions(InsertPlaceActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if( requestCode== REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                saveImage();
            }else{
                Toast.makeText(this, "Please provide required permission", Toast.LENGTH_SHORT).show();
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
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis()+"-jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");
        Uri uri= contentResolver.insert(images, contentValues);

        try {
            BitmapDrawable bitmapDrawable= (BitmapDrawable) image.getDrawable();
            Bitmap bitmap=bitmapDrawable.getBitmap();

            OutputStream outputStream= contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,1000,outputStream);
            Objects.requireNonNull(outputStream);

            Toast.makeText(this, "Images Saved Succesfully", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(this, "Images not Saved ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
}