package com.siu.android.dondusang.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.fragment.CentersFragment;
import com.siu.android.dondusang.fragment.FragmentTabListener;

/**
 * Created by lukas on 8/11/13.
 */
public class BloodTabsActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_tabs_activity);

        initActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        EasyTracker.getInstance().activityStop(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }


    /* Action bar */

    private void initActionBar() {
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        addTab("Centres", 0, CentersFragment.class);
    }

    private void addTab(String title, int icon, Class clazz) {
        ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setText(title);
        tab.setIcon(getResources().getDrawable(icon));
        tab.setTabListener(new FragmentTabListener<SherlockFragment>(this, title + " fragment", clazz));

        getSupportActionBar().addTab(tab);
    }
}
