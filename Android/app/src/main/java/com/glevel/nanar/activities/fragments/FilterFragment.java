package com.glevel.nanar.activities.fragments;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glevel.nanar.R;
import com.glevel.nanar.models.Video;
import com.glevel.nanar.providers.ContentProvider;

/**
 * Created by guillaume on 6/2/14.
 */
public class FilterFragment extends VideoListFragment {

    public static final String EXTRA_FILTER = "filter";
    private String mFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        mFilter = args.getString(EXTRA_FILTER);

        getActivity().getActionBar().setTitle("#" + mFilter);

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    protected int getEmptyViewMessage() {
        return R.string.no_result;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_VIDEOS:
                return new CursorLoader(getActivity().getApplicationContext(), ContentProvider.URI_VIDEOS, null, Video.COL_TITLE + " LIKE ? OR " + Video.COL_TAGS + " LIKE ?", new String[]{"%" + mFilter + "%", "%" + mFilter + "%"}, Video.COL_CREATED_DATE + " DESC");
            default:
                throw new IllegalStateException("Cannot create Loader with id[" + id + "]");
        }
    }

}
