package com.glevel.nanar.models.navigation;

import android.app.Fragment;

/**
 * Created by guillaume on 04/06/14.
 */
public abstract class NavItem {

    public abstract boolean isHeader();

    public abstract int getIcon();

    public abstract Fragment getTargetFragment();

    protected String text;

    public String getText() {
        return text;
    }


}