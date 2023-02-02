package com.blackbox.pinspot.data.repository.user;

import com.blackbox.pinspot.model.User;

import java.util.List;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);
    // void onSuccessFromRemoteDatabase(List<User> userList);
    //void onSuccessFromGettingUserPreferences();
    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();
}
