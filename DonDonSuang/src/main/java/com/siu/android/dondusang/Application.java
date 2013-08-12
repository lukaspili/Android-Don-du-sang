package com.siu.android.dondusang;

import android.R;
import android.content.Context;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import com.siu.android.dondusang.dao.DatabaseHelper;

import java.util.Locale;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class Application extends android.app.Application {

    private static Context sContext;

    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

//        // debug
//        if (context.getResources().getBoolean(R.bool.application_debug)) {
//            DatabaseHelper.getInstance().getDaoSession().getCenterDao().deleteAll();
//            DatabaseHelper.getInstance().getDaoSession().getNewsDao().deleteAll();
//            PreferenceManager.getDefaultSharedPreferences(context).edit().remove(context.getString(R.string.application_preferences_centers_md5)).commit();
//            PreferenceManager.getDefaultSharedPreferences(context).edit().remove(context.getString(R.string.application_preferences_news_md5)).commit();
//        }
    }

    public static Context getContext() {
        return sContext;
    }
}