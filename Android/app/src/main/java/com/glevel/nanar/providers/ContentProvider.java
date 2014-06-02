package com.glevel.nanar.providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.glevel.nanar.models.Tag;


/**
 * Created by guillaume on 5/27/14.
 */
public class ContentProvider extends android.content.ContentProvider {

    private DatabaseHelper dbHelper;


    private static final String AUTHORITY = "com.glevel.nanar";
    private static final String CONTENT_URI = "content://" + AUTHORITY + "/";

    private static final int TYPE_TAGS = 10;
    public static final Uri URI_TAGS = Uri.parse(CONTENT_URI + "tags");
    private static final int TYPE_VIDEOS = 20;
    public static final Uri URI_VIDEOS = Uri.parse(CONTENT_URI + "videos");

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, "tags", TYPE_TAGS);
        sURIMatcher.addURI(AUTHORITY, "videos", TYPE_VIDEOS);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TYPE_TAGS:
                queryBuilder.setTables(Tag.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        Uri insertedId = null;
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TYPE_TAGS:
                long id = sqlDB.insert(Tag.TABLE_NAME, null, values);
                insertedId = ContentUris.withAppendedId(URI_TAGS, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(insertedId, null);
        return insertedId;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TYPE_TAGS:
                rowsDeleted = sqlDB.delete(Tag.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TYPE_TAGS:
                rowsUpdated = sqlDB.update(Tag.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
