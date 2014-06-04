package com.glevel.nanar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.glevel.nanar.R;

/**
 * Created by guillaume on 6/4/14.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupUI();
    }

    private void setupUI() {
        // activate the dialog links
        TextView creditsTV = (TextView) findViewById(R.id.aboutCredits);
        creditsTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView blogTV = (TextView) findViewById(R.id.aboutBlog);
        blogTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
