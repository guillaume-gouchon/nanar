package com.glevel.nanar.models.navigation;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavDrawerHeader extends Item {

    public NavDrawerHeader(int text) {
        this.textResource = text;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

}
