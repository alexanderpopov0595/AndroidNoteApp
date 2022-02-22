package com.fancysoft.androidnoteapp.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.note_list_fragment, new AddNoteFragment())
                .commit());
    }

}