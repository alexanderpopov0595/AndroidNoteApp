package com.fancysoft.androidnoteapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.model.Note;

import lombok.RequiredArgsConstructor;

/**
 * Provides methods to work with database
 */
@RequiredArgsConstructor
public class DataBaseAdapter {

    /**
     * Used to simplify work with database
     */
    private final DataBaseHelper dbHelper;
    /**
     * Holds database properties
     */
    private final DataBaseProperties dbProperties;

    private SQLiteDatabase db;

    public DataBaseAdapter open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }
    /**
     * Adds new note to database
     *
     * @param note - record to add
     * @return id of inserted row
     */
    public long add(Note note) {
        ContentValues values = new ContentValues();
        values.put(dbProperties.getLastUpdateColumn(), note.getLastUpdate());
        values.put(dbProperties.getContentColumn(), note.getContent());
        return db.insert(dbProperties.getTable(), null, values);
    }

    /**
     * Allows to get all notes
     *
     * @return cursor with all notes
     */
    public Cursor getAll() {
        String[] columns = new String[]{dbProperties.getIdColumn(), dbProperties.getLastUpdateColumn(), dbProperties.getContentColumn()};
        return db.query(dbProperties.getTable(), columns, null, null, null, null, null);
    }
}
