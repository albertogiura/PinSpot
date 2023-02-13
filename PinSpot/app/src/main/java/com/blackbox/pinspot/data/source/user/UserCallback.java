package com.blackbox.pinspot.data.source.user;

import com.blackbox.pinspot.model.User;


public interface UserCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessLogout();
    void onSuccessForgotPassword();
}
