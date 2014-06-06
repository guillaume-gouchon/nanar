package com.glevel.nanar.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.glevel.nanar.providers.rest.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by guillaume on 6/2/14.
 */
public class Video implements Parcelable, SyncResource {

    public static final int RESOURCE_ID = 100;
    public static final String RESOURCE_URL = RestClient.SERVER_BASE_URL + "/videos";

    public static final String TABLE_NAME = "videos";
    public static final String _ID = "_id";
    public static String COL_REMOTE_ID = "remote_id";
    public static String COL_VIDEO_ID = "video_id";
    public static String COL_TITLE = "title";
    public static String COL_TAGS = "tags";
    public static String COL_CREATED_DATE = "date_created";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_REMOTE_ID + " TEXT, "
            + COL_VIDEO_ID + " TEXT, " + COL_TITLE + " TEXT, " + COL_TAGS + " TEXT, " + COL_CREATED_DATE + " DATE);";

    private final long id;
    private final String remoteId;
    private final String videoId;
    private final String title;
    private final String tags;

    public static Creator<Video> CREATOR = new Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in.readLong(), in.readString(), in.readString(), in.readString(), in.readString());
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public Video(long id, String remoteId, String videoId, String title, String tags) {
        this.id = id;
        this.remoteId = remoteId;
        this.videoId = videoId;
        this.title = title;
        this.tags = tags;
    }

    public static HashMap<String, SyncResource> responseToMap(String response) {
        HashMap<String, SyncResource> map = new HashMap<String, SyncResource>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonEntry = jsonArray.getJSONObject(i);
                map.put(jsonEntry.getString("_id"), new Video(0, jsonEntry.getString("_id"), jsonEntry.getString("url"), jsonEntry.getString("name"), jsonEntry.getString("tags")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static Video fromCursor(Cursor cursor) {
        return new Video(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
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
        out.writeString(tags);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues args = new ContentValues();
        args.put(_ID, (id == 0 ? null : id));
        args.put(COL_REMOTE_ID, remoteId);
        args.put(COL_VIDEO_ID, videoId);
        args.put(COL_TITLE, title);
        args.put(COL_TAGS, tags);
        args.put(COL_CREATED_DATE, new Date().getTime());
        return args;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getRemoteId() {
        return remoteId;
    }

    @Override
    public long getId() {
        return id;
    }

}
