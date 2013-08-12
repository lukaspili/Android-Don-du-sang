//package com.siu.android.dondusang.task;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import com.google.gson.reflect.TypeToken;
//import com.siu.android.andutils.util.CryptoUtils;
//import com.siu.android.andutils.util.HttpUtils;
//import com.siu.android.andutils.util.NetworkUtils;
//import com.siu.android.dondusang.R;
//import com.siu.android.dondusang.dao.DatabaseHelper;
//import com.siu.android.dondusang.dao.model.Center;
//import com.siu.android.dondusang.dao.model.CenterDao;
//import com.siu.android.dondusang.gson.GsonFormatter;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.Iterator;
//import java.util.List;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class CentersUpdateTask extends AsyncTask<Void, Void, Void> {
//
//    private Context context;
//    private SharedPreferences sharedPreferences;
//
//    public CentersUpdateTask(Context context) {
//        this.context = context;
//        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//        Log.d(getClass().getName(), "CentersUpdateTask");
//
//        if (!NetworkUtils.isOnline()) {
//            return null;
//        }
//
//        String urlAsString = String.format(context.getString(R.string.webservice_centers_url),
//                (context.getResources().getInteger(R.integer.webservice_map_france_nelat) / 1E6),
//                (context.getResources().getInteger(R.integer.webservice_map_france_nelon) / 1E6),
//                (context.getResources().getInteger(R.integer.webservice_map_france_swlat) / 1E6),
//                (context.getResources().getInteger(R.integer.webservice_map_france_swlon) / 1E6));
//
//        String data = HttpUtils.get(urlAsString);
//
//        if (isCancelled() || StringUtils.isEmpty(data)) {
//            return null;
//        }
//
//        String md5 = CryptoUtils.md5Hex(data);
//        String existingMd5 = sharedPreferences.getString(context.getString(R.string.application_preferences_centers_md5), "");
//
//        Log.d(getClass().getName(), "current : " + md5);
//        Log.d(getClass().getName(), "existing : " + existingMd5);
//
//        if (existingMd5.equals(md5)) {
//            return null;
//        }
//
//        List<Center> centers;
//        try {
//            centers = GsonFormatter.getGson().fromJson(data, new TypeToken<List<Center>>() {}.getType());
//        } catch (Exception e) {
//            Log.e(getClass().getName(), "Error parsing centers", e);
//            return null;
//        }
//
//        if (isCancelled()) {
//            return null;
//        }
//
//        updateCenters(centers, md5);
//
//        return null;
//    }
//
//    private void updateCenters(final List<Center> centers, String md5) {
//        final CenterDao centerDao = DatabaseHelper.getInstance().getDaoSession().getCenterDao();
//
//        centerDao.getSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
//                centerDao.deleteAll();
//                for (Iterator<Center> it = centers.iterator(); it.hasNext(); ) {
//                    centerDao.insert(it.next());
//                }
//            }
//        });
//
//        sharedPreferences.edit()
//                .putString(context.getString(R.string.application_preferences_centers_md5), md5)
//                .commit();
//    }
//}