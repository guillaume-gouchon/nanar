package com.glevel.nanar.movies.models.navigation;


import android.support.v4.app.Fragment;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavDrawerLink extends NavItem {

    private final int icon;
    private final Fragment targetFragment;

    public NavDrawerLink(int icon, String text, Fragment targetFragment) {
        this.icon = icon;
        this.text = text;
        this.targetFragment = targetFragment;
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
