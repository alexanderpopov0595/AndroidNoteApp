package com.fancysoft.androidnoteapp.fragment;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.adapter.NoteAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.utils.Helper;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.note_list_fragment, new AddNoteFragment())
                .commit());

        DataBaseProperties dbProperties = new DataBaseProperties(Helper.getProperties(this.getContext().getApplicationContext()));
        DataBaseHelper dbHelper = new DataBaseHelper(this.getContext().getApplicationContext(), dbProperties);
        DataBaseAdapter dbAdapter = new DataBaseAdapter(dbHelper ,dbProperties);

        Cursor cursor = dbAdapter.getAll();
        NoteAdapter adapter = new NoteAdapter(this.getContext().getApplicationContext(), cursor, dbProperties);

        ListView noteList = view.findViewById(R.id.note_list);
        noteList.setAdapter(adapter);

    }

}