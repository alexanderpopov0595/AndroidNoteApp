package com.fancysoft.androidnoteapp.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.adapter.NoteAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.repository.NoteRepository;
import com.fancysoft.androidnoteapp.repository.impl.NoteRepositoryImpl;
import com.fancysoft.androidnoteapp.utils.Constants;
import com.fancysoft.androidnoteapp.utils.Helper;

import java.util.List;
import java.util.Properties;

/**
 * Represents app fragment containing list of notes
 */
public class NoteListFragment extends Fragment {

    private NoteRepository repository;
    private NoteAdapter adapter;
    private DataBaseProperties dbProperties;

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

        initNoteRepository();
        initAdapter();
        initAddButton(view);
        initList(view);
        initSearch(view);

    }

    /**
     * Inits repository
     */
    private void initNoteRepository() {
        Context context = this.requireContext().getApplicationContext();

        Properties properties = Helper.getProperties(context);
        dbProperties = new DataBaseProperties(properties);

        DataBaseHelper dbHelper = new DataBaseHelper(context, dbProperties);

        repository = new NoteRepositoryImpl(dbHelper, dbProperties);
    }

    /**
     * Inits adapter
     */
    private void initAdapter() {
        MatrixCursor dbCursor = initCursor(repository.getAll());

        adapter = new NoteAdapter(this.getContext().getApplicationContext(), dbCursor, dbProperties);

        adapter.setFilterQueryProvider(constraint -> {
            if (constraint == null || constraint.length() == 0) {
                return dbCursor;
            }
            return initCursor(repository.filter(constraint.toString()));
        });
    }

    /**
     * Creates cursor from list of notes
     * @param notes - list of notes
     * @return cursor with data
     */
    private MatrixCursor initCursor(List<Note> notes) {
        MatrixCursor dbCursor = new MatrixCursor(new String[]{dbProperties.getIdColumn(), dbProperties.getLastUpdateColumn(), dbProperties.getContentColumn()});

        for (Note note : notes) {
            dbCursor.newRow()
                    .add(dbProperties.getIdColumn(), note.getId())
                    .add(dbProperties.getLastUpdateColumn(), note.getLastUpdate())
                    .add(dbProperties.getContentColumn(), note.getContent());
        }

        return dbCursor;
    }

    /**
     * Inits "add button", which should switch fragment to add fragment     *
     * @param view - current view
     */
    private void initAddButton(View view) {
        Button button = view.findViewById(R.id.add_button);

        button.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.note_list_fragment, new AddNoteFragment())
                .commit());
    }

    /**
     * Inits list view of notes, each element of which should switch fragment on edit fragment on click
     * @param view - current view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initList(View view) {
        ListView noteList = view.findViewById(R.id.note_list);
        noteList.setAdapter(adapter);
        noteList.setOnItemClickListener((parent, v, position, id) -> {
            long noteId = 0;

            try (Cursor cursor = (Cursor) parent.getItemAtPosition(position)) {
                int idColumnIndex = cursor.getColumnIndex(dbProperties.getIdColumn());

                noteId = cursor.getLong(idColumnIndex);
            }

            Bundle bundle = getBundle(noteId);

            Fragment fragment = getFragment(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_list_fragment, fragment)
                    .commit();
        });
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

    /**
     * Inits search view, which should filter results
     * @param view - current view
     */
    private void initSearch(View view) {
        SearchView search = view.findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }
}