package com.fancysoft.androidnoteapp.db.properties;

import android.content.Context;

import com.fancysoft.androidnoteapp.utils.Helper;

import java.util.Properties;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Provides properties for database
 */
@Getter
public class DataBaseProperties {

    /**
     * Database name
     */
    private final String name;
    /**
     * Database version
     */
    private final int schema;
    /**
     * Database table's name
     */
    public final String table;
    /**
     * Columns names
     */
    private final String idColumn;
    private final String lastUpdateColumn;
    private final String contentColumn;

    /**
     * Database properties instance
     */
    @Getter(AccessLevel.NONE)
    private static DataBaseProperties dbProperties;

    private DataBaseProperties(Properties properties) {
        name = properties.getProperty("database_name");
        schema = Integer.parseInt(properties.getProperty("database_schema"));
        table = properties.getProperty("database_table");
        idColumn = properties.getProperty("database_id_column");
        lastUpdateColumn = properties.getProperty("database_last_update_column");
        contentColumn = properties.getProperty("database_content_column");
    }

    /**
     * Returns instance of database properties
     * @param context - application context
     * @return database properties with set data
     */
    public static DataBaseProperties getInstance(Context context) {
        if (dbProperties == null) {
            Properties properties = Helper.getProperties(context.getApplicationContext());
            dbProperties = new DataBaseProperties(properties);
        }
        return dbProperties;
    }

}
