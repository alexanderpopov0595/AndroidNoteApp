package com.fancysoft.androidnoteapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.utils.Constants;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Note get(long id) {
        Note note = null;

        String query = String.format(Constants.SQL_SELECT_BY_ID_QUERY, dbProperties.getTable(), dbProperties.getIdColumn());

        try(Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(id)})) {
            if(cursor.moveToFirst()){

                int noteIdColumnIndex = cursor.getColumnIndex(dbProperties.getIdColumn());
                int noteLastUpdateColumnIndex = cursor.getColumnIndex(dbProperties.getLastUpdateColumn());
                int noteContentColumnIndex = cursor.getColumnIndex(dbProperties.getContentColumn());

                long noteId =  cursor.getLong(noteIdColumnIndex);
                long noteLastUpdate = cursor.getLong(noteLastUpdateColumnIndex);
                String noteContent = cursor.getString(noteContentColumnIndex);

                note = new Note(noteId, noteLastUpdate, noteContent);
            }
        }
        return  note;
    }

    public long update(Note note) {
        String whereClause = dbProperties.getIdColumn() + "=" + note.getId();

        ContentValues values = new ContentValues();

        values.put(dbProperties.getContentColumn(), note.getContent());
        values.put(dbProperties.getLastUpdateColumn(), note.getLastUpdate());

        return db.update(dbProperties.getTable(), values, whereClause, null);
    }

    public void delete(long id) {
        String whereClause = dbProperties.getIdColumn() + "=" + id;

        db.delete(dbProperties.getTable(), whereClause, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Cursor getByContent(String content) {
        return db.rawQuery(String.format(Constants.SQL_SELECT_BY_CONTENT_QUERY, dbProperties.getTable(), dbProperties.getContentColumn()), new String[]{ "%"+ content + "%" });
    }
}
