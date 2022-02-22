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
}
