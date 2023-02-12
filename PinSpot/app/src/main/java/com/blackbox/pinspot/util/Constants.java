package com.blackbox.pinspot.util;

public class Constants {

    public static final int STORAGE_REQUEST_CODE = 1000;
    public   static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";

    // OpenWeatherMap.org API related constants
    public static final String OPENWEATHERMAP_API_BASE_URL = "https://api.openweathermap.org/data/2.5/";

    //places api key
    public static final String PLACES_API_KEK = "AIzaSyBVzu-lEm7gs-V1AElIWVgwHlNXdaeuVyM";

    // Costants fot onSaveIstanceState
    public static final String LAST_LAT = "LAST_LAT";
    public static final String LAST_LON = "LAST_LON";


    // Constants for managing errors
    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    // Constants for SharedPreferences
    public static final String SHARED_PREFERENCES_FILE_NAME = "settings";
    public static final String SHARED_PREFERENCES_SKIP = "skip";
    public static final String SHARED_PREFERENCES_CELSIUS = "celsius";

    public static final int MINIMUM_PASSWORD_LENGTH = 6;

    // Constants for Firebase Realtime Database
    public static final String FIREBASE_REALTIME_DATABASE = "https://pinspot-demo-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_FAVORITE_NEWS_COLLECTION = "favorite_news";
}
