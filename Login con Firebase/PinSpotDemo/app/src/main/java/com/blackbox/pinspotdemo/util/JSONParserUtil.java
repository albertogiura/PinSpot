package com.blackbox.pinspotdemo.util;

import android.app.Application;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class to show different ways to parse a JSON file.
 */
public class JSONParserUtil {

    private static final String TAG = JSONParserUtil.class.getSimpleName();

    public enum JsonParserType {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };

    private final Application application;

    private final String statusParameter = "status";
    private final String totalResultsParameter = "totalResults";
    private final String articlesParameter = "articles";
    private final String sourceParameter = "source";
    private final String authorParameter = "author";
    private final String titleParameter = "title";
    private final String descriptionParameter = "description";
    private final String urlParameter = "url";
    private final String urlToImageParameter = "urlToImage";
    private final String publishedAtParameter = "publishedAt";
    private final String contentParameter = "content";
    private final String nameParameter = "name";

    public JSONParserUtil(Application application) {
        this.application = application;
    }

    /*public PinApiResponse parseJSONFileWithGSon(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, PinApiResponse.class);
    }*/
}
