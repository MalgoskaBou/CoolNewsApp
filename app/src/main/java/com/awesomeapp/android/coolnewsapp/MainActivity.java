package com.awesomeapp.android.coolnewsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsModel>>, SharedPreferences.OnSharedPreferenceChangeListener {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<NewsModel> newsList;
    private TextView mEmptyStateTextView;
    private View loadingBar;

    private static final String REQUEST_URL_BASIC = "http://content.guardianapis.com/search?";
    private static final int LOADER_NEWS_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.recyclerView);
        loadingBar = findViewById(R.id.loading_bar);

        newsList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        newsAdapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(newsAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);


        // ConnectivityManager - check connection to internet (get info about connection of not null)
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr != null ? connMgr.getActiveNetworkInfo() : null;

        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager adn initialize with chosen ID
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_NEWS_ID, null, this);
        } else {

            //If have not connection with internet
            loadingBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.internetConnectionMessage);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_news_limit_key)) ||
                key.equals(getString(R.string.settings_order_by_topic_key))) {
            // Clear the list for new query
            newsAdapter.clearAllData();

            // Hide the empty state text view as the loading indicator will be displayed
            mEmptyStateTextView.setVisibility(View.GONE);

            // Show the loading bar while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_bar);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the news
            getLoaderManager().restartLoader(LOADER_NEWS_ID, null, this);
        }
    }

    @Override
    public Loader<List<NewsModel>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString from sharedPreferences
        String newsLimitNumber = sharedPrefs.getString(
                getString(R.string.settings_news_limit_key),
                getString(R.string.settings_news_limit_default));
        String newsTopic = sharedPrefs.getString(
                getString(R.string.settings_order_by_topic_key),
                getString(R.string.settings_order_by_topic_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL_BASIC);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        if (!newsTopic.equals("all"))
            uriBuilder.appendQueryParameter(getString(R.string.settings_order_by_topic_key), newsTopic);
        if (!newsLimitNumber.isEmpty())
            uriBuilder.appendQueryParameter(getString(R.string.settings_news_limit_key), newsLimitNumber);

        uriBuilder.appendQueryParameter(getString(R.string.show_tags_author_key), getString(R.string.show_tags_author_value));
        uriBuilder.appendQueryParameter(getString(R.string.api_key), getString(R.string.api_key_value));

        // Return the completed uri
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsModel>> loader, List<NewsModel> myNewsList) {

        loadingBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.nothingToShowMessage);
        newsAdapter.clearAllData();

        if (myNewsList != null && !myNewsList.isEmpty()) {

            mEmptyStateTextView.setVisibility(View.GONE);
            newsAdapter.addAllData(myNewsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsModel>> loader) {
        newsAdapter.clearAllData();
    }

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
