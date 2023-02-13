package com.blackbox.pinspot.data.source.pin;

import static android.content.ContentValues.TAG;

import static com.blackbox.pinspot.util.Constants.DBIMAGES;
import static com.blackbox.pinspot.util.Constants.PIN_COLLECTION;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.ui.main.InsertPinActivity;
import com.blackbox.pinspot.ui.main.MapFragmentDirections;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PinRemoteDataSource extends BasePinRemoteDataSource {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES);
    private StorageReference storageRef = storage.getReference();

    public PinRemoteDataSource() {
    }

    @Override
    public void uploadPinImage(byte[] data, String photoName) {
        StorageReference takenPhotoRef = storageRef.child("pinPhotos/"+photoName+".jpeg");
        UploadTask uploadTask = takenPhotoRef.putBytes(data); // Uploading here
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                pinCallback.onFailureUploadingImage(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pinCallback.onSuccessUploadingImage();
            }
        });
    }

    @Override
    public void uploadPin(Pin pin) {
        db.collection(PIN_COLLECTION)
                .add(pin)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        pinCallback.onSuccessUploadingPin();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.w(TAG, "Error adding document", exception);
                        pinCallback.onFailureUploadingPin(exception.getMessage());
                    }
                });
    }
}
