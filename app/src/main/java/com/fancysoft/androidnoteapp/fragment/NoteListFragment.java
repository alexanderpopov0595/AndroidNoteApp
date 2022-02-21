package com.fancysoft.androidnoteapp.fragment;

import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;

/**
 * Represents app fragment containing list of notes
 */
public class NoteListFragment extends Fragment {

    /**
     * Passes note list fragment layout to super constructor
     */
    public NoteListFragment() {
        super(R.layout.fragment_note_list);
    }
}