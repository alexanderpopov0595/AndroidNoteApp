package com.fancysoft.androidnoteapp.adapter;

import static org.mockito.Mockito.verify;

import android.view.View;

import com.fancysoft.androidnoteapp.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ViewHolderTest {

    @Mock
    private View view;

    @Test
    public void shouldSetAllFields() {
        new ViewHolder(view);

        verify(view).findViewById(R.id.note_id);
        verify(view).findViewById(R.id.note_last_update);
        verify(view).findViewById(R.id.note_content);
    }
}
