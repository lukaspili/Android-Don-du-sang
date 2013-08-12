package com.siu.android.dondusang.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.siu.android.dondusang.Application;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.activity.fragments.NewsTabFragment;
import com.siu.android.dondusang.dao.DatabaseHelper;
import com.siu.android.dondusang.dao.model.News;
import com.siu.android.dondusang.dao.model.NewsDao;

import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class NewsLoadTask extends AsyncTask<Void, List<News>, List<News>> {

    private NewsTabFragment fragment;
    private SharedPreferences preferences;

    public NewsLoadTask(NewsTabFragment fragment) {
        this.fragment = fragment;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
    }

    @Override
    protected List<News> doInBackground(Void... voids) {
        String existingMd5 = preferences.getString(Application.getContext().getString(R.string.application_preferences_news_md5), "");
        final NewsDao newsDao = DatabaseHelper.getInstance().getDaoSession().getNewsDao();

//        if (StringUtils.isNotEmpty(existingMd5)) {
//            publishProgress(newsDao.loadAll());
//        }
//
//        if (!NetworkUtils.isOnline()) {
//            return null;
//        }
//
//        String data = HttpUtils.get(Application.getContext().getString(R.string.webservice_news_url));
//
//        if (StringUtils.isEmpty(data)) {
//            return null;
//        }
//
//        String currentMd5 = CryptoUtils.md5Hex(data);
//
//        if (existingMd5.equals(currentMd5)) {
//            return null;
//        }
//
//        List<News> newsList = new NewsParser().parse(data);
//
//        if (null == newsList) {
//            return null;
//        }
//
//        newsDao.insertInTx(newsList);
//        preferences.edit().putString(Application.getContext().getString(R.string.application_preferences_news_md5), currentMd5).commit();
//
//        return newsList;

        return null;
    }

    @Override
    protected void onProgressUpdate(List<News>... values) {
        if (null == fragment) {
            return;
        }

        fragment.onNewsLoadTaskProgress(values[0]);
    }

    @Override
    protected void onPostExecute(List<News> newsListLoaded) {
        if (null == fragment) {
            return;
        }

        fragment.onNewsLoadTaskFinish(newsListLoaded);
    }

    public void setFragment(NewsTabFragment fragment) {
        this.fragment = fragment;
    }
}
