package it.sal.disco.unimib.camera;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE=22;
    private ImageView myImage1;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImage1=findViewById(R.id.myImage);
        button1=findViewById(R.id.button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, REQUEST_CODE);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQUEST_CODE && resultCode==RESULT_OK)  {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            myImage1.setImageBitmap(photo);
        }
        else{
            Toast.makeText(this,"Cancelled",Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode,resultCode,data);
        }

    }
}