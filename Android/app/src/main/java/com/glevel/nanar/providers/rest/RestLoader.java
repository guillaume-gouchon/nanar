package com.glevel.nanar.providers.rest;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.glevel.nanar.models.RestResource;

import org.apache.http.NameValuePair;

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

    public RestLoader(Context context, RestHelper.HttpMethod method, String uri, ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params) {
        super(context);
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.params = params;
    }

    @Override
    public Cursor loadInBackground() {
        try {
            String response = new RestClient(method, uri, headers, params, null).doRequest();
            return T.responseToCursor(response);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
