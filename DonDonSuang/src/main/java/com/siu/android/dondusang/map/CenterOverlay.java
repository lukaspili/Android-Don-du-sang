//package com.siu.android.dondusang.map;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import com.google.android.maps.MapView;
//import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
//import com.readystatesoftware.mapviewballoons.BalloonOverlayView;
//import com.siu.android.andgapisutils.util.MarkerUtils;
//import com.siu.android.dondusang.R;
//import com.siu.android.dondusang.activity.CenterDetailActivity;
//import com.siu.android.dondusang.dao.model.Center;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class CenterOverlay extends BalloonItemizedOverlay<CenterOverlayItem> {
//
//    private List<CenterOverlayItem> overlayItems = new ArrayList<CenterOverlayItem>();
//
//    public CenterOverlay(Drawable defaultMarker, MapView mapView) {
//        super(MarkerUtils.boundBottomLeft(defaultMarker), mapView);
//
//        // bug fix, see this : http://stackoverflow.com/questions/3755921/problem-with-crash-with-itemizedoverlay
//        populate();
//    }
//
//    @Override
//    protected CenterOverlayItem createItem(int i) {
//        return overlayItems.get(i);
//    }
//
//    @Override
//    public int size() {
//        return overlayItems.size();
//    }
//
//    @Override
//    protected BalloonOverlayView<CenterOverlayItem> createBalloonOverlayView() {
//        return new BalloonOverlayView<CenterOverlayItem>(getMapView().getContext(), getBalloonBottomOffset()) {
//            @Override
//            protected void setupView(Context context, ViewGroup parent) {
//                LayoutInflater inflater = (LayoutInflater) context
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View v = inflater.inflate(R.layout.center_overlay_balloon, parent);
//                title = (TextView) v.findViewById(R.id.balloon_item_title);
//                snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
//            }
//        };
//    }
//
//    @Override
//    protected boolean onBalloonTap(int index, CenterOverlayItem overlayItem) {
//
//        Intent intent = new Intent(getMapView().getContext(), CenterDetailActivity.class);
//        intent.putExtra(CenterDetailActivity.EXTRA_CENTER, ((CenterOverlayItem) overlayItem).getCenter());
//
//        getMapView().getContext().startActivity(intent);
//
//        return true;
//    }
//
//    public void addOverlayItem(CenterOverlayItem overlayItem) {
//        overlayItems.add(overlayItem);
//        populate();
//    }
//
//    public void addCenters(List<Center> centers) {
//        for (Iterator<Center> it = centers.iterator(); it.hasNext(); ) {
//            overlayItems.add(new CenterOverlayItem(it.next()));
//        }
//
//        populate();
//    }
//
//    public List<CenterOverlayItem> getOverlayItems() {
//        return overlayItems;
//    }
//}
