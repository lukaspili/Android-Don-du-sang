package com.siu.android.dondusang.util;

import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lukas on 8/12/13.
 */
public class DateUtils {

    private static SimpleDateFormat fullDateFormat;

    public static String formatAsFull(Date date) {
        if (null == date) {
            return null;
        }

        return WordUtils.capitalize(getFullDateFormat().format(date));
    }

    private static SimpleDateFormat getFullDateFormat() {
        if (null == fullDateFormat) {
            fullDateFormat = new SimpleDateFormat("EEEEEEEE dd MMMMMM", Locale.getDefault());
        }

        return fullDateFormat;
    }
}
