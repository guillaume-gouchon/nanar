package com.glevel.nanar.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.glevel.nanar.R;
import com.glevel.nanar.models.navigation.NavDrawerLink;
import com.glevel.nanar.models.navigation.NavItem;

import java.util.List;

/**
 * Created by guillaume on 5/28/14.
 */
public class NavigationAdapter extends ArrayAdapter<NavItem> {

    public NavigationAdapter(Context context, int resource, List<NavItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavItem item = getItem(position);

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
        tv.setText(item.getText());
        if (!item.isHeader()) {
            tv.setCompoundDrawablesWithIntrinsicBounds(((NavDrawerLink) item).getIcon(), 0, 0, 0);
        }

        return v;
    }

}
