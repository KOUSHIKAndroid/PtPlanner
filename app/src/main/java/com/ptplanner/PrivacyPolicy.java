package com.ptplanner;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.ptplanner.helper.AppConfig;

import java.util.Locale;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //////////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_privacy_policy);

        WebView webView=(WebView)findViewById(R.id.webview);
        webView.loadUrl(AppConfig.HOST+"app_control/privacy_policy");
        findViewById(R.id.backIMG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
