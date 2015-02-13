package com.glevel.nanar.activities.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.nanar.R;
import com.glevel.nanar.activities.interfaces.OnListItemSelected;
import com.glevel.nanar.models.navigation.NavItem;

import java.util.List;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private List<NavItem> mItems;
    private final OnListItemSelected<NavItem> mClickListener;

    public NavigationAdapter(List<NavItem> items, OnListItemSelected<NavItem> clickListener) {
        mItems = items;
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        NavItem navItem = mItems.get(position);

        viewHolder.title.setText(navItem.getText());
        viewHolder.image.setImageResource(navItem.getIcon());

        viewHolder.itemView.setTag(navItem);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemSelected((NavItem) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }

}
