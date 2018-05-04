package com.awesomeapp.android.coolnewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsModel>> {

    private String myUrl;

    NewsLoader(Context context, String myUrl) {
        super(context);
        this.myUrl = myUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsModel> loadInBackground() {

        if (myUrl == null) {
            return null;
        }
        return NewsQuery.getNewsData(myUrl);
    }
}