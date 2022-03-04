package com.fancysoft.androidnoteapp.fragment;

import android.content.Context;
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
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.exception.model.AppException;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.repository.NoteRepository;
import com.fancysoft.androidnoteapp.repository.impl.NoteRepositoryImpl;
import com.fancysoft.androidnoteapp.utils.Constants;
import com.fancysoft.androidnoteapp.utils.Helper;

import java.util.Properties;

/**
 * Represents app fragment to create new note
 */
public class EditNoteFragment extends Fragment {

    private NoteRepository repository;
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

        initRepository();
        initNoteView(view, getNote());
        initBackButton(view);
        initSaveButton(view);
        initDeleteButton(view);
    }

    /**
     * Inits repository
     */
    private void initRepository() {
        Context context = this.requireContext().getApplicationContext();

        Properties properties = Helper.getProperties(context);
        DataBaseProperties dbProperties = new DataBaseProperties(properties);

        DataBaseHelper dbHelper = new DataBaseHelper(context, dbProperties);
        repository = new NoteRepositoryImpl(dbHelper, dbProperties);
    }

    /**
     * Retrieves note from database
     * @return retrieved note
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Note getNote() {
        long noteId = getNoteId();

        return repository.get(noteId);
    }

    /**
     * Retrieves saved note id from bundle
     * @return retrieved note id
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private long getNoteId() {
        Bundle bundle = getBundle();

        return bundle.getLong(Constants.NOTE_ID_KEY);
    }

    /**
     * Retrieves bundle
     * @return bundle with saved arguments
     * @throws AppException if bundle is null
     */
    private Bundle getBundle() {
        Bundle bundle = this.getArguments();

        if (bundle == null) {
            throw new AppException("Data wasn't passed between fragments");
        }

        return bundle;
    }

    /**
     * Passes note values to the UI
     * @param view - current view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNoteView(View view, Note note) {
        TextView noteIdView = view.findViewById(R.id.note_id);
        noteIdView.setText(String.valueOf(note.getId()));

        TextView noteLastUpdateView = view.findViewById(R.id.note_last_update);
        noteLastUpdateView.setText(Helper.millisToString(note.getLastUpdate()));

        EditText noteContentView = view.findViewById(R.id.note_content);
        noteContentView.setText(note.getContent());
    }

    /**
     * Inits "back" button, which should switch back to the list fragment     *
     * @param view - current layout
     */
    private void initBackButton(View view) {
        Button button = view.findViewById(R.id.back_button);
        button.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.note_list_fragment, new NoteListFragment())
                .commit());
    }

    /**
     * Inits "save" button, which should save note and refresh UI content     *
     * @param view - current layout
     */
    private void initSaveButton(View view) {
        Button button = view.findViewById(R.id.save_button);
        button.setOnClickListener(v -> {
            Note note = viewToNote(view);

            repository.update(note);

            Bundle bundle = getBundle();
            Fragment fragment = getFragment(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_list_fragment, fragment)
                    .commit();
        });
    }

    /**
     * Converts view to note
     * @param view - current view
     * @return converted note
     */
    private Note viewToNote(View view) {
        TextView idView = view.findViewById(R.id.note_id);
        long noteId = Long.parseLong(idView.getText().toString());

        EditText contentView = view.findViewById(R.id.note_content);
        String content = contentView.getText().toString();

        return new Note(noteId, System.currentTimeMillis(), content);
    }

    /**
     * Creates fragment to switch and saves bundle with argument to pass data to this fragment     *
     * @param bundle - bundle with saved argument
     * @return created fragment
     */
    private Fragment getFragment(Bundle bundle) {
        Fragment fragment = new EditNoteFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Inits delete button, which should delete note from database and switch fragment to list fragment
     * @param view - current view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDeleteButton(View view) {
        Button button = view.findViewById(R.id.delete_button);
        button.setOnClickListener(v -> {
            repository.delete(getNoteId());

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_list_fragment, new NoteListFragment())
                    .commit();
        });
    }
}