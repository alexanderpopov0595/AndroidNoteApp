package com.fancysoft.androidnoteapp.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;

import lombok.experimental.UtilityClass;

/**
 * Contains application constants
 */
@RequiresApi(api = Build.VERSION_CODES.O)
@UtilityClass
public class Constants {

    /**
     * Format for note's creation/update date
     */
    public final static DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance();

    /**
     * SQL queries
     */
    public final static String SQL_CREATE_QUERY = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s DATETIME DEFAULT CURRENT_TIMESTAMP, %s TEXT)";
    public final static String SQL_UPGRADE_QUERY = "DROP TABLE IF EXISTS %s";


}
