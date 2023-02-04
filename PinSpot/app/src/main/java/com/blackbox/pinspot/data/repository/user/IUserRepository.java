package com.blackbox.pinspot.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.User;

import java.util.Set;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> logout();
    MutableLiveData<Result> forgotPwd(String email);
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);
}
