package com.glevel.nanar.models;

import android.database.Cursor;
import android.database.MatrixCursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guillaume on 6/2/14.
 */
public class Video extends RestResource {

    public static final int CURSOR_ID = 0;
    public static final int CURSOR_TITLE = 1;

    private final String videoId;
    private final String title;

    public Video(String videoId, String title) {
        this.videoId = videoId;
        this.title = title;
    }

    public static Cursor responseToCursor(String response) {
        MatrixCursor cursor = null;

        try {
            JSONObject json = new JSONObject(response);
            if (json.has("data")) {
                JSONArray jsonArray = json.getJSONArray("data");
                cursor = new MatrixCursor(new String[]{"video_id", "title"});
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonVideo = jsonArray.getJSONObject(i);
                    cursor.addRow(new Object[]{jsonVideo.getString("video_id"), jsonVideo.getString("title")});
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cursor;
    }

}
