package com.ptplanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.ptplanner.R;
import com.ptplanner.VideoViewActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by ltp on 14/07/15.
 */
public class TrainingViewPagerAdapter extends PagerAdapter {


    Context context;
    String imgURL, videoUrl;
    LayoutInflater inflater;
    View itemview;
    ImageView imgExercise;
    WebView webView;
    LinearLayout playVideo;
    ProgressBar pBar;
    LinearLayout llExtra;
    VideoView VIDEOVIEW;

    public TrainingViewPagerAdapter(Context context, int i, String imgURL, String videoUrl) {
        this.context = context;
        this.imgURL = imgURL;
        this.videoUrl = videoUrl;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {


        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Log.d("@@ KOUSHIK","-"+imgURL +videoUrl);

        if (position == 0 && (videoUrl.equalsIgnoreCase("") || videoUrl==null)) {
            Log.d("@@ KOUSHIK","1-"+imgURL +videoUrl);

            itemview = inflater.inflate(R.layout.training_viewpager_adapter, container, false);
            imgExercise = (ImageView) itemview.findViewById(R.id.img_exercise);


            Glide.with(context)
                    .load(imgURL)
//                    .placeholder(R.drawable.no_progress_images)
//                    .fitCenter()
                    .error(R.drawable.no_progress_images)
                    .into(imgExercise);
//            Picasso.with(context)
//                    .load(imgURL).error(R.drawable.no_progress_images)
//                    .into(imgExercise);

        } else
            {
                itemview = inflater.inflate(R.layout.training_view_pager_adaper_webview, container, false);
            playVideo = (LinearLayout) itemview.findViewById(R.id.play_video);
            pBar = (ProgressBar) itemview.findViewById(R.id.pbar);
            llExtra = (LinearLayout) itemview.findViewById(R.id.ll_extra);
            llExtra.setVisibility(View.GONE);

            playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoViewActivity.class);
                    intent.putExtra("videoUrl", videoUrl);
                    context.startActivity(intent);
//                    VideoPlayerDialog videoPlayerDialog = new VideoPlayerDialog(context, videoUrl);
//                    videoPlayerDialog.showLoader();
                }
            });

            webView = (WebView) itemview.findViewById(R.id.webview);


            try {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.getSettings().setSupportMultipleWindows(true);
                webView.getSettings().setSupportZoom(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setAllowFileAccess(true);
//            webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        pBar.setVisibility(View.GONE);
                        llExtra.setVisibility(View.VISIBLE);
                    }
                });
                webView.loadUrl(videoUrl);
            }catch (NullPointerException e)
            {}catch (Exception e){}


        }

        ((ViewPager) container).addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}