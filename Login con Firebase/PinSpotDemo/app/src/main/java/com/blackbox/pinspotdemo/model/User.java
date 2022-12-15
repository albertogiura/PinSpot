package com.blackbox.pinspotdemo.model;

public class User {
    private String email;
    private String username;
    private boolean isNotificationEnabled;

    public User() {
    }

    public User(String email, String username, boolean isNotificationEnabled) {
        this.email = email;
        this.username = username;
        this.isNotificationEnabled = isNotificationEnabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }
}
