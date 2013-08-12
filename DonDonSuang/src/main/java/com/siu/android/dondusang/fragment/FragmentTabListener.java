package com.siu.android.dondusang.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;

/**
 * Created by lukas on 8/12/13.
 */
public class FragmentTabListener<T extends Fragment> implements ActionBar.TabListener {

    private final Activity activity;
    private final String tag;
    private final Class<T> clazz;

    private Fragment fragment;

    public FragmentTabListener(FragmentActivity activity, String tag, Class<T> clazz) {
        this.activity = activity;
        this.tag = tag;
        this.clazz = clazz;

        fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (null == fragment) {
            fragment = Fragment.instantiate(activity, clazz.getName());
            ft.add(android.R.id.content, fragment, tag);
        } else {
            ft.attach(fragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (null != fragment) {
            ft.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
