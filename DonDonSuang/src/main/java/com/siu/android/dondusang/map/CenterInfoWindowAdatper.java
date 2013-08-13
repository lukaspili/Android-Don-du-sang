package com.siu.android.dondusang.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.siu.android.dondusang.R;

/**
 * Created by lukas on 7/17/13.
 */
public class CenterInfoWindowAdatper implements GoogleMap.InfoWindowAdapter {

    private Context mContext = null;

    public CenterInfoWindowAdatper(Context context) {
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.center_info_window, null);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
