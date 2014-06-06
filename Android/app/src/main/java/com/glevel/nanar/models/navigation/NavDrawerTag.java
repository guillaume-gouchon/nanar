package com.glevel.nanar.models.navigation;


import android.app.Fragment;
import android.os.Bundle;

import com.glevel.nanar.R;
import com.glevel.nanar.activities.fragments.FilterFragment;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavDrawerTag extends NavItem {

    private final int icon;
    private final Fragment targetFragment;

    public NavDrawerTag(String text) {
        this.icon = R.drawable.ic_hash_tag;
        this.text = text;
        this.targetFragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(FilterFragment.EXTRA_FILTER, text);
        targetFragment.setArguments(args);
    }

    @Override
    public int getIcon() {
        return icon;
    }

    @Override
    public Fragment getTargetFragment() {
        return targetFragment;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

}
