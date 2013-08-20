package com.siu.android.dondusang.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.siu.android.dondusang.R;

/**
 * Created by lukas on 8/20/13.
 */
public class NextBloodEventActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_blood_event_activity);

        getSupportActionBar().setTitle(R.string.next_blood_event_activity_title);
    }
}
