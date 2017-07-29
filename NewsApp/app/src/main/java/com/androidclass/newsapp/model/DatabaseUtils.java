package com.androidclass.newsapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.androidclass.newsapp.model.Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR;
import static com.androidclass.newsapp.model.Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION;
import static com.androidclass.newsapp.model.Contract.TABLE_NEWS.COLUMN_NAME_PUBLISHED_AT;
import static com.androidclass.newsapp.model.Contract.TABLE_NEWS.COLUMN_NAME_TITLE;
import static com.androidclass.newsapp.model.Contract.TABLE_NEWS.COLUMN_NAME_URL;
import static com.androidclass.newsapp.model.Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE;
import static com.androidclass.newsapp.model.Contract.TABLE_NEWS.TABLE_NAME;


public class DatabaseUtils {

    //gets all items from databse ordered by date in descending order
    public static Cursor getAll(SQLiteDatabase db) {
        return db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_PUBLISHED_AT + " DESC"
        );
    }

    //fills the database with the items in the ArrayList passed in
    public static void bulkInsert(SQLiteDatabase db, ArrayList<Item> articles) {

        db.beginTransaction();
        try {
            for (Item a : articles) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_AUTHOR, a.getAuthor());
                cv.put(COLUMN_NAME_TITLE, a.getTitle());
                cv.put(COLUMN_NAME_DESCRIPTION, a.getDescription());
                cv.put(COLUMN_NAME_URL, a.getUrl());
                cv.put(COLUMN_NAME_URL_TO_IMAGE, a.getUrlToImage());
                cv.put(COLUMN_NAME_PUBLISHED_AT, a.getPublishedAt());
                db.insertWithOnConflict(TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //Deletes the table in the sql database
    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }


}
