package com.androidclass.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidclass.newsapp.model.DBHelper;
import com.androidclass.newsapp.model.DatabaseUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Void>,NewsAdapter.ItemClickListener {
    static final String TAG = "mainactivity";
    private static final int NEWS_LOADER = 0;
    private RecyclerView rv;
    private DBHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;
    private ProgressBar progressBar;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.recyclerviewNews);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        dbHelper = new DBHelper(this);
        // MAkes a Database to write on
        db = dbHelper.getWritableDatabase();
        //Handles first time opening the app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("isfirst", true);

        if (isFirst) {
            loadCurrent();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }

        ScheduleUtilities.scheduleRefresh(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        adapter = new NewsAdapter(cursor, this);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if (itemNumber == R.id.refresh) {
            progressBar.setVisibility(View.VISIBLE);
            loadCurrent();
            progressBar.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                //Deletes all items in the database and inserts them all again
                RefreshTasks.refreshArticles(MainActivity.this);
                return null;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progressBar.setVisibility(View.INVISIBLE);
        db = new DBHelper(MainActivity.this).getReadableDatabase(); // gets a readable databse
        cursor = DatabaseUtils.getAll(db);// gets all the items in the database
        adapter = new NewsAdapter(cursor, this);// we make a new adapter with the above grabbed items
        rv.setAdapter(adapter);// we set the adapter which has the above items to the recyclerview
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    //opens the news article web page of the clicked/touched item by passing a url to the called Intent
    @Override
    public void onItemClick(int clickedItemIndex, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    // Loads the current/default news source
    public void loadCurrent() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();

    }
}
