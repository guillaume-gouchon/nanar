package com.glevel.nanar.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.glevel.nanar.providers.rest.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by guillaume on 5/27/14.
 */
public class Tag implements SyncResource {

    public static final int RESOURCE_ID = 200;
    public static final String RESOURCE_URL = RestClient.SERVER_BASE_URL + "/tags";

    public static final String TABLE_NAME = "tags";
    public static final String _ID = "_id";
    public static String COL_REMOTE_ID = "remote_id";
    public static String COL_LABEL = "label";
    public static String TAGS_DEFAULT_SORT_ORDER = "label";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_REMOTE_ID + " TEXT, "
            + COL_LABEL + " INTEGER);";

    private final long mId;
    private final String mRemoteId;
    private final String mLabel;

    public Tag(long id, String remoteId, String label) {
        mId = id;
        mRemoteId = remoteId;
        mLabel = label;
    }

    @Override
    public long getId() {
        return mId;
    }

    public String getLabel() {
        return mLabel;
    }

    @Override
    public String getRemoteId() {
        return mRemoteId;
    }

    public static Tag fromCursor(Cursor c) {
        Tag entity = new Tag(c.getLong(0), c.getString(1), c.getString(2));
        return entity;
    }

    public static HashMap<String, SyncResource> responseToMap(String response) {
        HashMap<String, SyncResource> map = new HashMap<String, SyncResource>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonEntry = jsonArray.getJSONObject(i);
                map.put(jsonEntry.getString("_id"), new Tag(0, jsonEntry.getString("_id"), jsonEntry.getString("label")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues args = new ContentValues();
        args.put(_ID, (mId == 0 ? null : mId));
        args.put(COL_REMOTE_ID, mRemoteId);
        args.put(COL_LABEL, mLabel);
        return args;
    }

}
