package com.glevel.nanar.models.navigation;


import android.app.Fragment;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavDrawerItem extends Item {

    private final int icon;
    private final Fragment targetFragment;

    public NavDrawerItem(int icon, int text, Fragment targetFragment) {
        this.icon = icon;
        this.textResource = text;
        this.targetFragment = targetFragment;
    }

    public int getIcon() {
        return icon;
    }

    public Fragment getTargetFragment() {
        return targetFragment;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

}
