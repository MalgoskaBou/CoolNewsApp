package com.awesomeapp.android.coolnewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class NewsQuery {

    private static final String LOG_DATA = NewsQuery.class.getSimpleName();

    public NewsQuery() {
    }

    public static List<NewsModel> getNewsData(String requestUrl) {

        //from string to url
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            //from url to json data
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_DATA, "Cant make HTTP request", e);
        }

        // Return the list extracted from json data
        return extractFeatureFromJson(jsonResponse);
    }

    //get string return URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_DATA, "Cant create URL ", e);
        }
        return url;
    }

   //get URL return json data in string
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        if (url == null) {
            return jsonResponse;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful -> response code 200 - get stream and read data
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_DATA, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_DATA, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {

                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //get json data in string and return List with items
    private static List<NewsModel> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<NewsModel> newsList = new ArrayList<>();

        try {

            JSONObject baseJsonRoot = new JSONObject(newsJSON);

            //get json object
            JSONObject newsObjectResponse = baseJsonRoot.getJSONObject("response");
            JSONArray newsArrayResults = newsObjectResponse.getJSONArray("results");

            //loop by array of results
            for (int i = 0; i < newsArrayResults.length(); i++) {

                JSONObject currentNewsData = newsArrayResults.getJSONObject(i);

                // get single data
                String newsTitle = currentNewsData.getString("webTitle");
                String newsDate = currentNewsData.getString("webPublicationDate");
                String newsSection = currentNewsData.getString("sectionName");

                String urnewsAuthor = "unknown";

                //get title if exist
                JSONArray newsArrayTags = currentNewsData.getJSONArray("tags");
                if(newsArrayTags != null && newsArrayTags.length() > 0) {
                    JSONObject currentNewsDataTags = newsArrayTags.getJSONObject(0);
                    urnewsAuthor = currentNewsDataTags.getString("webTitle");
                }

                String newsUrl = currentNewsData.getString("webUrl");
               // add data to list
                newsList.add(new NewsModel(newsTitle, newsSection, urnewsAuthor, newsDate, newsUrl));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return newsList;
    }
}