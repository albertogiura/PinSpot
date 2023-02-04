package com.blackbox.pinspot.data.repository.user;

import com.blackbox.pinspot.model.User;

import java.util.List;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessLogout();
    void onSuccessForgotPassword();
}
