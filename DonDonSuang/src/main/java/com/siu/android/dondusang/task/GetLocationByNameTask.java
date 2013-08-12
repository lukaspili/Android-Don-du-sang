package com.siu.android.dondusang.task;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.siu.android.dondusang.AppConstants;
import com.siu.android.dondusang.Application;
import com.siu.android.dondusang.fragment.CentersFragment;

import java.util.List;

/**
 * Created by lukas on 8/12/13.
 */
public class GetLocationByNameTask extends SimpleTask<CentersFragment, Void, Void, LatLng> {

    private String mName;

    public GetLocationByNameTask(CentersFragment centersFragment, String name) {
        super(centersFragment);
        mName = name;
    }

    @Override
    protected LatLng doInBackground(Void... voids) {

        if (Build.VERSION.SDK_INT >= 9 && !Geocoder.isPresent()) {
            Log.w(getClass().getName(), "Geocoder is not available, exit");
            return null;
        }

        Geocoder geocoder = new Geocoder(Application.getContext());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(mName, 1, AppConstants.FRANCE_SW_LATITUDE, AppConstants.FRANCE_SW_LONGITUDE,
                    AppConstants.FRANCE_NE_LATITUDE, AppConstants.FRANCE_NE_LONGITUDE);
        } catch (Exception e) {
            Log.w(getClass().getName(), "Cannot get location addresses from geocoder", e);
            return null;
        }

        if (addresses == null || addresses.isEmpty()) {
            return null;
        }

        Address address = addresses.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    @Override
    public void onResult(LatLng latLng) {
        mOwner.onGetLocationByNameResult(latLng);
    }
}
