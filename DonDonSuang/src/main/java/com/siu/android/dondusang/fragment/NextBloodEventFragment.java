package com.siu.android.dondusang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.activity.NextBloodEventActivity;

/**
 * Created by lukas on 8/12/13.
 */
public class NextBloodEventFragment extends SherlockFragment {

    private Button mStartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.next_blood_event_fragment, null);
        mStartButton = (Button) view.findViewById(R.id.next_blood_event_start_button);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NextBloodEventActivity.class));
            }
        });
    }
}