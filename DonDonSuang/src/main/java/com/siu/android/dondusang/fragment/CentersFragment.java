package com.siu.android.dondusang.fragment;

import android.app.SearchManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
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
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.activity.CenterDetailActivity;
import com.siu.android.dondusang.broadcast.NetworkBroadcastReceiver;
import com.siu.android.dondusang.broadcast.NetworkBroadcastReceiverCallback;
import com.siu.android.dondusang.dao.model.Center;
import com.siu.android.dondusang.list.CenterAdapter;
import com.siu.android.dondusang.map.CenterInfoWindowAdatper;
import com.siu.android.dondusang.task.GetLocationByNameTask;
import com.siu.android.dondusang.task.SimpleTask;
import com.siu.android.dondusang.toast.NiceToast;
import com.siu.android.dondusang.volley.CentersRequest;
import com.siu.android.dondusang.volley.OkHttpStack;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lukas on 8/12/13.
 */
public class CentersFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final int ZOOM_LIMIT = 6;
    private static final int ZOOM_LOCATION = 6;
    private static final int ZOOM_INITIAL = 6;
    private static final String CENTERS_REQUEST_TAG = "centers request";

    private GoogleMap mMap;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;

    private ListView mListView;
    private CenterAdapter mListAdapter;

    private RequestQueue mRequestQueue;

    private GetLocationByNameTask mGetLocationByNameTask;

    private NetworkBroadcastReceiver mNetworkBroadcastReceiver;

    private Map<Marker, Center> mMarkerCenters;
    private Map.Entry<Marker, Center> mCurrentMarkerCenter;
    private List<Center> mCenters;
    private Center mCurrentCenter;

    private boolean mPlayServicesInitialized;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mRequestQueue = Volley.newRequestQueue(getActivity(), new OkHttpStack());
        mCenters = new ArrayList<Center>();
        mPlayServicesInitialized = false;

        mNetworkBroadcastReceiver = new NetworkBroadcastReceiver(getActivity(), new NetworkBroadcastReceiverCallback() {
            @Override
            public void onNetworkConnectivityChange(boolean connected) {
                Log.d(getClass().getName(), "network change connected = " + connected);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.centers_fragment, null);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mLocationClient != null && !mLocationClient.isConnected()) {
            mLocationClient.connect();
        }

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

        getActivity().registerReceiver(mNetworkBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mNetworkBroadcastReceiver);

        super.onPause();
    }

    @Override
    public void onStop() {
        if (getActivity().isFinishing()) {
            mRequestQueue.stop();
        }

        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {


        super.onDestroy();
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
        initList();

        mPlayServicesInitialized = true;
        startGetCentersRequest();
    }


    /* Map */

    private void initMap() {
        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setInfoWindowAdapter(new CenterInfoWindowAdatper(getActivity()));

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
                if (mCurrentCenter != null && !mMap.getProjection().getVisibleRegion().latLngBounds
                        .contains(new LatLng(mCurrentCenter.getLatitude(), mCurrentCenter.getLongitude()))) {
//                    hideCurrentCenter();
                }

                startGetCentersRequest();
            }
        });

//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                showStation(mMarkersStations.get(marker));
//
//                // fake info window in order to bring the marker to front
//                marker.showInfoWindow();
//                mCurrentMarker = marker;
//
//                return true;
//            }
//        });
    }

    private void animateCameraToPosition(LatLng latLng) {
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

        animateCameraToPosition(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
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
        // cancel previous requests
        stopGetCentersRequest();

        String url = String.format(AppConstants.CENTERS_REQUEST_URL,
                mMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude, mMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude,
                mMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude, mMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);

        CentersRequest request = new CentersRequest(url, new Response.Listener<List<Center>>() {
            @Override
            public void onResponse(List<Center> centers) {
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

                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.pin_blue);
                    for (Center center : centers) {
                        mMarkerCenters.put(mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(center.getLatitude(), center.getLongitude()))
                                .icon(bitmapDescriptor)), center);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );

        request.setTag(CENTERS_REQUEST_TAG);
        mRequestQueue.add(request);
    }

    private void stopGetCentersRequest() {
        mRequestQueue.cancelAll(CENTERS_REQUEST_TAG);
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
}
