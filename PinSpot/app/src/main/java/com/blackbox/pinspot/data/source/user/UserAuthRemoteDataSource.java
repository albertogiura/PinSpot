package com.blackbox.pinspot.data.source.user;

import static com.blackbox.pinspot.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.blackbox.pinspot.util.Constants.INVALID_USER_ERROR;
import static com.blackbox.pinspot.util.Constants.UNEXPECTED_ERROR;
import static com.blackbox.pinspot.util.Constants.USER_COLLISION_ERROR;
import static com.blackbox.pinspot.util.Constants.WEAK_PASSWORD_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blackbox.pinspot.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

// Class that contains concrete methods implementation that interacts with Firebase Auth database
public class UserAuthRemoteDataSource extends BaseUserAuthRemoteDataSource {

    private static final String TAG = UserAuthRemoteDataSource.class.getSimpleName();

    private final FirebaseAuth mAuth;

    public UserAuthRemoteDataSource() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public User getLoggedUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            return null;
        } else {
            return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid());
        }
    }

    @Override
    public void logout() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    Log.d(TAG, "User logged out");
                    userCallback.onSuccessLogout();
                }
            }
        };
        mAuth.addAuthStateListener(authStateListener);
        mAuth.signOut();
    }

    @Override
    public void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // Code to be executed if user has been correctly created on the Firebase Auth side
                    userCallback.onSuccessFromAuthentication(new User(
                            firebaseUser.getDisplayName(), email, firebaseUser.getUid()));
                } else {
                    userCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                userCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }

    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // Code to be executed if user has been correctly created on the Firebase Auth side
                    userCallback.onSuccessFromAuthentication(new User(
                            firebaseUser.getDisplayName(), email, firebaseUser.getUid()));
                } else {
                    userCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                userCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }

    @Override
    public void signInWithGoogle(String idToken) {
        if (idToken !=  null) {
            // Got an ID token from Google. Use it to authenticate with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        // Code to be executed if user has been correctly created on the Firebase Auth side
                        userCallback.onSuccessFromAuthentication(new User(
                                firebaseUser.getDisplayName(), firebaseUser.getEmail(),
                                firebaseUser.getUid()));
                    } else {
                        userCallback.onFailureFromAuthentication(
                                getErrorMessage(task.getException()));
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    userCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            });
        }
    }

    @Override
    public void forgotPassword(String email) {
        if (email != null) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                userCallback.onSuccessForgotPassword();
                            }
                        }
                    });
        }
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return WEAK_PASSWORD_ERROR;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return INVALID_CREDENTIALS_ERROR;
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return INVALID_USER_ERROR;
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return USER_COLLISION_ERROR;
        }
        return UNEXPECTED_ERROR;
    }
}
