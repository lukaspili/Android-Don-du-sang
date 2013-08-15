package com.siu.android.dondusang.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.model.Center;
import com.siu.android.dondusang.toast.NiceToast;
import com.siu.android.dondusang.util.WebUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class CenterDetailActivity extends SherlockFragmentActivity {

    public static final String EXTRA_CENTER = "extra_center";

    private TextView titleTextView;
    private TextView cityTextView;
    private TextView phoneTextView;
    private WebView webView;

    private Center center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_detail_activity);

        titleTextView = (TextView) findViewById(R.id.center_detail_title);
        cityTextView = (TextView) findViewById(R.id.center_detail_city_text);
        phoneTextView = (TextView) findViewById(R.id.center_detail_phone_text);

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
        getSupportMenuInflater().inflate(R.menu.center_detail_menu, menu);
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
                    NiceToast.makeText(this, "Le numéro du centre n\'est pas connnu", Toast.LENGTH_LONG).show();
                    return true;
                }

                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + center.getPhone()));
                    startActivity(callIntent);
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error during call : " + center.getPhone(), e);
                    NiceToast.makeText(this, "Impossible d'appeller le centre avec le numéro suivant : " + center.getPhone(), Toast.LENGTH_LONG).show();
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
        titleTextView.setText(center.getTitle());
        cityTextView.setText("Ville : " + center.getCity());

        String phone = "Tél : ";
        if (StringUtils.isNotEmpty(center.getPhone())) {
            phone += center.getPhone();
        } else {
            phone += "Numéro inconnu";
        }
        phoneTextView.setText(phone);

        WebUtils.loadToWeview(webView, center.getDescription());
    }
}
