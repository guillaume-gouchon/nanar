package com.glevel.nanar.providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.glevel.nanar.models.Favorite;
import com.glevel.nanar.models.Tag;
import com.glevel.nanar.models.Video;


/**
 * Created by guillaume on 5/27/14.
 */
public class ContentProvider extends android.content.ContentProvider {

    public static final String AUTHORITY = "com.glevel.nanar";
    private static final String CONTENT_URI = "content://" + AUTHORITY + "/";

    private DatabaseHelper dbHelper;

    private static final int TYPE_TAGS = 10;
    private static final int TYPE_VIDEOS = 20;
    private static final int TYPE_FAVORITES = 30;

    public static final Uri URI_TAGS = Uri.parse(CONTENT_URI + "tags");
    public static final Uri URI_VIDEOS = Uri.parse(CONTENT_URI + "videos");
    public static final Uri URI_FAVORITES = Uri.parse(CONTENT_URI + "favorites");

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, "tags", TYPE_TAGS);
        sURIMatcher.addURI(AUTHORITY, "videos", TYPE_VIDEOS);
        sURIMatcher.addURI(AUTHORITY, "favorites", TYPE_FAVORITES);
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
            case TYPE_VIDEOS:
                queryBuilder.setTables(Video.TABLE_NAME);
                break;
            case TYPE_FAVORITES:
                queryBuilder.setTables(Favorite.TABLE_NAME);
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
        long id;
        switch (uriType) {
            case TYPE_TAGS:
                id = sqlDB.insert(Tag.TABLE_NAME, null, values);
                insertedId = ContentUris.withAppendedId(URI_TAGS, id);
                break;
            case TYPE_VIDEOS:
                id = sqlDB.insert(Video.TABLE_NAME, null, values);
                insertedId = ContentUris.withAppendedId(URI_VIDEOS, id);
                break;
            case TYPE_FAVORITES:
                id = sqlDB.insert(Favorite.TABLE_NAME, null, values);
                insertedId = ContentUris.withAppendedId(URI_FAVORITES, id);
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
            case TYPE_VIDEOS:
                rowsDeleted = sqlDB.delete(Video.TABLE_NAME, selection, selectionArgs);
                break;
            case TYPE_FAVORITES:
                rowsDeleted = sqlDB.delete(Favorite.TABLE_NAME, selection, selectionArgs);
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
            case TYPE_VIDEOS:
                rowsUpdated = sqlDB.update(Video.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
