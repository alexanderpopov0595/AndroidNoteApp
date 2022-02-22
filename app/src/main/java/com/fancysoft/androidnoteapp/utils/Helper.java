package com.fancysoft.androidnoteapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fancysoft.androidnoteapp.exception.model.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    /**
     * Read properties from file
     * @param context - current app context
     * @return properties
     */
    public static Properties getProperties(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try(InputStream is = assetManager.open("app.properties")) {
            properties.load(is);
            return properties;
        }
        catch (IOException e) {
            throw new AppException(String.format("Unable to read property files: %s", e.getMessage()));
        }
    }
}
