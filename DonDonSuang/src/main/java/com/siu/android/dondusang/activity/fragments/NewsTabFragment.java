package com.siu.android.dondusang.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.activity.NewsDetailActivity;
import com.siu.android.dondusang.dao.model.News;
import com.siu.android.dondusang.list.NewsAdapter;
import com.siu.android.dondusang.task.NewsLoadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class NewsTabFragment extends SherlockFragment {

    private ListView listView;

    private NewsAdapter newsAdapter;
    private ArrayList<News> newsList = new ArrayList<News>();

    private NewsLoadTask newsLoadTask;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);

//        EasyTracker.getTracker().trackPageView("/news");
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.news_tab_fragment, viewGroup, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        // set new fragment's activity during rotation if async task is running
        if (null != newsLoadTask) {
            newsLoadTask.setFragment(this);
        }

        newsAdapter = new NewsAdapter(getActivity(), newsList);
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.EXTRA_NEWS, newsAdapter.getItem(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (newsList.isEmpty() && null == newsLoadTask) {
            startNewsLoadTask();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // invalidate old fragment's activity during rotation if async task is running
        if (null != newsLoadTask) {
            newsLoadTask.setFragment(null);
        }
    }

    /* NewsLoadTask */

    private void startNewsLoadTask() {
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        newsLoadTask = new NewsLoadTask(this);
        newsLoadTask.execute();
    }

    public void onNewsLoadTaskProgress(List<News> newsListLoaded) {
        if (newsListLoaded.isEmpty()) {
            return;
        }

        newsList.addAll(newsListLoaded);
        newsAdapter.notifyDataSetChanged();
    }

    public void onNewsLoadTaskFinish(List<News> newsListLoaded) {
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
        newsLoadTask = null;

        if (null == newsListLoaded) {
            return;
        }

        newsList.clear();
        newsList.addAll(newsListLoaded);
        newsAdapter.notifyDataSetChanged();
    }
}
