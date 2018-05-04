package com.awesomeapp.android.coolnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements LoaderCallbacks<List<NewsModel>> {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<NewsModel> newsList;
    private TextView mEmptyStateTextView;
    private View loadingBar;

    private static final String REQUEST_URL = "http://content.guardianapis.com/search?show-tags=contributor&api-key=8d158f50-c87b-451a-8cd6-eb6fad30df86";
    private static final int LOADER_NEWS_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView =findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.recyclerView);
        loadingBar= findViewById(R.id.loading_bar);

        newsList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        newsAdapter = new NewsAdapter( this, newsList);
        recyclerView.setAdapter(newsAdapter);

        // ConnectivityManager - check connection to internet (get info about connection of not null)
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public Loader<List<NewsModel>> onCreateLoader(int i, Bundle bundle) {

        return new NewsLoader(this, REQUEST_URL);
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
}
