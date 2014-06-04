package com.glevel.nanar.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.glevel.nanar.R;
import com.glevel.nanar.models.navigation.Item;
import com.glevel.nanar.models.navigation.NavDrawerHeader;
import com.glevel.nanar.models.navigation.NavDrawerItem;

import java.util.List;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavigationAdapter extends ArrayAdapter<Item> {

    public NavigationAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (item.isHeader()) {
                v = inflater.inflate(R.layout.navigation_drawer_header_item, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
            } else {
                v = inflater.inflate(R.layout.navigation_drawer_item, null);
            }


        }

        TextView tv = (TextView) v.findViewById(R.id.label);
        tv.setText(item.getTextResource());
        if (!item.isHeader()) {
            tv.setCompoundDrawablesWithIntrinsicBounds(((NavDrawerItem) item).getIcon(), 0, 0, 0);
        }

        return v;
    }

}
