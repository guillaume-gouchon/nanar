package com.glevel.nanar.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.nanar.MyApplication;
import com.glevel.nanar.R;
import com.glevel.nanar.providers.rest.RestClient;
import com.glevel.nanar.providers.rest.RestHelper;
import com.glevel.nanar.utils.YoutubeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class AddVideoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "AddVideoActivity";

    private String mVideoTitle;
    private String mVideoId;
    private Button mPublishButton;
    private TextView mMessageTV;
    private View mVideoView;
    private View mBigMessage;
    private TextView mBigMessageLabel;
    private Animation mMessageAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.publish_video_activity_title);

        setContentView(R.layout.activity_add_video);

        setupUI();

        try {
            // retrieve video data
            Bundle extras = getIntent().getExtras();
            String text = extras.getString(Intent.EXTRA_TEXT);
            String[] h = text.replaceAll("http://", "").split(":");
            mVideoTitle = h[0];
            mVideoId = h[1].split("/")[1];
            if (mVideoId.isEmpty()) {
                showMessage(R.string.error_bad_video, R.color.big_message_red, true, true, true);
            }
        } catch (Exception e) {
            showMessage(R.string.error_bad_video, R.color.big_message_red, true, true, true);
        }

        updateVideoDetails();
    }

    private void setupUI() {
        mVideoView = findViewById(R.id.video);

        mPublishButton = (Button) findViewById(R.id.publish);
        mPublishButton.setOnClickListener(this);

        mBigMessage = findViewById(R.id.message);
        mBigMessageLabel = (TextView) findViewById(R.id.messageLabel);
        mBigMessageLabel.setTypeface(MyApplication.FONTS.yard);
        mMessageAnimation = AnimationUtils.loadAnimation(this, R.anim.big_message_animation_long);
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

    private void updateVideoDetails() {
        ((TextView) mVideoView.findViewById(R.id.title)).setText(mVideoTitle);
        ImageLoader.getInstance().displayImage(YoutubeHelper.getVideoThumbnail(mVideoId), (ImageView) mVideoView.findViewById(R.id.thumbnail));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish:
                publishVideo();
                break;
        }
    }

    private void publishVideo() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", mVideoTitle));
        params.add(new BasicNameValuePair("url", mVideoId));

        try {
            new RestClient(RestHelper.HttpMethod.POST, RestHelper.VIDEOS_URL, null, params, new ProgressDialog(this)) {
                @Override
                protected void onPostExecute(RestResponse response) {
                    super.onPostExecute(response);
                    Log.d(TAG, "publish video response : " + response.getResponseBody());
                    if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        showMessage(R.string.success_video_added, R.color.big_message_green, true, true, false);
                    } else if (response.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        showMessage(R.string.error_video_already_exist, R.color.big_message_red, true, true, false);
                    } else {
                        showMessage(R.string.error_add_video, R.color.big_message_red, false, false, false);
                    }
                }
            }.execute();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void showMessage(int textResource, int color, boolean doHidePublishButton, boolean finishActivity, boolean hideLayout) {
        if (doHidePublishButton) {
            mPublishButton.setVisibility(View.GONE);
        }

        if (hideLayout) {
            mVideoView.setVisibility(View.GONE);
        }

        if (finishActivity) {
            mMessageAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

        mBigMessageLabel.setText(textResource);
        mBigMessageLabel.setTextColor(getResources().getColor(color));
        mBigMessage.setVisibility(View.VISIBLE);
        mBigMessage.startAnimation(mMessageAnimation);
    }

}
