package com.glevel.nanar.activities.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.glevel.nanar.models.Video;
import com.glevel.nanar.providers.rest.RestHelper;
import com.glevel.nanar.providers.rest.RestLoader;

/**
 * Created by guillaume on 6/2/14.
 */
public class BrowseFragment extends VideoListFragment {

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_VIDEOS:
                return new RestLoader<Video>(getActivity().getApplicationContext(), RestHelper.HttpMethod.GET, RestHelper.GET_VIDEOS_URL, null, null);
            default:
                throw new IllegalStateException("Cannot create Loader with id[" + id + "]");
        }
    }

}
