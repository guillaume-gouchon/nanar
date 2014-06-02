package com.glevel.nanar.activities.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.glevel.nanar.providers.ContentProvider;

/**
 * Created by guillaume on 6/2/14.
 */
public class FavoritesFragment extends VideoListFragment {

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_VIDEOS:
                return new CursorLoader(getActivity().getApplicationContext(), ContentProvider.URI_TAGS, null, null, null, null);
            default:
                throw new IllegalStateException("Cannot create Loader with id[" + id + "]");
        }
    }

}
