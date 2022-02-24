package com.fancysoft.androidnoteapp.adapter;

import android.view.View;
import android.widget.TextView;

import com.fancysoft.androidnoteapp.R;

import lombok.Getter;

/**
 * Used to store links to views
 */
@Getter
class ViewHolder {

    private final TextView noteIdView, noteLastUpdateView, noteContentView;

    public ViewHolder(View view) {
        noteIdView = view.findViewById(R.id.note_id);
        noteLastUpdateView = view.findViewById(R.id.note_last_update);
        noteContentView = view.findViewById(R.id.note_content);
    }
}

