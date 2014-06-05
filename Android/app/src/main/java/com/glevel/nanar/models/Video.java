package com.glevel.nanar.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.glevel.nanar.providers.rest.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by guillaume on 6/2/14.
 */
public class Video extends RestResource implements Parcelable {

    public static String RESOURCE_URL = RestClient.SERVER_BASE_URL + "/videos";

    public static final String TABLE_NAME = "videos";
    public static final String _ID = "_id";
    public static String COL_REMOTE_ID = "remote_id";
    public static String COL_VIDEO_ID = "video_id";
    public static String COL_TITLE = "title";
    public static String COL_CREATED_DATE = "date_created";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_REMOTE_ID + " TEXT, "
            + COL_VIDEO_ID + " TEXT, " + COL_TITLE + " TEXT, " + COL_CREATED_DATE + " DATE);";

    private long id;
    private String remoteId;
    private String videoId;
    private String title;

    public static Creator<Video> CREATOR = new Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in.readLong(), in.readString(), in.readString(), in.readString());
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public Video(long id, String remoteId, String videoId, String title) {
        this.id = id;
        this.remoteId = remoteId;
        this.videoId = videoId;
        this.title = title;
    }

    public static HashMap<String, Video> responseToVideoMap(String response) {
        HashMap<String, Video> map = new HashMap<String, Video>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonVideo = jsonArray.getJSONObject(i);
                map.put(jsonVideo.getString("_id"), new Video(0, jsonVideo.getString("_id"), jsonVideo.getString("url"), jsonVideo.getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static Video fromCursor(Cursor cursor) {
        return new Video(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(remoteId);
        out.writeString(videoId);
        out.writeString(title);
    }

    public ContentValues toContentValues() {
        ContentValues args = new ContentValues();
        args.put(_ID, (id == 0 ? null : id));
        args.put(COL_REMOTE_ID, remoteId);
        args.put(COL_VIDEO_ID, videoId);
        args.put(COL_TITLE, title);
        return args;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public long getId() {
        return id;
    }

}
