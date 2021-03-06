package com.glevel.nanar.movies.activities.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.nanar.movies.R;
import com.glevel.nanar.movies.models.Video;
import com.glevel.nanar.movies.utils.YoutubeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by guillaume on 6/2/14.
 */
public class VideoAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;
    private final ImageLoader mImageLoader;

    public VideoAdapter(Context context, int flags) {
        super(context, null, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.video_item, null);
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
        } else {
            viewHolder = (ViewHolder) view.getTag(R.string.viewholder);
        }

        Video video = Video.fromCursor(cursor);

        viewHolder.title.setText(video.getTitle());
        mImageLoader.displayImage(YoutubeHelper.getVideoThumbnail(video.getVideoId()), viewHolder.thumbnail);
    }

    public Video getVideo(int position) {
        Cursor cursor = getCursor();
        if (cursor.moveToPosition(position)) {
            return Video.fromCursor(cursor);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

}
