package com.blackbox.pinspot.util;

public class Constants {

    public static final int STORAGE_REQUEST_CODE = 1000;

    // OpenWeatherMap.org API related constants
    public static final String OPENWEATHERMAP_API_BASE_URL = "https://api.openweathermap.org/data/2.5/";



    // Constants for managing errors
    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    // Constants for SharedPreferences
    public static final String SHARED_PREFERENCES_FILE_NAME = "com.blackbox.pinspot.preferences";
    public static final String SHARED_PREFERENCES_COUNTRY_OF_INTEREST = "country_of_interest";
    public static final String SHARED_PREFERENCES_TOPICS_OF_INTEREST = "topics_of_interest";
    public static final String SHARED_PREFERENCES_FIRST_LOADING = "first_loading";

    // Costants fot onSaveIstanceState
    public static final String LAST_LAT = "LAST_LAT";
    public static final String LAST_LON = "LAST_LON";

    // Constants for EncryptedSharedPreferences
    public static final String ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "com.blackbox.pinspot.encrypted_preferences";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String PASSWORD = "password";
    public static final String ID_TOKEN = "google_token";

    // Constants for encrypted files
    public static final String ENCRYPTED_DATA_FILE_NAME = "com.blackbox.pinspot.encrypted_file.txt";

    public static final int MINIMUM_PASSWORD_LENGTH = 6;

    // Constants for Firebase Realtime Database
    public static final String FIREBASE_REALTIME_DATABASE = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_FAVORITE_NEWS_COLLECTION = "favorite_news";
}
