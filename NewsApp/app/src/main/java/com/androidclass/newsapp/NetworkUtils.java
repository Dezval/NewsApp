package com.androidclass.newsapp;

import android.net.Uri;
import android.util.Log;

import com.androidclass.newsapp.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    //URL Looks like the below
    //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=7fea95368f82446c9c3dfd8ccb399572
    private static final String baseUrl = "https://newsapi.org/v1/articles";
    private static final String sourceParam = "source";
    private static final String source = "the-next-web";
    private static final String sortParam = "sortBy";
    private static final String sort = "latest";
    private static final String apiKeyParam = "apiKey";
    private static final String apiKey = "7fea95368f82446c9c3dfd8ccb399572";

    public static URL buildUrl() {
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

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner input = new Scanner(in);
            input.useDelimiter("\\A");

            String result = (input.hasNext()) ? input.next() : null;
            return result;

        }catch (IOException e){
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static ArrayList<Item> parseJSON(String json) throws JSONException {
        ArrayList<Item> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("articles");

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String author = item.getString("author");
            String title = item.getString("title");
            String description = item.getString("description");
            String url = item.getString("url");
            String urlToImage = item.getString("urlToImage");
            String publishedAt= item.getString("publishedAt");

            result.add(new Item(author,  title,  description,  url,  urlToImage,  publishedAt));
        }
        Log.d(TAG, "final items size: " + result.size());
        return result;
    }
}
