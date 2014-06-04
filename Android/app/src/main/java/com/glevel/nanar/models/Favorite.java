package com.glevel.nanar.models;

/**
 * Created by guillaume on 6/2/14.
 */
public class Favorite extends Video {

    public static final String TABLE_NAME = "favorites";
    
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_VIDEO_ID + " TEXT, "
            + COL_TITLE + " TEXT, " + COL_CREATED_DATE + " DATE);";


}
