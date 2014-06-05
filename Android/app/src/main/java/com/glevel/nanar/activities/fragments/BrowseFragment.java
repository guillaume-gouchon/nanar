package com.glevel.nanar.activities.fragments;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glevel.nanar.R;
import com.glevel.nanar.providers.ContentProvider;

/**
 * Created by guillaume on 6/2/14.
 */
public class BrowseFragment extends VideoListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle(R.string.app_name);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_VIDEOS:
                return new CursorLoader(getActivity().getApplicationContext(), ContentProvider.URI_VIDEOS, null, null, null, null);
            default:
                throw new IllegalStateException("Cannot create Loader with id[" + id + "]");
        }
    }

}
