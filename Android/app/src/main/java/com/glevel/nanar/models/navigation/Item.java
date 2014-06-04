package com.glevel.nanar.models.navigation;

/**
 * Created by guillaume on 04/06/14.
 */
public abstract class Item {

    protected int textResource;

    public int getTextResource() {
        return textResource;
    }

    public abstract boolean isHeader();


}