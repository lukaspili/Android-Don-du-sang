package com.siu.android.dondusang.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.siu.android.dondusang.AppConstants;
import com.siu.android.dondusang.model.User;
import com.siu.android.dondusang.util.AndroidUtils;

/**
 * Created by lukas on 8/12/13.
 */
public class UserService {

    private SharedPreferences mSharedPreferences;

    public UserService(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public void save(User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit()
                .putString(AppConstants.USER_SEXE_PREFKEY, user.getSexe().toString())
                .putString(AppConstants.USER_AGE_PREFKEY, user.getAge().toString());

        AndroidUtils.commitPreferencesEditor(editor);
    }

    public User get() {
        if (mSharedPreferences.contains(AppConstants.USER_AGE_PREFKEY)) {
            try {
                User.Sexe sexe = User.Sexe.valueOf(mSharedPreferences.getString(AppConstants.USER_SEXE_PREFKEY, null));
                User.Age age = User.Age.valueOf(mSharedPreferences.getString(AppConstants.USER_AGE_PREFKEY, null));
                return new User(sexe, age);
            } catch (Exception e) {
                Log.e(getClass().getName(), "Cant create user from pref", e);
            }
        }

        return new User();
    }

    public boolean isDefined() {
        return mSharedPreferences.contains(AppConstants.USER_AGE_PREFKEY);
    }
}
