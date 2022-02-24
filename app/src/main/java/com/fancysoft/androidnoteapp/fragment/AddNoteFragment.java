package com.fancysoft.androidnoteapp.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.db.DataBaseAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.utils.Helper;

import java.util.Properties;

/**
 * Represents app fragment to create new note
 */
public class AddNoteFragment extends Fragment {

    private DataBaseAdapter dbAdapter;
    /**
     * Passes add note fragment layout to super constructor
     */
    public AddNoteFragment() {
        super(R.layout.fragment_add_note);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.note_list_fragment, new NoteListFragment())
                        .commit();
            }
        });

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputField = view.findViewById(R.id.input_field);
                String text = inputField.getText().toString();
                Note note = new Note(System.currentTimeMillis(), text);

                dbAdapter.open();
                dbAdapter.add(note);
                dbAdapter.close();

                //TODO replace with edit note fragment switch
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.note_list_fragment, new NoteListFragment())
                        .commit();
            }
        });
        run();
    }

    private void run() {
        Properties properties = Helper.getProperties(this.getContext().getApplicationContext());
        DataBaseProperties dbProperties = new DataBaseProperties(properties);

        DataBaseHelper dbHelper = new DataBaseHelper(this.getContext().getApplicationContext(), dbProperties);
        dbAdapter = new DataBaseAdapter(dbHelper, dbProperties);
    }

}