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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.lang.Object;

import java.io.OutputStream;
import java.util.Objects;


public class InsertPlaceActivity extends AppCompatActivity {

    Button button2;
    Button button3;
    Button downloadButton;
    private ImageView image;
    private static final int REQUEST_CODE=1;
    private static final int Read_Permission=101;
    private static final int SELECT_IMAGE=100;


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
        button2.setOnClickListener(view -> {
            Intent openHome = new Intent(InsertPlaceActivity.this, MainActivity.class);
            startActivity(openHome);
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