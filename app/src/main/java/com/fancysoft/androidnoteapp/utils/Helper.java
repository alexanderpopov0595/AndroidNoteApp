package com.fancysoft.androidnoteapp.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import lombok.experimental.UtilityClass;

/**
 * Provides help methods
 */
@UtilityClass
public class Helper {

    /**
     * Converts millis to readable format
     * @param millis - date in millis
     * @return formatted date string
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String millisToString(long millis) {
        return Constants.DATE_FORMAT.format(millis);
    }
}
