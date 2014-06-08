package com.glevel.nanar.models.navigation;


import android.support.v4.app.Fragment;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavDrawerHeader extends NavItem {

    public NavDrawerHeader(String text) {
        this.text = text;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Override
    public Fragment getTargetFragment() {
        return null;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

}
