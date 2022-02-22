package com.fancysoft.androidnoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fancysoft.androidnoteapp.fragment.AddNoteFragment;

/**
 * Represents main app's activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Switches to add note activity
     *
     * @param view - add button
     */
    public void onAdd(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.note_list_fragment, new AddNoteFragment())
                .commit();
    }
}