package com.glevel.nanar.movies.models.navigation;


import android.support.v4.app.Fragment;

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