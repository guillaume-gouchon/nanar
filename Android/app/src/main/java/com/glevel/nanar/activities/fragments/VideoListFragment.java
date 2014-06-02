package com.glevel.nanar.activities.fragments;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glevel.nanar.R;
import com.glevel.nanar.activities.adapters.VideoAdapter;

/**
 * Created by guillaume on 5/27/14.
 */
public abstract class VideoListFragment extends android.support.v4.app.ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int GET_VIDEOS = 1;

    private VideoAdapter mVideoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse_videos, container, false);

        mVideoAdapter = new VideoAdapter(getActivity().getApplicationContext(), R.layout.video_list_item);
        setListAdapter(mVideoAdapter);

        getLoaderManager().initLoader(GET_VIDEOS, null, this).forceLoad();
        return rootView;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mVideoAdapter != null && cursor != null) {
            DatabaseUtils.dumpCursor(cursor);
            mVideoAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mVideoAdapter != null) {
            mVideoAdapter.swapCursor(null);
        }
    }

}
