package com.ptplanner;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.ptplanner.customcameranew.CameraPreview;
import com.ptplanner.helper.AppConfig;
import java.io.IOException;
import java.util.Locale;

public class EsolzCamera extends AppCompatActivity {

    private CameraPreview mPreview;
    RelativeLayout mLayout;
    ImageView capture;
    LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_esolz_camera);
        /////////////////////////////////////////////////////
        setContentView(R.layout.activity_esolz_camera);
        mLayout = (RelativeLayout) findViewById(R.id.surfaceViewbucket);
        capture = (ImageView) findViewById(R.id.captureimage);
        progressBar = (LinearLayout) findViewById(R.id.progressloader);

        capture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    mPreview.captureImage();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LayoutParams previewLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPreview = new CameraPreview(this, 0,
                CameraPreview.LayoutMode.FitToParent, progressBar);

        mLayout.addView(mPreview, 0, previewLayoutParams);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
