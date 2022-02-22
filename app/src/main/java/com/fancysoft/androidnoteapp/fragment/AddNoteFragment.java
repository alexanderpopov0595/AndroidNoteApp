package com.fancysoft.androidnoteapp.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fancysoft.androidnoteapp.R;
import com.fancysoft.androidnoteapp.db.DataBaseAdapter;
import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
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
        run();
    }

    private void run() {
        Properties properties = Helper.getProperties(this.getContext().getApplicationContext());
        DataBaseProperties dbProperties = new DataBaseProperties(properties);

        DataBaseHelper dbHelper = new DataBaseHelper(this.getContext().getApplicationContext(), dbProperties);
        dbAdapter = new DataBaseAdapter(dbHelper, dbProperties);
    }

    /**
     * Adds note to database and switches to edit note activity
     * @param view - edit text field
     */
    public void onSave(View view) {
        System.out.println("Placeholder for now");
    }
}