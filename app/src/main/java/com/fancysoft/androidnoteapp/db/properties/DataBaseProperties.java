package com.fancysoft.androidnoteapp.db.properties;

import java.util.Properties;

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

    public DataBaseProperties(Properties properties) {
        name = properties.getProperty("database_name");
        schema = Integer.parseInt(properties.getProperty("database_schema"));
        table = properties.getProperty("database_table");
        idColumn = properties.getProperty("database_id_column");
        lastUpdateColumn = properties.getProperty("database_last_update_column");
        contentColumn = properties.getProperty("database_content_column");
    }
}
