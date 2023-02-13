package com.blackbox.pinspot.data.source.user;

import com.blackbox.pinspot.model.User;

public abstract class BaseUserAuthRemoteDataSource {

    protected UserCallback userCallback;

    public void setUserResponseCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }
    public abstract User getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password);
    public abstract void signIn(String email, String password);
    public abstract void signInWithGoogle(String idToken);
    public abstract void forgotPassword(String email);
}
