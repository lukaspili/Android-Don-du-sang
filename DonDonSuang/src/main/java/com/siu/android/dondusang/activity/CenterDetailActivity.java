package com.siu.android.dondusang.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.siu.android.dondusang.R;
import com.siu.android.dondusang.model.Center;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class CenterDetailActivity extends ActionBarActivity {

    public static final String EXTRA_CENTER = "extra_center";

    private TextView titleTextView;
    private TextView cityTextView;
    private TextView phoneTextView;
    private WebView webView;

    private Center center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        setContentView(R.layout.center_detail_activity);

        titleTextView = (TextView) findViewById(R.id.center_detail_title);
        cityTextView = (TextView) findViewById(R.id.center_detail_city);
        phoneTextView = (TextView) findViewById(R.id.center_detail_phone);

        webView = (WebView) findViewById(R.id.center_detail_webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        center = (Center) getIntent().getExtras().get(EXTRA_CENTER);

        initActionBar();
        initCenter();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (null == getLastNonConfigurationInstance()) {
//            EasyTracker.getTracker().trackPageView("/center/detail");
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.center_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_call:
                if (StringUtils.isEmpty(center.getPhone())) {
//                    new AppToast(this, "Le numéro du centre n\'est pas connnu");
                    return true;
                }

                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + center.getPhone()));
                    startActivity(callIntent);
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error during call : " + center.getPhone(), e);
//                    new AppToast(this, "Impossible d'appeller le centre avec le numéro suivant : " + center.getPhone());
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initCenter() {
//        titleTextView.setText(center.getTitle());
//        cityTextView.setText(center.getCity());
//
//        if (StringUtils.isNotEmpty(center.getPhone())) {
//            phoneTextView.setText(center.getPhone());
//        }
//
//        WebUtils.loadToWeview(webView, center.getDescription());
    }
}
