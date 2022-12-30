package com.blackbox.pinspotdemo.source.user;

import com.blackbox.pinspotdemo.model.User;
import com.blackbox.pinspotdemo.repository.user.UserResponseCallback;

public abstract class BaseUserAuthRemoteDataSource {

    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract User getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password);
    public abstract void signIn(String email, String password);
    public abstract void signInWithGoogle(String idToken);
}
