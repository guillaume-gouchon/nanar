package com.glevel.nanar.models;

import android.database.Cursor;

/**
 * Created by guillaume on 6/2/14.
 */
public interface RestResource {

    public Cursor responseToCursor(String response);

}
