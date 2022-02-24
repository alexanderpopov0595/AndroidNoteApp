package com.fancysoft.androidnoteapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import androidx.annotation.RequiresApi;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.utils.Helper;

/**
 * Custom adapter for list of notes
 */
public class NoteAdapter extends CursorAdapter {

    private final DataBaseProperties dbProperties;

    public NoteAdapter(Context context, Cursor cursor, DataBaseProperties dbProperties) {
        super(context, cursor, 0);
        this.dbProperties = dbProperties;
    }

    // The newView method is used to inflate a new view and return it,
    // Doesn't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_list_item, parent ,false);

        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    //Used to bind all data to a given view
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        int idIndex = cursor.getColumnIndex(dbProperties.getIdColumn());
        int lastUpdateIndex = cursor.getColumnIndex(dbProperties.getLastUpdateColumn());
        int textIndex = cursor.getColumnIndex(dbProperties.getContentColumn());

        holder.getNoteIdView().setText(String.valueOf(cursor.getInt(idIndex)));
        holder.getNoteLastUpdateView().setText(Helper.millisToString(cursor.getInt(lastUpdateIndex)));
        holder.getNoteContentView().setText(cursor.getString(textIndex));
    }
}
