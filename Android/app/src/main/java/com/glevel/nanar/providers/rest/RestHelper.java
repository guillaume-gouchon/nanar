package com.glevel.nanar.providers.rest;

/**
 * Created by guillaume on 5/28/14.
 */
public class RestHelper {

    public static final String SERVER_BASE_URL = "http://warnode.com:8080";
    public static final String GET_VIDEOS_URL = SERVER_BASE_URL + "/videos";

    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

}
