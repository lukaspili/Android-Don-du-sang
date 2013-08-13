package com.siu.android.dondusang.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.fragment.CentersFragment;
import com.siu.android.dondusang.model.Center;

/**
 * Created by lukas on 7/17/13.
 */
public class CenterInfoWindowAdatper implements GoogleMap.InfoWindowAdapter {

    private CentersFragment mCentersFragment;

    public CenterInfoWindowAdatper(CentersFragment centersFragment) {
        mCentersFragment = centersFragment;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(mCentersFragment.getActivity()).inflate(R.layout.center_info_window, null);
        TextView title = (TextView) view.findViewById(R.id.center_info_window_title_text);

        Center center = mCentersFragment.getMarkerCenters().get(marker);
        title.setText(center.getTitle());

        return view;
    }
}
