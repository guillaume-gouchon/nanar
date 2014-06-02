package com.glevel.nanar.models;

import android.support.v4.app.Fragment;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavDrawerItem {

    private final int icon;
    private final int textResource;
    private final Fragment targetFragment;

    public NavDrawerItem(int icon, int text, Fragment targetFragment) {
        this.icon = icon;
        this.textResource = text;
        this.targetFragment = targetFragment;
    }

    public int getIcon() {
        return icon;
    }

    public int getTextResource() {
        return textResource;
    }

    public Fragment getTargetFragment() {
        return targetFragment;
    }
}
