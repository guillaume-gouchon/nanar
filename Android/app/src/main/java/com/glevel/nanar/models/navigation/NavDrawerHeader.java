package com.glevel.nanar.models.navigation;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavDrawerHeader extends NavItem {

    public NavDrawerHeader(String text) {
        this.text = text;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

}
