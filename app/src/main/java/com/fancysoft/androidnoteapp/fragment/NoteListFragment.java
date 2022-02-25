package com.fancysoft.androidnoteapp.fragment;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.adapter.NoteAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.utils.Constants;
import com.fancysoft.androidnoteapp.utils.Helper;

/**
 * Represents app fragment containing list of notes
 */
public class NoteListFragment extends Fragment {

    private Cursor dbCursor;
    private DataBaseAdapter dbAdapter;

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

        dbAdapter = new DataBaseAdapter(dbHelper ,dbProperties);
        dbAdapter.open();
        dbCursor = dbAdapter.getAll();

        NoteAdapter adapter = new NoteAdapter(this.getContext().getApplicationContext(), dbCursor, dbProperties);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint == null || constraint.length() == 0) {
                    return dbAdapter.getAll();
                }
                return dbAdapter.getByContent(constraint.toString());
            }
        });

        ListView noteList = view.findViewById(R.id.note_list);
        noteList.setAdapter(adapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                long noteId = 0;

                try(Cursor cursor = (Cursor) parent.getItemAtPosition(position)) {
                        int idColumnIndex = cursor.getColumnIndex(dbProperties.getIdColumn());

                        noteId= cursor.getLong(idColumnIndex);
                }

                Bundle bundle = new Bundle();
                bundle.putLong(Constants.NOTE_ID_KEY, noteId);

                Fragment fragment = new EditNoteFragment();
                fragment.setArguments(bundle);

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.note_list_fragment, fragment)
                        .commit();
            }
        });

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

    /**
     * Closes db connection and cursor
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
        dbCursor.close();
    }
}