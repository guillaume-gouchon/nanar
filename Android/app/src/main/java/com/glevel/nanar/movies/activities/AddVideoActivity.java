package com.glevel.nanar.movies.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.nanar.movies.MyApplication;
import com.glevel.nanar.movies.R;
import com.glevel.nanar.movies.activities.adapters.AutoCompleteAdapter;
import com.glevel.nanar.movies.models.Video;
import com.glevel.nanar.movies.providers.rest.RestClient;
import com.glevel.nanar.movies.utils.YoutubeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddVideoActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "AddVideoActivity";

    private static final int GET_TAGS = 1;
    private static final int TAG_MINIMUM_LETTER = 3;

    private String mVideoTitle;
    private String mVideoId;
    private List<String> mTags = new ArrayList<String>();

    private Button mPublishButton;
    private TextView mMessageTV;
    private View mVideoView;
    private View mBigMessage;
    private TextView mBigMessageLabel;
    private Animation mMessageAnimation;
    private AutoCompleteTextView mTagInput;
    private ViewGroup mTagsLayout;

    private SimpleCursorAdapter mAutoCompleteAdapter;

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
            if (mVideoId == null || mVideoId.equals("")) {
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

        mTagInput = (AutoCompleteTextView) findViewById(R.id.tagInput);
        mTagInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mTagInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        && !mTagInput.getEditableText().toString().isEmpty() && mTagInput.getEditableText().toString().length() >= TAG_MINIMUM_LETTER) {
                    addTag(mTagInput.getEditableText().toString());
                    return true;
                }
                return false;
            }
        });

        // set adapter for auto complete
        mAutoCompleteAdapter = new AutoCompleteAdapter(getApplicationContext());
        mTagInput.setAdapter(mAutoCompleteAdapter);
        mTagInput.setThreshold(AutoCompleteAdapter.AUTO_COMPLETE_MINIMUM_LETTER);
        mTagInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                addTag(((TextView) view).getText().toString());
            }
        });

        mTagsLayout = (ViewGroup) findViewById(R.id.tagsLayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish:
                publishVideo();
                break;
            case R.id.tag:
                removeTag(view);
                break;
        }
    }

    private void addTag(String tag) {
        String formattedTag = formatTag(tag);
        Log.d(TAG, "Adding tag " + formattedTag);
        mTags.add(formattedTag);

        // add view
        View tagView = View.inflate(getApplicationContext(), R.layout.tag, null);
        TextView tagLabelView = ((TextView) tagView.findViewById(R.id.tag_label));
        tagLabelView.setText("#" + formattedTag);
        tagView.setTag(R.string.add_tag, formattedTag);
        tagView.setOnClickListener(this);
        mTagsLayout.addView(tagView);

        mTagInput.setText(null);
        closeKeyboard();
    }

    private void closeKeyboard() {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mTagInput.getWindowToken(), 0);
    }

    private String formatTag(String tag) {
        return tag.replaceAll(" ", "").replaceAll("\\#", "");
    }

    private void removeTag(View view) {
        mTags.remove(view.getTag(R.string.add_tag));
        mTagsLayout.removeView(view);
    }

    private void updateVideoDetails() {
        ((TextView) mVideoView.findViewById(R.id.title)).setText(mVideoTitle);
        ImageLoader.getInstance().displayImage(YoutubeHelper.getVideoThumbnail(mVideoId), (ImageView) mVideoView.findViewById(R.id.thumbnail));
    }

    private void publishVideo() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", mVideoTitle));
        params.add(new BasicNameValuePair("url", mVideoId));
        StringBuffer tags = new StringBuffer();
        for (int i = 0; i < mTags.size(); i++) {
            tags.append(mTags.get(i) + " ");
        }
        params.add(new BasicNameValuePair("tags", Arrays.toString(mTags.toArray()).replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", " ")));

        try {
            new RestClient(RestClient.HttpMethod.POST, Video.RESOURCE_URL, null, params, new ProgressDialog(this)) {
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
            mTagInput.setVisibility(View.GONE);
            mTagsLayout.setVisibility(View.GONE);
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
