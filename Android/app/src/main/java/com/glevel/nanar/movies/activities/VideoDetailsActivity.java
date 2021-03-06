package com.glevel.nanar.movies.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.glevel.nanar.movies.MyApplication;
import com.glevel.nanar.movies.R;
import com.glevel.nanar.movies.models.Favorite;
import com.glevel.nanar.movies.models.Video;
import com.glevel.nanar.movies.providers.ContentProvider;
import com.glevel.nanar.movies.utils.ApplicationUtils;
import com.glevel.nanar.movies.utils.YoutubeHelper;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class VideoDetailsActivity extends ActionBarActivity implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = "VideoDetailsActivity";

    public static final String EXTRAS_VIDEO = "video";

    private Video mVideo;
    private boolean mIsFavorite;

    private YouTubePlayerSupportFragment mPlayerFragment;
    private View mBigMessage;
    private Animation mMessageAnimation;
    private TextView mBigMessageLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

        // retrieve video object
        Bundle extras = getIntent().getExtras();
        mVideo = extras.getParcelable(EXTRAS_VIDEO);

        // update action bar title
        setTitle(mVideo.getTitle());

        // prepare youtube fragment
        mPlayerFragment = new YouTubePlayerSupportFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mPlayerFragment).commit();
        mPlayerFragment.initialize(YoutubeHelper.YOUTUBE_DEV_KEY, this);

        // check if this video is in my favorites and update UI
        Cursor cursor = getContentResolver().query(ContentProvider.URI_FAVORITES, null, Favorite.COL_VIDEO_ID + "=?", new String[]{mVideo.getVideoId()}, null);
        mIsFavorite = cursor.getCount() > 0;

        setupUI();
    }

    private void setupUI() {
        mBigMessage = findViewById(R.id.message);
        mBigMessageLabel = (TextView) findViewById(R.id.messageLabel);
        mBigMessageLabel.setTypeface(MyApplication.FONTS.yard);
        mMessageAnimation = AnimationUtils.loadAnimation(this, R.anim.big_message_animation);
        mMessageAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBigMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBigMessage.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.video_details, menu);
        menu.findItem(R.id.action_favorite).setVisible(!mIsFavorite);
        menu.findItem(R.id.action_unfavorite).setVisible(mIsFavorite);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_favorite:
                favoriteVideo();
                return true;
            case R.id.action_unfavorite:
                unFavoriteVideo();
                return true;
            case R.id.action_share:
                shareVideo();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareVideo() {
        ApplicationUtils.share(this, getString(R.string.share_video_subject, getString(R.string.app_name)), YoutubeHelper.getVideoLink(mVideo.getVideoId()), 0);
    }

    private void favoriteVideo() {
        Log.d(TAG, "Adding video to favorite...");
        getContentResolver().insert(ContentProvider.URI_FAVORITES, mVideo.toContentValues());
        mIsFavorite = true;
        showMessage(R.string.message_add_to_favorites, R.color.big_message_green);
        supportInvalidateOptionsMenu();
    }

    private void unFavoriteVideo() {
        Log.d(TAG, "Removing video from favorite...");
        getContentResolver().delete(ContentProvider.URI_FAVORITES, Favorite.COL_VIDEO_ID + "=?", new String[]{mVideo.getVideoId()});
        mIsFavorite = false;
        showMessage(R.string.message_remove_to_favorites, R.color.big_message_red);
        supportInvalidateOptionsMenu();
    }

    private void showMessage(int textResource, int color) {
        mBigMessageLabel.setText(textResource);
        mBigMessageLabel.setTextColor(getResources().getColor(color));
        mBigMessage.setVisibility(View.VISIBLE);
        mBigMessage.startAnimation(mMessageAnimation);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
            youTubePlayer.setShowFullscreenButton(false);
            youTubePlayer.cueVideo(mVideo.getVideoId());
        }
        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                getSupportActionBar().hide();
            }

            @Override
            public void onPaused() {
                getSupportActionBar().show();
            }

            @Override
            public void onStopped() {
                getSupportActionBar().show();
            }

            @Override
            public void onBuffering(boolean b) {
            }

            @Override
            public void onSeekTo(int i) {
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        mBigMessageLabel.setText(R.string.youtube_init_failure);
        mBigMessageLabel.setTextColor(getResources().getColor(R.color.big_message_red));
        mBigMessage.setVisibility(View.VISIBLE);
        mBigMessage.startAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.big_message_animation_long));
    }

}
