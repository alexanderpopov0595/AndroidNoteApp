package com.fancysoft.androidnoteapp.db.properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Properties;

@RunWith(MockitoJUnitRunner.class)
public class DataBasePropertiesTest {

    @Mock
    private Properties properties;

    @Test
    public void shouldWriteAllPropertiesToFields() {
        when(properties.getProperty("database_name")).thenReturn("dbName");
        when(properties.getProperty("database_schema")).thenReturn("1");
        when(properties.getProperty("database_table")).thenReturn("dbTable");
        when(properties.getProperty("database_id_column")).thenReturn("dbIdColumnName");
        when(properties.getProperty("database_last_update_column")).thenReturn("dbLastUpdateColumnName");
        when(properties.getProperty("database_content_column")).thenReturn("dbContentColumnName");

        DataBaseProperties actual = new DataBaseProperties(properties);

        assertEquals("dbName", actual.getName());
        assertEquals(1, actual.getSchema());
        assertEquals("dbTable", actual.getTable());
        assertEquals("dbIdColumnName", actual.getIdColumn());
        assertEquals("dbLastUpdateColumnName", actual.getLastUpdateColumn());
        assertEquals("dbContentColumnName", actual.getContentColumn());
    }
}
