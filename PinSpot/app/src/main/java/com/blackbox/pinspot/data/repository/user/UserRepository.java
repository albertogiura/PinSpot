package com.blackbox.pinspot.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.blackbox.pinspot.data.source.user.BaseUserAuthRemoteDataSource;
import com.blackbox.pinspot.data.source.user.UserCallback;
import com.blackbox.pinspot.model.Result;
import com.blackbox.pinspot.model.User;


public class UserRepository implements IUserRepository, UserCallback {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthRemoteDataSource userRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> forgotPasswordMutableLiveData;


    public UserRepository(BaseUserAuthRemoteDataSource userRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.forgotPasswordMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
    }


    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> forgotPwd(String email) {
        userRemoteDataSource.forgotPassword(email);
        return forgotPasswordMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }


    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
            userMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessForgotPassword() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        forgotPasswordMutableLiveData.postValue(result);
    }

}
