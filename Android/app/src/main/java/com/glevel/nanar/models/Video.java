package com.glevel.nanar.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guillaume on 6/2/14.
 */
public class Video implements RestResource, Parcelable {

    public static final String TABLE_NAME = "videos";
    public static final String _ID = "_id";
    public static String COL_VIDEO_ID = "video_id";
    public static String COL_TITLE = "title";
    public static String COL_CREATED_DATE = "date_created";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_VIDEO_ID + " TEXT, "
            + COL_TITLE + " TEXT, " + COL_CREATED_DATE + " DATE);";

    public static final int CURSOR_VIDEO_ID = 1;
    public static final int CURSOR_TITLE = 2;

    private long id;
    private String videoId;
    private String title;

    public static Creator<Video> CREATOR = new Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in.readString(), in.readString());
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public Video() {
    }

    public Video(String videoId, String title) {
        this.videoId = videoId;
        this.title = title;
    }

    @Override
    public Cursor responseToCursor(String response) {
        MatrixCursor cursor = null;

        try {
            JSONArray jsonArray = new JSONArray(response);
            cursor = new MatrixCursor(new String[]{"_id", "video_id", "title"});
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonVideo = jsonArray.getJSONObject(i);
                cursor.addRow(new Object[]{i, jsonVideo.getString("url"), jsonVideo.getString("name")});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(videoId);
        out.writeString(title);
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public static ContentValues toContentValues(Video entity) {
        ContentValues args = new ContentValues();
        args.put(_ID, (entity.getId() == 0 ? null : entity.getId()));
        args.put(COL_VIDEO_ID, entity.getVideoId());
        args.put(COL_TITLE, entity.getTitle());
        return args;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
