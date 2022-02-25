package com.fancysoft.androidnoteapp.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.db.DataBaseAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.utils.Constants;
import com.fancysoft.androidnoteapp.utils.Helper;

import java.util.Properties;

/**
 * Represents app fragment to create new note
 */
public class EditNoteFragment extends Fragment {

    private DataBaseAdapter dbAdapter;
    /**
     * Passes add note fragment layout to super constructor
     */
    public EditNoteFragment() {
        super(R.layout.fragment_edit_note);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Properties properties = Helper.getProperties(this.getContext().getApplicationContext());
        DataBaseProperties dbProperties = new DataBaseProperties(properties);

        DataBaseHelper dbHelper = new DataBaseHelper(this.getContext().getApplicationContext(), dbProperties);
        dbAdapter = new DataBaseAdapter(dbHelper, dbProperties);
        dbAdapter.open();

        Bundle bundle = this.getArguments();

        long noteId = bundle.getLong(Constants.NOTE_ID_KEY);

        Note note = dbAdapter.get(noteId);

        dbAdapter.close();

        TextView noteIdView = view.findViewById(R.id.note_id);
        noteIdView.setText(String.valueOf(note.getId()));

        TextView noteLastUpdateView = view.findViewById(R.id.note_last_update);
        noteLastUpdateView.setText(Helper.millisToString(note.getLastUpdate()));

        EditText noteContentView = view.findViewById(R.id.note_content);
        noteContentView.setText(note.getContent());

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
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
                EditText editText = view.findViewById(R.id.note_content);
                String content = editText.getText().toString();

                Note updatedNote = new Note(noteId, System.currentTimeMillis(), content);

                dbAdapter.open();
                dbAdapter.update(updatedNote);
                dbAdapter.close();

                Fragment fragment = new EditNoteFragment();
                fragment.setArguments(bundle);

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.note_list_fragment, fragment)
                        .commit();
            }
        });
    }

    /**
     * Closes db connection
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}