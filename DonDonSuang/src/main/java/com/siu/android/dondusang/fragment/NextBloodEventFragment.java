package com.siu.android.dondusang.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.siu.android.dondusang.R;

/**
 * Created by lukas on 8/12/13.
 */
public class NextBloodEventFragment extends SherlockFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.next_blood_event_fragment, null);
        return view;
    }
}