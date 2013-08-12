//package com.siu.android.dondusang.activity;
//
//import android.app.SearchManager;
//import android.content.Intent;
//import android.location.Location;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.SherlockActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;
//import com.google.android.apps.analytics.easytracking.EasyTracker;
//import com.google.android.maps.GeoPoint;
//import com.siu.android.andgapisutils.util.LocationUtils;
//import com.siu.android.dondusang.R;
//import com.siu.android.dondusang.dao.model.Center;
//import com.siu.android.dondusang.list.CenterAdapter;
//import com.siu.android.dondusang.map.CenterOverlay;
//import com.siu.android.dondusang.map.EnhancedMapView;
//import com.siu.android.dondusang.task.CentersLocationTask;
//import com.siu.android.dondusang.task.CentersUpdateTask;
//import com.siu.android.dondusang.task.CurrentLocationTask;
//import com.siu.android.dondusang.task.GeocoderLocationByNameTask;
//import com.siu.android.dondusang.toast.AppToast;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//@SuppressWarnings("deprecation")
//public class CenterMapActivity extends SherlockActivity {
//
//    private static final int ZOOM_LIMIT = 6;
//
//    private EnhancedMapView mapView;
//    private ListView listView;
//    private CenterAdapter listAdapter;
//
//    private GeocoderLocationByNameTask geocoderLocationByNameTask;
//    private CurrentLocationTask currentLocationTask;
//    private CentersLocationTask centersLocationTask;
//
//    // fix used to ignore the first map change, see initMap()
//    private boolean firstMapChanged;
//
//    //    private Runnable centerUpdateRunnable;
////    private Handler centerUpdateHandler;
//    private CentersUpdateTask centersUpdateTask;
//
//    private CenterOverlay centerOverlay;
//
//    private List<Center> centers;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        onCreate(savedInstanceState, R.layout.center_map_activity);
////        AirPushHelper.init(this);
//
//        mapView = (EnhancedMapView) findViewById(R.id.map);
//        listView = (ListView) findViewById(android.R.id.list);
//
//        initActionBar();
//
//        // TODO: catch the orientation change
//        centers = new ArrayList<Center>();
//
//        initMap();
//        initList();
//
//        RetainInstance retainInstance = (RetainInstance) getLastNonConfigurationInstance();
//        if (null != retainInstance) {
//            currentLocationTask = retainInstance.currentLocationTask;
//            currentLocationTask.setActivity(this);
//            if (currentLocationTask.isRunning()) {
//                setSupportProgressBarIndeterminateVisibility(true);
//            }
//
//            if (null != retainInstance.geocoderLocationByNameTask) {
//                geocoderLocationByNameTask = retainInstance.geocoderLocationByNameTask;
//                geocoderLocationByNameTask.setActivity(this);
//                setSupportProgressBarIndeterminateVisibility(true);
//            }
//
//            // TODO: need to switch destroyed activity in centerUpdateTask
//
//        } else {
//            mapView.getController().setZoom(7);
//            mapView.getController().setCenter(LocationUtils.getFranceGeoPoint());
//
//            currentLocationTask = new CurrentLocationTask(this);
//            startCurrentLocation();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (null == getLastNonConfigurationInstance()) {
////            EasyTracker.getTracker().trackPageView("/map");
//        }
//
//        // restart centers update task if failed because of network or something like that
//        if (centers.isEmpty() && (null == centersUpdateTask || centersUpdateTask.getStatus() != AsyncTask.Status.RUNNING)) {
//            startCenterUpdateTask();
//        }
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        // only at this moment map view is drawn and the coordinates from projection are correct
//        if (hasFocus && centers.isEmpty() && mapView.getZoomLevel() > ZOOM_LIMIT) {
//            startCentersLocation();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //stopCentersLocationIfRunning(); // always stop centers location because even in rotation case the map view size will change
//    }
//
//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        RetainInstance retainInstance = new RetainInstance();
//
//        currentLocationTask.setActivity(null);
//        retainInstance.currentLocationTask = currentLocationTask;
//
//        if (null != geocoderLocationByNameTask) {
//            geocoderLocationByNameTask.setActivity(null);
//            retainInstance.geocoderLocationByNameTask = geocoderLocationByNameTask;
//        }
//
//        EasyTracker.getTracker().trackActivityRetainNonConfigurationInstance();
//
//        return retainInstance;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getSupportMenuInflater().inflate(R.menu.center_map_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_search_address:
//                stopCurrentLocation(true);
//                stopCentersLocationIfRunning();
//
//                onSearchRequested();
//                break;
//
//            case R.id.menu_location:
//                stopGeocoderIfRunning();
//                stopCentersLocationIfRunning();
//
//                startCurrentLocation();
//                break;
//
//            case R.id.menu_infos:
//                stopGeocoderIfRunning();
//                stopCurrentLocation(false);
//
//                startActivity(new Intent(this, InfosActivity.class));
//                break;
//
//            case R.id.menu_preferences:
//                stopGeocoderIfRunning();
//                stopCurrentLocation(false);
//
//                startActivity(new Intent(this, PreferencesActivity.class));
//                break;
//        }
//
//        return true;
//    }
//
//    @Override
//    public void onNewIntent(Intent intent) {
//        setIntent(intent);
//
//        String name = null;
//
//        // get name from search input or from search auto complete
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            name = intent.getStringExtra(SearchManager.QUERY);
//        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
//            name = intent.getDataString();
//        }
//
//        if (StringUtils.isEmpty(name)) {
//            return;
//        }
//
//        startGeocoder(name);
//    }
//
//    private void initActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        ActionBar.Tab tab = actionBar.newTab();
//        tab.setText(" " + getString(R.string.misc_map)); // add whitespace because text is too close to the icon (only for the map)
//        tab.setIcon(getResources().getDrawable(R.drawable.ic_menu_map));
//        tab.setTabListener(new ActionBar.TabListener() {
//            @Override
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//                mapView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                mapView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            }
//        });
//        actionBar.addTab(tab);
//
//        tab = actionBar.newTab();
//        tab.setText(getString(R.string.misc_list));
//        tab.setIcon(getResources().getDrawable(R.drawable.ic_menu_list));
//        tab.setTabListener(new ActionBar.TabListener() {
//            @Override
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//                listView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                listView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            }
//        });
//        actionBar.addTab(tab);
//    }
//
//    private void initMap() {
//        mapView.setBuiltInZoomControls(true);
//        mapView.setOnChangeListener(new EnhancedMapView.OnChangeListener() {
//            @Override
//            public void onChange(EnhancedMapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom, int oldZoom) {
//                // ignore the first map change because it's done during the init and will stop the geolocalisation
//                if (!firstMapChanged) {
//                    firstMapChanged = true;
//                    return;
//                }
//
//                if (newZoom <= ZOOM_LIMIT) {
//                    centerOverlay.getOverlayItems().clear();
//                    centers.clear();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mapView.postInvalidate();
//                            listAdapter.notifyDataSetChanged();
//                        }
//                    });
//
//                    return;
//                }
//
//                // newZoom < oldZoom || (newZoom > oldZoom && oldZoom <= ZOOM_LIMIT)
//                if (!newCenter.equals(oldCenter) || newZoom != oldZoom) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            stopCurrentLocation(true);
//                            startCentersLocation();
//                        }
//                    });
//                }
//            }
//        });
//
//        centerOverlay = new CenterOverlay(getResources().getDrawable(R.drawable.ic_map_marker_blue), mapView);
//        mapView.getOverlays().add(centerOverlay);
//    }
//
//    private void initList() {
//        listAdapter = new CenterAdapter(this, centers);
//        listView.setAdapter(listAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Center center = (Center) listView.getItemAtPosition(i);
//
//                Intent intent = new Intent(CenterMapActivity.this, CenterDetailActivity.class);
//                intent.putExtra(CenterDetailActivity.EXTRA_CENTER, center);
//
//                startActivity(intent);
//            }
//        });
//    }
//
//
//    /* Current Location */
//
//    private void startCurrentLocation() {
//        if (currentLocationTask.isRunning()) {
//            return;
//        }
//
//        setSupportProgressBarIndeterminateVisibility(true);
//        new AppToast(CenterMapActivity.this, "Géolocalisation en cours...").show();
//        currentLocationTask.startCurrentLocation();
//    }
//
//    private void stopCurrentLocation(boolean showToast) {
//        if (!currentLocationTask.isRunning()) {
//            return;
//        }
//
//        setSupportProgressBarIndeterminateVisibility(false);
//        currentLocationTask.stopCurrentLocation();
//
//        if (showToast) {
//            new AppToast(CenterMapActivity.this, "Géolocalisation interrompue").show();
//        }
//    }
//
//    public void onCurrentLocationTaskResult(Location location) {
//        stopCurrentLocation(false);
//
//        if (null == location) {
//            new AppToast(CenterMapActivity.this, R.string.map_error_getcurrentlocation).show();
//            return;
//        }
//
//        Log.d(getClass().getName(), "Location result");
//        locatePositionOnMap(LocationUtils.getGeoPoint(location));
//    }
//
//
//    /* Geocoder */
//
//    private void startGeocoder(String name) {
//        stopGeocoderIfRunning();
//        geocoderLocationByNameTask = new GeocoderLocationByNameTask(this);
//        geocoderLocationByNameTask.execute(name);
//    }
//
//    private void stopGeocoderIfRunning() {
//        if (null == geocoderLocationByNameTask) {
//            return;
//        }
//
//        geocoderLocationByNameTask.cancel(true);
//        geocoderLocationByNameTask = null;
//    }
//
//    public void onGeocoderSuccess(GeoPoint geoPoint) {
//        locatePositionOnMap(geoPoint);
//    }
//
//    public void onGeocoderFailure(String name) {
//        new AppToast(this, String.format(getString(R.string.map_error_getlocation), name)).show();
//    }
//
//
//    /* Centers Update */
//
////    private void initCenterUpdate() {
////        centerUpdateRunnable = new Runnable() {
////            @Override
////            public void run() {
////                startCenterUpdateTask();
////                centerUpdateHandler.postDelayed(this, 1000 * 60 * 30); // update every 30 min
////            }
////        };
////
////        centerUpdateHandler = new Handler();
////        centerUpdateHandler.post(centerUpdateRunnable);
////    }
//
//    private void startCenterUpdateTask() {
//        if (null != centersUpdateTask) {
//            centersUpdateTask.cancel(true);
//        }
//
//        centersUpdateTask = new CentersUpdateTask(CenterMapActivity.this);
//        centersUpdateTask.execute();
//    }
//
//
//    /* Centers Location */
//
//    private void startCentersLocation() {
//        stopCentersLocationIfRunning();
//        setSupportProgressBarIndeterminateVisibility(true);
//
//        GeoPoint bottomLeftGeoPoint = mapView.getProjection().fromPixels(0, mapView.getHeight() - 1);
//        GeoPoint topRightGeoPoint = mapView.getProjection().fromPixels(mapView.getWidth() - 1, 0);
//
//        centersLocationTask = new CentersLocationTask(this);
//        centersLocationTask.execute(topRightGeoPoint.getLatitudeE6() / 1E6, topRightGeoPoint.getLongitudeE6() / 1E6,
//                bottomLeftGeoPoint.getLatitudeE6() / 1E6, bottomLeftGeoPoint.getLongitudeE6() / 1E6);
//    }
//
//    private void stopCentersLocationIfRunning() {
//        if (null == centersLocationTask) {
//            return;
//        }
//
//        // do not hide loading if current location task is still running
//        if (null == currentLocationTask || !currentLocationTask.isRunning()) {
//            setSupportProgressBarIndeterminateVisibility(false);
//        }
//
//        centersLocationTask.cancel(true);
//        centersLocationTask = null;
//    }
//
//    public void onCentersLocationTaskFinish(List<Center> receivedCenters) {
//        stopCentersLocationIfRunning();
//
//        centers.clear();
//        centerOverlay.getOverlayItems().clear();
//
//        if (null != receivedCenters) {
//            centers.addAll(receivedCenters);
//            centerOverlay.addCenters(centers);
//        }
//
//        listAdapter.notifyDataSetChanged();
//        mapView.invalidate();
//    }
//
//
//    private void locatePositionOnMap(GeoPoint geoPoint) {
//        mapView.getController().setZoom(13);
//        mapView.getController().animateTo(geoPoint);
//    }
//
//    @Override
//    protected boolean isRouteDisplayed() {
//        return false;
//    }
//
//    private static class RetainInstance {
//        private GeocoderLocationByNameTask geocoderLocationByNameTask;
//        private CurrentLocationTask currentLocationTask;
//    }
//}
