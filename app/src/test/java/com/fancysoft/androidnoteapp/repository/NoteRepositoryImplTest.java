package com.fancysoft.androidnoteapp.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fancysoft.androidnoteapp.db.DataBaseHelper;
import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.exception.model.AppException;
import com.fancysoft.androidnoteapp.model.Note;
import com.fancysoft.androidnoteapp.repository.impl.NoteRepositoryImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NoteRepositoryImplTest {

    @Mock
    private DataBaseProperties dbProperties;
    @Mock
    private DataBaseHelper dbHelper;
    @Mock
    private SQLiteDatabase db;
    @Mock
    private Cursor dbCursor;
    @InjectMocks
    private NoteRepositoryImpl repository;

    @Before
    public void init() {
        when(dbProperties.getTable()).thenReturn("notes");
        when(dbProperties.getIdColumn()).thenReturn("_id");
        when(dbProperties.getLastUpdateColumn()).thenReturn("last_update");
        when(dbProperties.getContentColumn()).thenReturn("content");
    }

    @Test
    public void shouldSaveNote() {
        when(dbHelper.getWritableDatabase()).thenReturn(db);
        when(db.insert(anyString(), nullable(String.class), nullable(ContentValues.class))).thenReturn(1L);

        long actual = repository.save(new Note(System.currentTimeMillis(), "Some text"));

        verify(db).insert(anyString(), nullable(String.class), nullable(ContentValues.class));
        verify(db).close();

        assertEquals(1L, actual);
    }

    @Test
    public void shouldGetNote() {
        Note expected = new Note(1L, System.currentTimeMillis(), "Some note");

        when(dbHelper.getReadableDatabase()).thenReturn(db);
        when(db.rawQuery(anyString(), any())).thenReturn(dbCursor);

        when(dbCursor.moveToFirst()).thenReturn(true);

        when(dbCursor.getColumnIndex(dbProperties.getIdColumn())).thenReturn(0);
        when(dbCursor.getColumnIndex(dbProperties.getLastUpdateColumn())).thenReturn(1);
        when(dbCursor.getColumnIndex(dbProperties.getContentColumn())).thenReturn(2);

        when(dbCursor.getLong(0)).thenReturn(expected.getId());
        when(dbCursor.getLong(1)).thenReturn(expected.getLastUpdate());
        when(dbCursor.getString(2)).thenReturn(expected.getContent());

        Note actual = repository.get(1L);

        verify(db).rawQuery(anyString(), any());
        verify(dbCursor).moveToFirst();
        verify(dbCursor, times(3)).getColumnIndex(nullable(String.class));
        verify(dbCursor, times(2)).getLong(anyInt());
        verify(dbCursor).getString(anyInt());
        verify(dbCursor).close();
        verify(db).close();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenNoteIsNotFound() {
        String expected = "Note with id 1 is not found!";

        when(dbHelper.getReadableDatabase()).thenReturn(db);
        when(db.rawQuery(anyString(), any())).thenReturn(dbCursor);

        when(dbCursor.moveToFirst()).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> repository.get(1L));

        verify(db).rawQuery(anyString(), any());
        verify(dbCursor).moveToFirst();
        verify(dbCursor).close();
        verify(db).close();

        String actual = exception.getMessage();

        assertEquals(actual, expected);
    }

    @Test
    public void shouldReturnAllNotes() {
        List<Note> expected = List.of(
                new Note(1L, System.currentTimeMillis(), "First text"),
                new Note(2L, System.currentTimeMillis(), "Second text"));

        when(dbHelper.getReadableDatabase()).thenReturn(db);
        when(db.query(anyString(), any(String[].class), nullable(String.class), nullable(String[].class), nullable(String.class), nullable(String.class), nullable(String.class))).thenReturn(dbCursor);

        when(dbCursor.moveToNext()).thenReturn(true, true, false);

        when(dbCursor.getColumnIndex(dbProperties.getIdColumn())).thenReturn(0);
        when(dbCursor.getColumnIndex(dbProperties.getLastUpdateColumn())).thenReturn(1);
        when(dbCursor.getColumnIndex(dbProperties.getContentColumn())).thenReturn(2);

        when(dbCursor.getLong(0)).thenReturn(expected.get(0).getId(), expected.get(1).getId());
        when(dbCursor.getLong(1)).thenReturn(expected.get(0).getLastUpdate(), expected.get(1).getLastUpdate());
        when(dbCursor.getString(2)).thenReturn(expected.get(0).getContent(), expected.get(1).getContent());

        List<Note> actual = repository.getAll();

        verify(db).query(anyString(), any(String[].class), nullable(String.class), nullable(String[].class), nullable(String.class), nullable(String.class), nullable(String.class));
        verify(dbCursor, times(3)).moveToNext();
        verify(dbCursor, times(6)).getColumnIndex(nullable(String.class));
        verify(dbCursor, times(4)).getLong(anyInt());
        verify(dbCursor, times(2)).getString(anyInt());
        verify(dbCursor).close();
        verify(db).close();

        assertEquals(actual, expected);
    }

    @Test
    public void shouldUpdateNote() {
        when(dbHelper.getWritableDatabase()).thenReturn(db);
        when(db.update(anyString(), nullable(ContentValues.class), anyString(), any(String[].class))).thenReturn(1);

        long actual = repository.update(new Note(1L, System.currentTimeMillis(), "Some text"));

        verify(db).update(anyString(), nullable(ContentValues.class), anyString(), any(String[].class));
        verify(db).close();

        assertEquals(1L, actual);
    }

    @Test
    public void shouldDeleteNote() {
        when(dbHelper.getWritableDatabase()).thenReturn(db);

        repository.delete(1L);

        verify(db).delete(anyString(), anyString(), any(String[].class));
        verify(db).close();
    }
}
