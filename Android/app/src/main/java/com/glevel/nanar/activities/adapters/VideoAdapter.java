package com.glevel.nanar.activities.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.nanar.MyApplication;
import com.glevel.nanar.R;
import com.glevel.nanar.models.Video;
import com.glevel.nanar.utils.YoutubeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by guillaume on 6/2/14.
 */
public class VideoAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;
    private final ImageLoader mImageLoader;
    private final Animation inAnimation;

    public VideoAdapter(Context context, int flags) {
        super(context, null, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = ImageLoader.getInstance();
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.video_in_animation);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.video_list_item, null);
    }

    static class ViewHolder {
        TextView title;
        ImageView thumbnail;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder;
        if (view.getTag(R.string.viewholder) == null) {
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            view.setTag(R.string.viewholder, viewHolder);
            view.setAnimation(inAnimation);
        } else {
            viewHolder = (ViewHolder) view.getTag(R.string.viewholder);
        }

        viewHolder.title.setText(cursor.getString(Video.CURSOR_TITLE));
        mImageLoader.displayImage(YoutubeHelper.getVideoThumbnail(cursor.getString(Video.CURSOR_VIDEO_ID)), viewHolder.thumbnail);
    }

    public Video getVideo(int position) {
        Cursor cursor = getCursor();
        if (cursor.moveToPosition(position)) {
            return new Video(cursor.getString(Video.CURSOR_VIDEO_ID), cursor.getString(Video.CURSOR_TITLE));
        }
        throw new ArrayIndexOutOfBoundsException();
    }

}
