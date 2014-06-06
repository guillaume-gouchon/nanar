package com.glevel.nanar.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.glevel.nanar.R;
import com.glevel.nanar.activities.adapters.AutoCompleteAdapter;
import com.glevel.nanar.activities.fragments.FilterFragment;

/**
 * Created by guillaume on 6/4/14.
 */
public class SearchActivity extends Activity {

    private static final int SEARCH = 1;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        // associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(new AutoCompleteAdapter(getApplicationContext()));
        searchView.setOnSuggestionListener(new AutoCompleteAdapter.MySuggestionListener(searchView));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "Searching for " + query);
            Fragment fragment = new FilterFragment();
            Bundle args = new Bundle();
            args.putString(FilterFragment.EXTRA_FILTER, query);
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
