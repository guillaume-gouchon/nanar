package com.glevel.nanar.models.navigation;

/**
 * Created by guillaume on 04/06/14.
 */
public abstract class NavItem {

    public abstract boolean isHeader();

    protected String text;

    public String getText() {
        return text;
    }

}