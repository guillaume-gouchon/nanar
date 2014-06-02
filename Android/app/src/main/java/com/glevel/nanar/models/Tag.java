package com.glevel.nanar.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by guillaume on 5/27/14.
 */
public class Tag {

    public static final String TABLE_NAME = "tags";
    public static final String _ID = "_id";
    public static String COL_LABEL = "label";
    public static String TAGS_DEFAULT_SORT_ORDER = "label";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_LABEL + " INTEGER);";

    private final long mId;
    private final String mLabel;

    public Tag(long id, String label) {
        mId = id;
        mLabel = label;
    }

    public long getId() {
        return mId;
    }

    public String getLabel() {
        return mLabel;
    }

    public static Tag fromCursor(Cursor c) {
        Tag entity = new Tag(c.getLong(0), c.getString(1));
        return entity;
    }

    public static ContentValues toContentValues(Tag entity) {
        ContentValues args = new ContentValues();
        args.put(_ID, (entity.getId() == 0 ? null : entity.getId()));
        args.put(COL_LABEL, entity.getLabel());
        return args;
    }

}
