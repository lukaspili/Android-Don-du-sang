package com.siu.android.dondusang.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.dao.model.News;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class NewsDetailActivity extends SherlockFragmentActivity {

    public static final String EXTRA_NEWS = "extra_news";

    private TextView titleTextView;
    private TextView dateTextView;
    private WebView webView;

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTextView = (TextView) findViewById(R.id.news_detail_title);
        dateTextView = (TextView) findViewById(R.id.news_detail_date);
        webView = (WebView) findViewById(R.id.news_detail_webview);

        news = (News) getIntent().getExtras().get(EXTRA_NEWS);

        titleTextView.setText(news.getTitle());
//        dateTextView.setText(DateUtils.formatAsFull(news.getDate()));
//        WebUtils.loadToWeview(webView, news.getContent());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (null == getLastNonConfigurationInstance()) {
//            EasyTracker.getTracker().trackPageView("/news/detail");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
