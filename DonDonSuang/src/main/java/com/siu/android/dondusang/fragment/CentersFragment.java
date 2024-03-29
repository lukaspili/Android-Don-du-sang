package com.siu.android.dondusang.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.siu.android.dondusang.AppConstants;
import com.siu.android.dondusang.Application;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.activity.CenterDetailActivity;
import com.siu.android.dondusang.broadcast.NetworkBroadcastReceiver;
import com.siu.android.dondusang.broadcast.NetworkBroadcastReceiverCallback;
import com.siu.android.dondusang.list.CenterAdapter;
import com.siu.android.dondusang.map.CenterInfoWindowAdatper;
import com.siu.android.dondusang.model.Center;
import com.siu.android.dondusang.task.GetLocationByNameTask;
import com.siu.android.dondusang.task.SimpleTask;
import com.siu.android.dondusang.toast.NiceToast;
import com.siu.android.dondusang.volley.CentersRequest;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lukas on 8/12/13.
 */
public class CentersFragment extends SherlockFragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final float ZOOM_LOCATION = 11.5F;
    private static final float ZOOM_INITIAL = 5.5F;
    private static final String CENTERS_REQUEST_TAG = "centers request";

    private LocationClient mLocationClient;
    private Location mCurrentLocation;

    private View mMapFrameView;
    private GoogleMap mMap;

    private ListView mListView;
    private CenterAdapter mListAdapter;

    private GetLocationByNameTask mGetLocationByNameTask;

    private NetworkBroadcastReceiver mNetworkBroadcastReceiver;

    private Map<Marker, Center> mMarkerCenters;
    private Map.Entry<Marker, Center> mCurrentMarkerCenter;
    private List<Center> mCenters;

    private boolean mPlayServicesInitialized;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mMarkerCenters = new HashMap<Marker, Center>();
        mCenters = new ArrayList<Center>();
        mPlayServicesInitialized = false;

        mNetworkBroadcastReceiver = new NetworkBroadcastReceiver(getActivity(), new NetworkBroadcastReceiverCallback() {
            @Override
            public void onNetworkConnectivityChange(boolean connected) {
                Log.d(getClass().getName(), "network change connected = " + connected);

                if (connected) {
                    startGetCentersRequest();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.centers_fragment, null);
        mListView = (ListView) view.findViewById(R.id.list);
        mMapFrameView = view.findViewById(R.id.map_frame);

        Fragment fragment = getFragmentManager().findFragmentByTag("map fragment");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fragmentTransaction.add(R.id.map_frame, fragment, "map fragment");
        } else {
            fragmentTransaction.attach(fragment);
        }
        fragmentTransaction.commit();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initList();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mLocationClient != null && !mLocationClient.isConnected()) {
            mLocationClient.connect();
        }

        getActivity().registerReceiver(mNetworkBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        if (mPlayServicesInitialized) {
            startGetCentersRequest();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMap == null) {
            if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) == ConnectionResult.SUCCESS) {
                initWithValidPlayServices();
            } else {
                NiceToast.makeText(getActivity(), R.string.must_install_play_services, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onDestroyView() {
        if (!getActivity().isFinishing()) {
            SupportMapFragment fragment = (SupportMapFragment) getFragmentManager().findFragmentByTag("map fragment");
            if (fragment != null) {
                getFragmentManager().beginTransaction().detach(fragment).commit();
            }
        }

        super.onDestroyView();
    }

    @Override
    public void onStop() {
        stopGetCentersRequest();

        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }

        getActivity().unregisterReceiver(mNetworkBroadcastReceiver);

        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.centers_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list:
                if (mMapFrameView.getVisibility() == View.VISIBLE) {
                    mMapFrameView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    item.setTitle(R.string.menu_map);
                    item.setIcon(R.drawable.map);
                } else {
                    mListView.setVisibility(View.GONE);
                    mMapFrameView.setVisibility(View.VISIBLE);
                    item.setTitle(R.string.menu_list);
                    item.setIcon(R.drawable.list);
                }
                return true;

            case R.id.menu_reload:
                startGetCentersRequest();
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public void onNewIntent(Intent intent) {
//        getActivity().setIntent(intent);
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
//        startGetLocationByNameTask(name);
//    }

    private void initWithValidPlayServices() {
        initMap();
        initLocation();

        mPlayServicesInitialized = true;
        startGetCentersRequest();
    }


    /* Map */

    private void initMap() {
        mMap = ((SupportMapFragment) getFragmentManager().findFragmentByTag("map fragment")).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setInfoWindowAdapter(new CenterInfoWindowAdatper(this));

        // initial position
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(AppConstants.FRANCE_LATITUDE, AppConstants.FRANCE_LONGITUDE))
                .zoom(ZOOM_INITIAL)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        // camera change listener
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                // cancel get location from address task that was running
                if (mGetLocationByNameTask != null && mGetLocationByNameTask.stopTaskIfRunning()) {
                    mGetLocationByNameTask = null;
                    NiceToast.makeText(getActivity(), R.string.location_from_address_task_canceled, Toast.LENGTH_LONG).show();
                }

                // hide current station if exists and out of the screen
                if (mCurrentMarkerCenter != null && !mMap.getProjection().getVisibleRegion().latLngBounds
                        .contains(new LatLng(mCurrentMarkerCenter.getValue().getLatitude(), mCurrentMarkerCenter.getValue().getLongitude()))) {
                    mCurrentMarkerCenter.getKey().hideInfoWindow();
                    mCurrentMarkerCenter = null;
                }

                startGetCentersRequest();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                mCurrentMarkerCenter = new AbstractMap.SimpleEntry<Marker, Center>(marker, mMarkerCenters.get(marker));

                return true;
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mLocationClient != null) {
                    Location location = mLocationClient.getLastLocation();
                    if (location != null) {
                        animateCameraToLocalizedPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                }

                return true;
            }
        });
    }

    private void animateCameraToLocalizedPosition(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(latLng)
                .zoom(ZOOM_LOCATION)
                .build()));
    }


    /* Location */

    private void initLocation() {
        mLocationClient = new LocationClient(getActivity(), this, this);
        mLocationClient.connect();
    }

    private void startLocation() {
        mCurrentLocation = mLocationClient.getLastLocation();

        if (mCurrentLocation == null) {
            Log.d(getClass().getName(), "No location available");
            return;
        }

        animateCameraToLocalizedPosition(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
    }

    @Override
    public void onConnected(Bundle bundle) {
        // locate automatically the first time
        if (mCurrentLocation == null) {
            startLocation();
        }

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    /* List */

    private void initList() {
        mListAdapter = new CenterAdapter(getActivity(), mCenters);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Center center = (Center) mListView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), CenterDetailActivity.class);
                intent.putExtra(CenterDetailActivity.EXTRA_CENTER, center);

                startActivity(intent);
            }
        });
    }


    /* Centers */

    private void startGetCentersRequest() {
        if (!mPlayServicesInitialized) {
            return;
        }

        // cancel previous requests
        stopGetCentersRequest();

        String url = String.format(AppConstants.CENTERS_REQUEST_URL,
                mMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude, mMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude,
                mMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude, mMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);

        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        CentersRequest request = new CentersRequest(url, new Response.Listener<List<Center>>() {
            @Override
            public void onResponse(List<Center> centers) {
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

                // remove from list in any case (re add all if new centers)
                mCenters.clear();

                if (centers == null || centers.isEmpty()) {
                    // remove from map
                    for (Map.Entry<Marker, Center> entry : mMarkerCenters.entrySet()) {
                        entry.getKey().remove();
                    }

                    mMarkerCenters.clear();
                } else {
                    // add to list
                    mCenters.addAll(centers);

                    // add to map
                    Iterator<Map.Entry<Marker, Center>> it = mMarkerCenters.entrySet().iterator();
                    Map.Entry<Marker, Center> entry;

                    while (it.hasNext()) {
                        entry = it.next();

                        if (centers.contains(entry.getValue())) {
                            centers.remove(entry.getValue());
                        } else {
                            entry.getKey().remove();
                            it.remove();
                        }
                    }

                    BitmapDescriptor blueBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.pin_blue);
                    BitmapDescriptor redBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.pin_red);
                    for (Center center : centers) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(center.getLatitude(), center.getLongitude()));

                        if (center.isPermanent()) {
                            markerOptions.icon(blueBitmapDescriptor);
                        } else {
                            markerOptions.icon(redBitmapDescriptor);
                        }

                        mMarkerCenters.put(mMap.addMarker(markerOptions), center);
                    }
                }

                mListAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NiceToast.makeText(getActivity(), R.string.get_centers_request_error, Toast.LENGTH_SHORT).show();
            }
        }
        );

        request.setTag(CENTERS_REQUEST_TAG);
        Application.getRequestQueue().add(request);
    }

    private void stopGetCentersRequest() {
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
        Application.getRequestQueue().cancelAll(CENTERS_REQUEST_TAG);
    }


    /* Get location by name task */

    private void startGetLocationByNameTask(String name) {
        SimpleTask.stopTaskAndGetNullReference(mGetLocationByNameTask);

        mGetLocationByNameTask = new GetLocationByNameTask(this, name);
        mGetLocationByNameTask.execute();
    }

    public void onGetLocationByNameResult(LatLng latLng) {
        mGetLocationByNameTask = SimpleTask.stopTaskAndGetNullReference(mGetLocationByNameTask);

        if (latLng == null) {
            NiceToast.makeText(getActivity(), R.string.location_from_address_not_found, Toast.LENGTH_LONG).show();
            return;
        }

        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(ZOOM_LOCATION)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }


    public Map<Marker, Center> getMarkerCenters() {
        return mMarkerCenters;
    }
}