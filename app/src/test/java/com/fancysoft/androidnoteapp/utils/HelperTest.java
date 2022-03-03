package com.fancysoft.androidnoteapp.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.AssetManager;

import com.fancysoft.androidnoteapp.exception.model.AppException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class HelperTest {

    @Mock
    private Context context;
    @Mock
    private AssetManager assetManager;
    @Mock
    private InputStream is;

    @Test
    public void shouldConvertMillisToDate() {
        long millis = System.currentTimeMillis();

        String expected = DateFormat.getDateTimeInstance().format(new Date(millis));

        String actual = Helper.millisToString(millis);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReadProperties() throws IOException {
        when(context.getAssets()).thenReturn(assetManager);
        when(assetManager.open(anyString())).thenReturn(is);

        Helper.getProperties(context);

        verify(context).getAssets();
        verify(assetManager).open(anyString());
        verify(is).close();
    }

    @Test
    public void shouldThrowExceptionWhenCantReadProperties() throws IOException {
        when(context.getAssets()).thenReturn(assetManager);
        when(assetManager.open(anyString())).thenThrow(new IOException("Some message"));

        AppException actual = assertThrows(AppException.class, () -> Helper.getProperties(context));

        assertEquals("Unable to read property files: Some message", actual.getMessage());

        verify(context).getAssets();
        verify(assetManager).open(anyString());
    }
}
