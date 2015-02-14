package com.glevel.nanar.movies;

import android.app.Application;
import android.graphics.Typeface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by guillaume on 6/2/14.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.ic_chuck)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);

        // fonts caching
        loadFonts();
    }

    /**
     * Loads the required fonts.
     */
    private void loadFonts() {
        FONTS.yard = Typeface.createFromAsset(getAssets(), "fonts/yard_sale.ttf");
    }

    public static class FONTS {
        public static Typeface yard;
    }

}
