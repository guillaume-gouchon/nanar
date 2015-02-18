package com.glevel.nanar.movies.utils;

import android.util.Log;

/**
 * Created by guillaume on 6/2/14.
 */
public class YoutubeHelper {

    private static final String TAG = "YoutubeHelper";

    public static final String YOUTUBE_DEV_KEY = "AIzaSyCI1PgjbEkWUfxEQYeE54SOIhiuZ5rd0U8";

    public static String getVideoThumbnail(String videoId) {
        Log.d(TAG, "Getting thumbnail for video : " + videoId);
        return "http://img.youtube.com/vi/" + videoId + "/0.jpg";
    }

    public static String getVideoLink(String videoId) {
        return "http://youtube.com/watch?v=" + videoId;
    }

}
