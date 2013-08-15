package com.siu.android.dondusang.gson;

import com.google.gson.JsonElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lukas on 8/12/13.
 */
public class CommonDeserializer {

    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static Long getLong(JsonElement jsonElement) {
        if (null == jsonElement) {
            return null;
        }

        try {
            return jsonElement.getAsLong();
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getInteger(JsonElement jsonElement) {
        if (null == jsonElement) {
            return null;
        }

        try {
            return jsonElement.getAsInt();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(JsonElement jsonElement) {
        if (null == jsonElement) {
            return null;
        }

        try {
            String value = jsonElement.getAsString();
            if (value == null) {
                return null;
            }
            return value.trim();
        } catch (Exception e) {
            return null;
        }
    }

    public static Double getDouble(JsonElement jsonElement) {
        if (null == jsonElement) {
            return null;
        }

        try {
            return jsonElement.getAsDouble();
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean getBoolean(JsonElement jsonElement) {
        if (null == jsonElement) {
            return null;
        }

        try {
            return jsonElement.getAsBoolean();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDate(JsonElement jsonElement) {
        if (null == jsonElement) {
            return null;
        }

        try {
            return dateFormat.parse(jsonElement.getAsString());
        } catch (Exception e) {
            return null;
        }
    }
}
