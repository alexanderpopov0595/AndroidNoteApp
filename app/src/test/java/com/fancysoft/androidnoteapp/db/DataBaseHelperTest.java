package com.fancysoft.androidnoteapp.db;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.database.sqlite.SQLiteDatabase;

import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataBaseHelperTest {

    @Mock
    private DataBaseProperties dbProperties;
    @Mock
    private SQLiteDatabase db;
    @InjectMocks
    private DataBaseHelper dbHelper;

    @Test
    public void shouldCreate() {
        dbHelper.onCreate(db);

        verify(db).execSQL(anyString());

        verify(dbProperties).getTable();
        verify(dbProperties).getIdColumn();
        verify(dbProperties).getLastUpdateColumn();
        verify(dbProperties).getContentColumn();
    }

    @Test
    public void shouldUpgrade() {
        dbHelper.onUpgrade(db, 0, 0);

        verify(db, times(2)).execSQL(anyString());
        verify(dbProperties, times(2)).getTable();
        verify(dbProperties).getIdColumn();
        verify(dbProperties).getLastUpdateColumn();
        verify(dbProperties).getContentColumn();
    }
}
