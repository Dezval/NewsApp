package com.androidclass.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mNewsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public class FetchNewsTask extends AsyncTask<Object, Object, String> {


        @Override
        protected void onPreExecute() {

            // TODO Add onPreExecute Method for loading bar...

        }

        @Override
        protected String doInBackground(Object... params) {

            if (params.length == 0) {
                return null;
            }

            URL newsRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonNewsResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);

                return jsonNewsResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String newsData) {
            // TODO Add onPostExecute Method
        }
    }
}
