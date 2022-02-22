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

    public DataBaseHelper(Context context, DataBaseProperties dbProperties) {
        super(context, dbProperties.getName(), null, dbProperties.getSchema());
        this.dbProperties = dbProperties;
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
