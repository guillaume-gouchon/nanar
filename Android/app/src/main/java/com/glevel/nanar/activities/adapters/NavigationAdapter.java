package com.glevel.nanar.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.glevel.nanar.R;
import com.glevel.nanar.models.NavDrawerItem;

import java.util.List;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavigationAdapter extends ArrayAdapter<NavDrawerItem> {

    public NavigationAdapter(Context context, int resource, List<NavDrawerItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavDrawerItem item = getItem(position);

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.navigation_drawer_item, null);
        }

        TextView tv = (TextView) v.findViewById(android.R.id.text1);
        tv.setCompoundDrawablesWithIntrinsicBounds(item.getIcon(), 0, 0, 0);
        tv.setText(item.getTextResource());

        return v;
    }

}
