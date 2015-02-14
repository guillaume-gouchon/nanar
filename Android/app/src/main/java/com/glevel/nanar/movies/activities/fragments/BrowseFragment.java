package com.glevel.nanar.movies.activities.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glevel.nanar.movies.R;
import com.glevel.nanar.movies.providers.ContentProvider;

/**
 * Created by guillaume on 6/2/14.
 */
public class BrowseFragment extends VideoListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);

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
