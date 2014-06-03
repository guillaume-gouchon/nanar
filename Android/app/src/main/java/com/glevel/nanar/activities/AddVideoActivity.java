package com.glevel.nanar.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.nanar.R;
import com.glevel.nanar.providers.rest.RestClient;
import com.glevel.nanar.providers.rest.RestHelper;
import com.glevel.nanar.utils.YoutubeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class AddVideoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "AddVideoActivity";

    private String mVideoTitle;
    private String mVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        // retrieve video data
        Bundle extras = getIntent().getExtras();
        String text = extras.getString(Intent.EXTRA_TEXT);
        String[] h = text.replaceAll("http://", "").split(":");
        mVideoTitle = h[0];
        mVideoId = h[1].split("/")[1];

        setupUI();
    }

    private void setupUI() {
        View videoView = findViewById(R.id.video);
        ((TextView) videoView.findViewById(R.id.title)).setText(mVideoTitle);
        ImageLoader.getInstance().displayImage(YoutubeHelper.getVideoThumbnail(mVideoId), (ImageView) videoView.findViewById(R.id.thumbnail));

        findViewById(R.id.publish).setOnClickListener(this);
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
                protected void onPostExecute(String json) {
                    super.onPostExecute(json);
                    Log.d(TAG, "publish video response : " + json);
                }
            }.execute();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
