package com.glevel.nanar.models;

import android.content.ContentValues;

/**
 * Created by guillaume on 6/6/14.
 */
public interface SyncResource {

    public ContentValues toContentValues();

    public long getId();

    public String getRemoteId();

}
