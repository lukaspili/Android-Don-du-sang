package com.siu.android.dondusang.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Created by lukas on 8/12/13.
 */
public class FragmentTabListener<T extends SherlockFragment> implements ActionBar.TabListener {

    private final SherlockFragmentActivity activity;
    private final String tag;
    private final Class<T> clazz;

    private Fragment fragment;

    public FragmentTabListener(SherlockFragmentActivity activity, String tag, Class<T> clazz) {
        this.activity = activity;
        this.tag = tag;
        this.clazz = clazz;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (fragment == null) {
            fragment = Fragment.instantiate(activity, clazz.getName());
            ft.add(android.R.id.content, fragment, tag);
        } else {
            ft.attach(fragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            ft.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
