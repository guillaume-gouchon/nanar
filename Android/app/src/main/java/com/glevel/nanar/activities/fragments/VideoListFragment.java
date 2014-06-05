package com.glevel.nanar.activities.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glevel.nanar.R;
import com.glevel.nanar.activities.VideoDetailsActivity;
import com.glevel.nanar.activities.adapters.VideoAdapter;
import com.glevel.nanar.models.Video;
import com.glevel.nanar.providers.sync.SyncUtils;

/**
 * Created by guillaume on 5/27/14.
 */
public abstract class VideoListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    protected static final int GET_VIDEOS = 1;

    private VideoAdapter mVideoAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos_list, container, false);

        mVideoAdapter = new VideoAdapter(getActivity().getApplicationContext(), R.layout.video_list_item);
        setListAdapter(mVideoAdapter);

        // implements pull to refresh
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorScheme(R.color.action_bar_blue, R.color.launcher_yellow, R.color.launcher_yellow, R.color.launcher_yellow);
        mSwipeLayout.setOnRefreshListener(this);

        getLoaderManager().initLoader(GET_VIDEOS, null, this).forceLoad();

        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Video video = mVideoAdapter.getVideo(position);
        Bundle extras = new Bundle();
        extras.putParcelable(VideoDetailsActivity.EXTRAS_VIDEO, video);
        Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mVideoAdapter != null && cursor != null) {
            DatabaseUtils.dumpCursor(cursor);
            mVideoAdapter.swapCursor(cursor);
            if (mSwipeLayout.isRefreshing()) {
                mSwipeLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mVideoAdapter != null) {
            mVideoAdapter.swapCursor(null);
        }
    }

    @Override
    public void onRefresh() {
        SyncUtils.TriggerRefresh();
        getLoaderManager().restartLoader(GET_VIDEOS, null, this).forceLoad();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SyncUtils.CreateSyncAccount(activity);
    }

}
