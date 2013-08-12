package com.siu.android.dondusang.util;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.siu.android.dondusang.Application;

/**
 * Created by lukas on 7/4/13.
 */
public class AndroidUtils {

    public static final boolean isSupportAnimations() {
        return Build.VERSION.SDK_INT >= 12;
    }

    public static final void removeGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    public static final void commitPreferencesEditor(final SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    editor.commit();
                    return null;
                }
            }.execute();
        }
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static final int getAppVersionCode() {
        try {
            PackageInfo packageInfo = Application.getContext().getPackageManager().getPackageInfo(Application.getContext().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
