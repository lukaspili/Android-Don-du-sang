//package com.siu.android.dondusang.task;
//
//import android.os.AsyncTask;
//import android.util.Log;
//import com.google.gson.reflect.TypeToken;
//import com.siu.android.andutils.util.HttpUtils;
//import com.siu.android.andutils.util.NetworkUtils;
//import com.siu.android.dondusang.Application;
//import com.siu.android.dondusang.R;
//import com.siu.android.dondusang.activity.CenterMapActivity;
//import com.siu.android.dondusang.dao.DatabaseHelper;
//import com.siu.android.dondusang.dao.model.Center;
//import com.siu.android.dondusang.dao.model.CenterDao;
//import com.siu.android.dondusang.gson.GsonFormatter;
//
//import java.util.List;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class CentersLocationTask extends AsyncTask<Double, Void, List<Center>> {
//
//    private CenterMapActivity activity;
//
//    public CentersLocationTask(CenterMapActivity activity) {
//        this.activity = activity;
//    }
//
//    @Override
//    protected List<Center> doInBackground(Double... coords) {
//        Log.d(getClass().getName(), "CentersLocationTask");
//
//        double neLat = coords[0];
//        double neLon = coords[1];
//        double swLat = coords[2];
//        double swLon = coords[3];
//
//        CenterDao centerDao = DatabaseHelper.getInstance().getDaoSession().getCenterDao();
//
//        if (centerDao.count() == 0) {
//            if (!NetworkUtils.isOnline() || isCancelled()) {
//                return null;
//            }
//
//            Log.d(getClass().getName(), "Get centers from url");
//            return getCenters(String.format(Application.getContext().getString(R.string.webservice_centers_url), neLat, neLon, swLat, swLon));
//        }
//
//        if (isCancelled()) {
//            return null;
//        }
//
//        List<Center> centers = centerDao.queryBuilder()
//                .where(CenterDao.Properties.Latitude.between(swLat, neLat), CenterDao.Properties.Longitude.between(swLon, neLon))
//                .list();
//
//        if (isCancelled()) {
//            return null;
//        }
//
//        if (centers.size() < 30 && NetworkUtils.isOnline()) {
//            Log.d(getClass().getName(), "Get centers from url for temporal centers");
//            return getCenters(String.format(Application.getContext().getString(R.string.webservice_centers_url), neLat, neLon, swLat, swLon));
//        }
//
//        Log.d(getClass().getName(), "Get centers from database");
//        return centers;
//    }
//
//    @Override
//    protected void onPostExecute(List<Center> centers) {
//        if (isCancelled() || null == activity) {
//            return;
//        }
//
//        activity.onCentersLocationTaskFinish(centers);
//    }
//
//    private List<Center> getCenters(String urlAsString) {
//        if (!NetworkUtils.isOnline()) {
//            return null;
//        }
//
//        String data = HttpUtils.get(urlAsString);
//
//        if (null == data) {
//            return null;
//        }
//
//        try {
//            return GsonFormatter.getGson().fromJson(data, new TypeToken<List<Center>>() {}.getType());
//        } catch (Exception e) {
//            Log.e(getClass().getName(), "Error parsing centers", e);
//            return null;
//        }
//    }
//
//    public void setActivity(CenterMapActivity activity) {
//        this.activity = activity;
//    }
//}
