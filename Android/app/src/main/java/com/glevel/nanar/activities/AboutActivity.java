package com.glevel.nanar.activities;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.glevel.nanar.R;
import com.glevel.nanar.utils.ApplicationUtils;

/**
 * Created by guillaume on 6/4/14.
 */
public class AboutActivity extends ActionBarActivity {

    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupUI();
    }

    private void setupUI() {
        // activate the dialog links
        TextView creditsTV = (TextView) findViewById(R.id.aboutCredits);
        creditsTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView blogTV = (TextView) findViewById(R.id.aboutBlog);
        blogTV.setMovementMethod(LinkMovementMethod.getInstance());
        try {
            ((TextView) findViewById(R.id.version)).setText(getString(R.string.about_version, getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                ApplicationUtils.contactSupport(this);
                return true;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
