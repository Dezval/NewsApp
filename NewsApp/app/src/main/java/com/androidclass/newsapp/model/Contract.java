package com.androidclass.newsapp.model;

import android.provider.BaseColumns;

public class Contract {
    public static class TABLE_NEWS implements BaseColumns{
        public static final String TABLE_NAME = "newsItem";

        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_URL_TO_IMAGE= "urlToImage";
        public static final String COLUMN_NAME_PUBLISHED_AT= "publishedAt";



    }
}
