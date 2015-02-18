package com.glevel.nanar.movies.providers.rest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RestClient extends AsyncTask<Void, Void, RestClient.RestResponse> {

    private static final String TAG = "RestClient";

    public static final String SERVER_BASE_URL = "http://warnode.com:7000";

    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    private HttpMethod method;
    private URI uri;
    private ArrayList<NameValuePair> headers;
    private ArrayList<NameValuePair> params;
    private ProgressDialog progressDialog;

    public RestClient(HttpMethod method, String url, ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params, ProgressDialog progressDialog) throws URISyntaxException {
        super();
        this.method = method;
        this.uri = new URI(url);
        this.headers = headers;
        this.params = params;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        if (progressDialog != null) {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(RestResponse json) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected RestResponse doInBackground(Void... voids) {
        return doRequest();
    }

    /**
     * Also used in RestLoader
     *
     * @return HTTP response
     */
    public RestResponse doRequest() {
        Log.d(TAG, "Preparing request " + method + " " + uri.toString());
        switch (method) {
            case GET: {
                // add parameters
                String combinedParams = "";
                if (params != null) {
                    combinedParams += "?";
                    for (NameValuePair p : params) {
                        String paramString = null;
                        try {
                            paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (combinedParams.length() > 1) {
                            combinedParams += "&" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }
                    try {
                        uri = new URI(uri.toString() + combinedParams);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                HttpGet request = new HttpGet();
                request.setURI(uri);
                addHeaders(request);
                return executeRequest(request);
            }
            case POST: {
                HttpPost request = new HttpPost();
                request.setURI(uri);
                addHeaders(request);
                if (params != null) {
                    try {
                        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return executeRequest(request);
            }
            case PUT: {
                HttpPut request = new HttpPut();
                request.setURI(uri);
                addHeaders(request);
                if (params != null) {
                    try {
                        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return executeRequest(request);
            }
            case DELETE: {
                HttpDelete request = new HttpDelete();
                request.setURI(uri);
                addHeaders(request);
                return executeRequest(request);
            }
        }

        return null;
    }


    private void addHeaders(HttpRequest request) {
        if (headers != null) {
            headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
            for (NameValuePair h : headers) {
                request.addHeader(h.getName(), h.getValue());
            }
        }
    }

    private RestResponse executeRequest(HttpUriRequest request) {
        Log.d(TAG, "Executing request...");
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(request);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            String message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();

            Log.d(TAG, "Request response code is " + responseCode);
            return new RestResponse(responseCode, convertStreamToString(entity.getContent()));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return new RestResponse(500, null);
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
        } catch (IOException e) {
        }
        return sb.toString();
    }

    public static class RestResponse {

        private final int responseCode;
        private final String responseBody;

        private RestResponse(int responseCode, String responseBody) {
            this.responseCode = responseCode;
            this.responseBody = responseBody;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public int getResponseCode() {
            return responseCode;
        }

    }

}