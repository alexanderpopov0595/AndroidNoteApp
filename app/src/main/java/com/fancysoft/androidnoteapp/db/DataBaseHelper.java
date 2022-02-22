package com.fancysoft.androidnoteapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fancysoft.androidnoteapp.db.properties.DataBaseProperties;
import com.fancysoft.androidnoteapp.utils.Constants;

/**
 * Used to simplify work with SQLite
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    /**
     * Contains required database properties
     */
    private final DataBaseProperties dbProperties;

    /**
     * Database helper instance
     */
    private static DataBaseHelper dbHelper;

    private DataBaseHelper(Context context, DataBaseProperties dbProperties) {
        super(context, dbProperties.getName(), null, dbProperties.getSchema());
        this.dbProperties = dbProperties;
    }

    /**
     * Returns instance of database helper
     * @param context - application context
     * @return database helper
     */
    public static DataBaseHelper getInstance(Context context) {
        if (dbHelper == null) {
            DataBaseProperties dbProperties = DataBaseProperties.getInstance(context.getApplicationContext());
            dbHelper = new DataBaseHelper(context.getApplicationContext(), dbProperties);
        }
        return dbHelper;
    }

    /**
     * Creates table in database if not exists
     * @param db - db reference
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(Constants.SQL_CREATE_QUERY,
                dbProperties.getTable(),
                dbProperties.getIdColumn(),
                dbProperties.getLastUpdateColumn(),
                dbProperties.getContentColumn()));
    }

    /**
     * Drops database if schema was updated
     * @param db - database reference
     * @param oldVersion - old database version
     * @param newVersion - new database version
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format(Constants.SQL_UPGRADE_QUERY, dbProperties.getTable()));
        onCreate(db);
    }
}
