package com.glevel.nanar.activities.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.FilterQueryProvider;

import com.glevel.nanar.R;
import com.glevel.nanar.models.Tag;
import com.glevel.nanar.providers.ContentProvider;

/**
 * Created by guillaume on 6/6/14.
 */
public class AutoCompleteAdapter extends SimpleCursorAdapter {

    private static final String TAG = "AutoCompleteAdapter";

    private Context mContext;

    public static final int AUTO_COMPLETE_MINIMUM_LETTER = 2;

    public AutoCompleteAdapter(Context context) {
        super(context, R.layout.auto_complete_item, null, new String[]{Tag.COL_LABEL}, new int[]{R.id.text}, 0);

        mContext = context;

        setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public String convertToString(Cursor cursor) {
                // Get the label for this row out of the "state" column
                final int columnIndex = cursor.getColumnIndexOrThrow(Tag.COL_LABEL);
                final String str = "#" + cursor.getString(columnIndex);
                return str;
            }
        });

        setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                if (constraint.length() >= AUTO_COMPLETE_MINIMUM_LETTER) {
                    // Search for states whose names begin with the specified letters.
                    Cursor cursor = mContext.getContentResolver().query(ContentProvider.URI_TAGS, null, Tag.COL_LABEL + " LIKE ?", new String[]{"%" + constraint + "%"}, Tag.COL_LABEL + " ASC LIMIT 5");
                    return cursor;
                }
                return null;
            }
        });
    }

    public static class MySuggestionListener implements SearchView.OnSuggestionListener {

        private final SearchView searchView;

        public MySuggestionListener(SearchView searchView) {
            this.searchView = searchView;
        }

        @Override
        public boolean onSuggestionSelect(int position) {
            return query(position);
        }

        @Override
        public boolean onSuggestionClick(int position) {
            return query(position);
        }

        private boolean query(int position) {
            Log.d(TAG, "Selecting position " + position);
            Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
            Tag tag = Tag.fromCursor(cursor);
            searchView.setQuery(tag.getLabel(), true);
            return true;
        }

    }

}
