package com.blackbox.pinspot.data.source.user;

import com.blackbox.pinspot.data.repository.user.UserResponseCallback;
import com.blackbox.pinspot.model.User;

import java.util.Set;


/**
 * Base class to get the user data from a remote source.
 */
public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
    public abstract void getUserFavoriteNews(String idToken);
    public abstract void getUserPreferences(String idToken);
    public abstract void saveUserPreferences(String favoriteCountry, Set<String> favoriteTopics, String idToken);
}
