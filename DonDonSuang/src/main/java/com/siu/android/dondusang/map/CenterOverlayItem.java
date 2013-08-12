//package com.siu.android.dondusang.map;
//
//import com.google.android.maps.OverlayItem;
//import com.siu.android.andgapisutils.util.LocationUtils;
//import com.siu.android.andgapisutils.util.MarkerUtils;
//import com.siu.android.dondusang.Application;
//import com.siu.android.dondusang.dao.model.Center;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class CenterOverlayItem extends OverlayItem {
//
//    private Center center;
//
//    public CenterOverlayItem(Center center) {
//        super(LocationUtils.getGeoPoint(center.getLatitude(), center.getLongitude()), center.getTitle(), center.getSubtitle());
//        this.center = center;
//        setMarker(MarkerUtils.boundBottomLeft(Application.getContext().getResources().getDrawable(center.getMarkerId())));
//    }
//
//    public Center getCenter() {
//        return center;
//    }
//}
