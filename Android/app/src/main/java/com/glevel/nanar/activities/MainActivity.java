package com.glevel.nanar.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.glevel.nanar.R;
import com.glevel.nanar.activities.adapters.AutoCompleteAdapter;
import com.glevel.nanar.activities.fragments.BrowseFragment;
import com.glevel.nanar.activities.fragments.NavigationDrawerFragment;
import com.glevel.nanar.models.navigation.NavItem;
import com.glevel.nanar.utils.ApplicationUtils;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "MainActivity";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // increase number of launches
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int nbLaunches = prefs.getInt(ApplicationUtils.PREFS_NB_LAUNCHES, 0);
        prefs.edit().putInt(ApplicationUtils.PREFS_NB_LAUNCHES, ++nbLaunches).commit();
        int nbLaunchesBeforeRateDialog = prefs.getInt(ApplicationUtils.PREFS_RATE_DIALOG_IN, ApplicationUtils.NB_LAUNCHES_RATE_DIALOG_APPEARS);
        prefs.edit().putInt(ApplicationUtils.PREFS_RATE_DIALOG_IN, --nbLaunchesBeforeRateDialog).commit();
        ApplicationUtils.showRateDialogIfNeeded(this);

        // set up the drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment targetFragment;
        if (position == 0) {
            targetFragment = new BrowseFragment();
        } else {
            NavItem itemSelected = mNavigationDrawerFragment.getNavItems().get(position);
            targetFragment = itemSelected.getTargetFragment();
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, targetFragment).commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);

            // associate searchable configuration with the SearchView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setSuggestionsAdapter(new AutoCompleteAdapter(getApplicationContext()));
                searchView.setOnSuggestionListener(new AutoCompleteAdapter.MySuggestionListener(searchView));
            }

            restoreActionBar();

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_share:
                ApplicationUtils.share(this, getString(R.string.share_subject, getString(R.string.app_name)),
                        getString(R.string.share_message, getPackageName()), R.drawable.ic_launcher);
                return true;
            case R.id.action_rate_app:
                ApplicationUtils.rateTheApp(this);
                return true;
            case R.id.search:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    onSearchRequested();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, query);
        }
    }

}
