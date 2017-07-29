package com.androidclass.newsapp;

/**
 * Created by Dezval on 7/28/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androidclass.newsapp.model.DBHelper;
import com.androidclass.newsapp.model.DatabaseUtils;
import com.androidclass.newsapp.model.Item;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RefreshTasks {

    public static final String TAG = "refresh";


    public static void refreshArticles(Context context) {
        ArrayList<Item> result = null;
        URL url = NetworkUtils.buildUrl();

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();

        try {
            DatabaseUtils.deleteAll(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);
            DatabaseUtils.bulkInsert(db, result);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }
}
