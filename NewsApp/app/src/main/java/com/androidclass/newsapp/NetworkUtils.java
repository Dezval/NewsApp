package com.androidclass.newsapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    public static String base_url;
    public static String query_parameter;

    NetworkUtils(String baseURL, String queryParameter){
        base_url = baseURL;
        query_parameter = queryParameter;
    }

    private static final String baseUrl = "https://newsapi.org/v1/articles";
    private static final String sourceParam = "source";
    private static final String source = "the-next-web";
    private static final String sortParam = "sortBy";
    private static final String sort = "latest";
    private static final String apiKeyParam = "apiKey";
    private static final String apiKey = "7fea95368f82446c9c3dfd8ccb399572";

    public static URL buildUrl(String locationQuery) {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(sourceParam, source)
                .appendQueryParameter(sortParam, sort)
                .appendQueryParameter(apiKeyParam, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }

}
