package com.blackbox.pinspotdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blackbox.pinspotdemo.databinding.ActivitySignupBinding;
import com.blackbox.pinspotdemo.model.User;
import com.blackbox.pinspotdemo.util.LoadingDialog;
import com.blackbox.pinspotdemo.util.Permissions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";
    private LoadingDialog loadingDialog;
    private Permissions storagePermission;
    private String email, username, password;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        storagePermission = new Permissions();
        loadingDialog = new LoadingDialog(this);
        //storageReference = FirebaseStorage.getInstance().getReference();

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldsValid()) {
                    signUp();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {

    }

    private boolean areFieldsValid() {
        email = binding.edtEmailSignup.getText().toString().trim();
        username = binding.edtFullNameSignup.getText().toString().trim();
        password = binding.edtPasswordSignup.getText().toString().trim();

        boolean isNotReady = false;
        View requestView = null;

        if (username.isEmpty()) {
            binding.edtFullNameSignup.setError("Entering your full name is mandatory");
            requestView = binding.edtFullNameSignup;
            isNotReady = true;
        } else if (email.isEmpty()) {
            binding.edtEmailSignup.setError("Entering your email is mandatory");
            requestView = binding.edtEmailSignup;
            isNotReady = true;
        } else if (password.isEmpty()) {
            binding.edtPasswordSignup.setError("Entering a password for your account is mandatory");
            requestView = binding.edtPasswordSignup;
            isNotReady = true;
        } else if (password.length() < 8) {
            binding.edtPasswordSignup.setError("Your password has to be at least 8 characters long");
            requestView = binding.edtPasswordSignup;
            isNotReady = true;
        }

        if (isNotReady) {
            requestView.requestFocus();
            return false;
        }
        return true;
    }


    private void signUp() {
        loadingDialog.startLoading();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> signUp) {

                if (signUp.isSuccessful()) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    firebaseAuth.getCurrentUser().updateProfile(profileChangeRequest).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        User user = new User(email,
                                                username, true);

                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        loadingDialog.stopLoading();
                                                        Toast.makeText(SignupActivity.this, "Verify your account through email", Toast.LENGTH_SHORT).show();
                                                        onBackPressed();
                                                    }
                                                });
                                    } else {
                                        loadingDialog.stopLoading();
                                        Log.d("TAG", "onComplete: Update Profile" + task.getException());
                                        Toast.makeText(SignupActivity.this, "Update Profile" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    loadingDialog.stopLoading();
                    Log.d("TAG", "onComplete: Create user" + signUp.getException());
                    Toast.makeText(SignupActivity.this, "" + signUp.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Mio tentativo
    /*private void signUp() {
        loadingDialog.startLoading();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //StorageReference dbReference = FirebaseStorage.getInstance().getReference("Users");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(email, username, true);
                            loadingDialog.stopLoading();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignupActivity.this, "Signup completed",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            loadingDialog.stopLoading();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }*/
}