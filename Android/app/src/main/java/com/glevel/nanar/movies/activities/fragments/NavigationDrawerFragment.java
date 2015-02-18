package com.glevel.nanar.movies.activities.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glevel.nanar.movies.R;
import com.glevel.nanar.movies.activities.adapters.NavigationAdapter;
import com.glevel.nanar.movies.activities.interfaces.OnListItemSelected;
import com.glevel.nanar.movies.models.Tag;
import com.glevel.nanar.movies.models.navigation.NavDrawerLink;
import com.glevel.nanar.movies.models.navigation.NavDrawerTag;
import com.glevel.nanar.movies.models.navigation.NavItem;
import com.glevel.nanar.movies.providers.ContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnListItemSelected<NavItem> {

    private static final String TAG = "NavigationDrawerFragment";

    private static final int GET_TAGS = 1;

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    private RecyclerView mDrawerListView;
    private int mCurrentSelectedPosition = 0;
    private List<NavItem> mNavItems = new ArrayList<>();
    private NavigationAdapter mNavigationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        selectItem(mCurrentSelectedPosition);

        getLoaderManager().initLoader(GET_TAGS, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        setupUI(rootView);
        return rootView;
    }

    private void setupUI(View rootView) {
        mDrawerListView = (RecyclerView) rootView.findViewById(R.id.drawer_list);
        mDrawerListView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mNavigationAdapter = new NavigationAdapter(mNavItems, this);
        mDrawerListView.setAdapter(mNavigationAdapter);

        // set up the drawer's list view with items and click listener
        mNavItems.add(new NavDrawerLink(R.drawable.ic_grid, getString(R.string.browse_videos), new BrowseFragment()));
        mNavItems.add(new NavDrawerLink(R.drawable.ic_star_inactive, getString(R.string.my_favourites), new FavoritesFragment()));
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_TAGS:
                return new CursorLoader(getActivity().getApplicationContext(), ContentProvider.URI_TAGS, null, null, null, "RANDOM() LIMIT 10");
            default:
                throw new IllegalStateException("Cannot create Loader with id[" + id + "]");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (mNavigationAdapter != null && cursor != null) {
            DatabaseUtils.dumpCursor(cursor);

            removeTagsEntries();

            Tag tag;
            while (cursor.moveToNext()) {
                tag = Tag.fromCursor(cursor);
                mNavItems.add(new NavDrawerTag(tag.getLabel()));
            }

            mNavigationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        if (mNavigationAdapter != null) {
            removeTagsEntries();
            mNavigationAdapter.notifyDataSetChanged();
        }
    }

    private void removeTagsEntries() {
        // remove all tags entries
        int n = mNavItems.size() - 1;
        while (n > 2) {
            mNavItems.remove(n);
            n--;
        }
    }

    public List<NavItem> getNavItems() {
        return mNavItems;
    }

    @Override
    public void onItemSelected(NavItem navItem) {
        selectItem(mNavItems.indexOf(navItem));
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }


}
