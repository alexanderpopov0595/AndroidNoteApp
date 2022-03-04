package com.fancysoft.androidnoteapp.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.repository.NoteRepository;
import com.fancysoft.androidnoteapp.repository.impl.NoteRepositoryImpl;
import com.fancysoft.androidnoteapp.utils.Constants;
import com.fancysoft.androidnoteapp.utils.Helper;

import java.util.Properties;

/**
 * Represents app fragment to create new note
 */
public class AddNoteFragment extends Fragment {

    /**
     * Used to work with database
     */
    private NoteRepository repository;

    /**
     * Passes add note fragment layout to super constructor
     */
    public AddNoteFragment() {
        super(R.layout.fragment_add_note);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initNoteRepository();

        initCancelButton(view);

        initSaveButton(view);
    }

    /**
     * Inits repository
     */
    private void initNoteRepository() {
        Context context = this.requireContext().getApplicationContext();

        Properties properties = Helper.getProperties(context);
        DataBaseProperties dbProperties = new DataBaseProperties(properties);

        DataBaseHelper dbHelper = new DataBaseHelper(context, dbProperties);

        repository = new NoteRepositoryImpl(dbHelper, dbProperties);
    }

    /**
     * Inits "cancel" button, which should switch back to the list fragment
     *
     * @param view - current layout
     */
    private void initCancelButton(View view) {
        ImageButton button = view.findViewById(R.id.cancel_button);
        button.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.note_list_fragment, new NoteListFragment())
                .commit());
    }

    /**
     * Inits "save" button, which should save note and switch to edit fragment
     *
     * @param view - current layout
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initSaveButton(View view) {
        ImageButton button = view.findViewById(R.id.save_button);
        button.setOnClickListener(v -> {
            Note note = viewToNote(view);

            long noteId = repository.save(note);

            Bundle bundle = getBundle(noteId);

            Fragment fragment = getFragment(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_list_fragment, fragment)
                    .commit();
        });
    }

    /**
     * Converts view data to note
     *
     * @param view - current view
     * @return created note
     */
    private Note viewToNote(View view) {
        EditText inputField = view.findViewById(R.id.input_field);
        String text = inputField.getText().toString();
        return new Note(System.currentTimeMillis(), text);
    }

    /**
     * Creates bundle and saves note id to it
     *
     * @param noteId - id of created note
     * @return created bundle
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bundle getBundle(long noteId) {
        Bundle bundle = new Bundle();

        bundle.putLong(Constants.NOTE_ID_KEY, noteId);

        return bundle;
    }

    /**
     * Creates fragment to switch and saves bundle with argument to pass data to this fragment
     *
     * @param bundle - bundle with saved argument
     * @return created fragment
     */
    private Fragment getFragment(Bundle bundle) {
        Fragment fragment = new EditNoteFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

}