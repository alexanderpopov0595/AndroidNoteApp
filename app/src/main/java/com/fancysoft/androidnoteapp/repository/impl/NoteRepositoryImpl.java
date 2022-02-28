package com.fancysoft.androidnoteapp.repository.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.exception.model.AppException;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.repository.NoteRepository;
import com.fancysoft.androidnoteapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor
public class NoteRepositoryImpl implements NoteRepository {

    /**
     * Used to simplify work with database
     */
    private final DataBaseHelper dbHelper;
    /**
     * Holds database properties
     */
    private final DataBaseProperties dbProperties;

    @Override
    public long save(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(dbProperties.getLastUpdateColumn(), note.getLastUpdate());
        cv.put(dbProperties.getContentColumn(), note.getContent());

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            return db.insert(dbProperties.getTable(), null, cv);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Note get(long id) {
        String query = String.format(Constants.SQL_SELECT_BY_ID_QUERY, dbProperties.getTable(), dbProperties.getIdColumn());

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor dbCursor = db.rawQuery(query, new String[]{ String.valueOf(id)})) {

            if(dbCursor.moveToFirst()){
                return cursorToNote(dbCursor);
            }
        }
        throw new AppException(String.format("Note with id %d is not found!", id));
    }

    /**
     * Converts cursor to note
     * @param dbCursor - cursor with data
     * @return note
     */
    private Note cursorToNote(Cursor dbCursor) {
        int noteIdColumnIndex = dbCursor.getColumnIndex(dbProperties.getIdColumn());
        int noteLastUpdateColumnIndex = dbCursor.getColumnIndex(dbProperties.getLastUpdateColumn());
        int noteContentColumnIndex = dbCursor.getColumnIndex(dbProperties.getContentColumn());

        long noteId = dbCursor.getLong(noteIdColumnIndex);
        long noteLastUpdate = dbCursor.getLong(noteLastUpdateColumnIndex);
        String noteContent = dbCursor.getString(noteContentColumnIndex);

        return new Note(noteId, noteLastUpdate, noteContent);
    }

    @Override
    public List<Note> getAll() {
        String[] columns = new String[] { dbProperties.getIdColumn(), dbProperties.getLastUpdateColumn(), dbProperties.getContentColumn() };

        List<Note> notes = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor dbCursor = db.query(dbProperties.getTable(), columns, null, null, null, null, null)) {

             while(dbCursor.moveToNext()) {
                Note note = cursorToNote(dbCursor);
                notes.add(note);
             }
        }

        return notes;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Note> filter(String filter) {
        List<Note> notes = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor dbCursor = db.rawQuery(String.format(Constants.SQL_SELECT_BY_CONTENT_QUERY, dbProperties.getTable(), dbProperties.getContentColumn()), new String[]{ "%"+ filter + "%" })) {

             while(dbCursor.moveToNext()) {
                Note note = cursorToNote(dbCursor);
                notes.add(note);
             }
        }

        return notes;
    }

    @Override
    public long update(Note note) {
        String whereClause = dbProperties.getIdColumn() + "= ?";

        ContentValues cv = new ContentValues();

        cv.put(dbProperties.getContentColumn(), note.getContent());
        cv.put(dbProperties.getLastUpdateColumn(), note.getLastUpdate());

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            return db.update(dbProperties.getTable(), cv, whereClause, new String[] { String.valueOf(note.getId()) });
        }
    }

    @Override
    public void delete(long id) {
        String whereClause = dbProperties.getIdColumn() + "= ?";

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete(dbProperties.getTable(), whereClause, new String[] { String.valueOf(id) });
        }
    }
}
