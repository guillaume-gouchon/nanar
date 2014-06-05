package com.glevel.nanar.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.glevel.nanar.models.Favorite;
import com.glevel.nanar.models.Tag;
import com.glevel.nanar.models.Video;


/**
 * Created by guillaume on 5/27/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "nanar.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tag.CREATE_TABLE);
        db.execSQL(Video.CREATE_TABLE);
        db.execSQL(Favorite.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tag.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Video.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Favorite.TABLE_NAME);
        onCreate(db);
    }

}
