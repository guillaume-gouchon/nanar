package com.glevel.nanar.utils;

import android.util.Log;

/**
 * Created by guillaume on 6/2/14.
 */
public class YoutubeHelper {

    private static final String TAG = "YoutubeHelper";

    public static String getVideoThumbnail(String videoId) {
        Log.d(TAG, "Getting thumbnail for video : " + videoId);
        return "http://img.youtube.com/vi/" + videoId + "/0.jpg";
    }
    
}
