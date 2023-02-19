package com.blackbox.pinspot.util;

public class Constants {

    // Firebase Storage
    public static final String DBIMAGES = "Insert Firebase Storage URL here";

    // Pin collection path on remote DB
    public static final String PIN_COLLECTION = "demo";

    // Request code for InsertPin related permissions request
    public static final int CAMERA_REQUEST_CODE = 1000;

    // OpenWeatherMap.org API related constants
    public static final String OPENWEATHERMAP_API_BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String WEATHER_API_KEY = "Insert API key here";

    // Places API key
    public static final String PLACES_API_KEY = "Insert API key here";

    // Constants for onSaveInstanceState
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

    // Login related constants
    public static final int MINIMUM_PASSWORD_LENGTH = 6;
}
