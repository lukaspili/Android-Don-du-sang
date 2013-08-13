package com.siu.android.dondusang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.siu.android.dondusang.R;
import com.siu.android.dondusang.activity.fragments.NewsTabFragment;
import com.siu.android.dondusang.fragment.AptitudeFragment;
import com.siu.android.dondusang.fragment.FragmentTabListener;

/**
 * @author Lukasz Piliszczuk <lukasz.piliszczuk AT zenika.com>
 */
public class InfosActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        setContentView(R.layout.infos_activity);

        initActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_about:
//                FragmentUtils.showDialog(getSupportFragmentManager(), new AboutDialogFragment());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void initActionBar() {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ActionBar.Tab tab = actionBar.newTab();
        tab.setText("Aptitude");
        tab.setTabListener(new FragmentTabListener<AptitudeFragment>(this, "QuestionsTabFragment", AptitudeFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText("Actualités");
        tab.setTabListener(new FragmentTabListener<NewsTabFragment>(this, "NewsTabFragment", NewsTabFragment.class));
        actionBar.addTab(tab);
    }

    private static class TabListener<T extends Fragment> implements ActionBar.TabListener {

        private final Activity activity;
        private final String tag;
        private final Class<T> clazz;

        private Fragment fragment;

        public TabListener(FragmentActivity activity, String tag, Class<T> clazz) {
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
}