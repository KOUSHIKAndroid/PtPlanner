package com.ptplanner;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.ptplanner.helper.AppConfig;
import java.util.Locale;

/**
 * Created by ltp on 23/11/15.
 */
public class VideoViewActivity extends Activity {

//    fkjndfjknfdj Commect

    String videoLink;
    ProgressBar pBar;
    RelativeLayout closeDialog;
    WebView webView;
    LinearLayout playVideo;
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String languageToLoad = AppConfig.LANGUAGE; // your language "sv --- > swedish :: en ---- > english"
        Locale mlocale = new Locale(languageToLoad);
        Locale.setDefault(mlocale);
        Configuration config = new Configuration();
        config.locale = mlocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.dialog_videopaly);

        pBar = (ProgressBar) findViewById(R.id.pbar);
        closeDialog = (RelativeLayout) findViewById(R.id.close_pager);
        playVideo = (LinearLayout) findViewById(R.id.play_video);
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pBar.setVisibility(View.GONE);
            }
        });
        if (savedInstanceState != null)
        {
            ((WebView)findViewById(R.id.webview)).restoreState(savedInstanceState);
        }
        else
        {
//            webView.loadUrl("https://www.youtube.com/watch?v=wo0ospGvxXc");
            webView.loadUrl(getIntent().getExtras().getString("videoUrl"));

            Log.d("@@ VIDEO URL-",getIntent().getExtras().getString("videoUrl"));
        }





    }

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}