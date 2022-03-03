package com.fancysoft.androidnoteapp.adapter;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NoteAdapterTest {

    @Mock
    private View view;
    @Mock
    private Context context;
    @Mock
    private Cursor cursor;

    @Mock
    private ViewHolder holder;
    @Mock
    TextView idView, lastUpdateView, contentView;

    @Mock
    private DataBaseProperties dbProperties;
    @InjectMocks
    private NoteAdapter adapter;

    @Test
    public void shouldBindView() {
        when(view.getTag()).thenReturn(holder);

        when(holder.getNoteIdView()).thenReturn(idView);
        when(holder.getNoteLastUpdateView()).thenReturn(lastUpdateView);
        when(holder.getNoteContentView()).thenReturn(contentView);

        when(cursor.getLong(anyInt())).thenReturn(System.currentTimeMillis());

        adapter.bindView(view, context, cursor);

        verify(view).getTag();

        verify(dbProperties).getIdColumn();
        verify(dbProperties).getLastUpdateColumn();
        verify(dbProperties).getContentColumn();

        verify(cursor, times(3)).getColumnIndex(nullable(String.class));

        verify(cursor, times(2)).getLong(anyInt());
        verify(cursor).getString(anyInt());

        verify(holder).getNoteIdView();
        verify(holder).getNoteLastUpdateView();
        verify(holder).getNoteContentView();

        verify(idView).setText(nullable(String.class));
        verify(lastUpdateView).setText(nullable(String.class));
        verify(contentView).setText(nullable(String.class));


    }
}
