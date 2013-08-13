package com.siu.android.dondusang;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.siu.android.dondusang.volley.OkHttpStack;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class Application extends android.app.Application {

    private static Context sContext;
    private static RequestQueue sRequestQueue;

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

    public static synchronized RequestQueue getRequestQueue() {
        if (sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(sContext, new OkHttpStack());
        }

        return sRequestQueue;
    }
}