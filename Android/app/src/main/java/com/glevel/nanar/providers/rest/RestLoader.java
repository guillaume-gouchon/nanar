package com.glevel.nanar.providers.rest;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.glevel.nanar.models.RestResource;

import org.apache.http.NameValuePair;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by guillaume on 6/2/14.
 */
public class RestLoader<T extends RestResource> extends AsyncTaskLoader<Cursor> {

    private static final String TAG = "RestLoader";

    private final RestHelper.HttpMethod method;
    private final String uri;
    private final ArrayList<NameValuePair> headers;
    private final ArrayList<NameValuePair> params;
    private final Class<T> resourceClass;

    public RestLoader(Class<T> resourceClass, Context context, RestHelper.HttpMethod method, String uri, ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params) {
        super(context);
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.params = params;
        this.resourceClass = resourceClass;
    }

    @Override
    public Cursor loadInBackground() {
        try {
            RestClient.RestResponse response = new RestClient(method, uri, headers, params, null).doRequest();
            if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try {
                    return resourceClass.newInstance().responseToCursor(response.getResponseBody());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
