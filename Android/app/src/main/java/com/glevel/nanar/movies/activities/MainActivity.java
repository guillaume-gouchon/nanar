package com.glevel.nanar.movies.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.glevel.nanar.movies.R;
import com.glevel.nanar.movies.activities.adapters.AutoCompleteAdapter;
import com.glevel.nanar.movies.activities.fragments.BrowseFragment;
import com.glevel.nanar.movies.activities.fragments.NavigationDrawerFragment;
import com.glevel.nanar.movies.models.navigation.NavItem;
import com.glevel.nanar.movies.utils.ApplicationUtils;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "MainActivity";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * UI
     */
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        setContentView(R.layout.activity_main);

        // increase number of launches
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int nbLaunches = prefs.getInt(ApplicationUtils.PREFS_NB_LAUNCHES, 0);
        prefs.edit().putInt(ApplicationUtils.PREFS_NB_LAUNCHES, ++nbLaunches).apply();

        // show rate dialog if needed
        int nbLaunchesBeforeRateDialog = prefs.getInt(ApplicationUtils.PREFS_RATE_DIALOG_IN, ApplicationUtils.NB_LAUNCHES_RATE_DIALOG_APPEARS);
        prefs.edit().putInt(ApplicationUtils.PREFS_RATE_DIALOG_IN, --nbLaunchesBeforeRateDialog).apply();
        ApplicationUtils.showRateDialogIfNeeded(this);

        setupUI();
    }

    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);
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

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, targetFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(new AutoCompleteAdapter(getApplicationContext()));
        searchView.setOnSuggestionListener(new AutoCompleteAdapter.MySuggestionListener(searchView));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            // close drawer when open
            mDrawerLayout.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

}
