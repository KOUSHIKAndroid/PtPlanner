package com.ptplanner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.ptplanner.R;

/**
 * Created by ltp on 23/11/15.
 */
public class VideoPlayerDialog {

    Context context;
    String videoLink;
    VideoView videoview;
    Dialog videoPlayerDialog;
    ProgressBar pBar;
    RelativeLayout closeDialog;
    WebView webView;

    public VideoPlayerDialog(Context context, String videoLink) {
        this.context = context;
        this.videoLink = videoLink;

        videoPlayerDialog = new Dialog(context);
        videoPlayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //videoPlayerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        videoPlayerDialog.setCanceledOnTouchOutside(false);
        videoPlayerDialog.setContentView(R.layout.dialog_videopaly);

        // videoview = (VideoView) videoPlayerDialog.findViewById(R.id.VideoView);
        pBar = (ProgressBar) videoPlayerDialog.findViewById(R.id.pbar);
        closeDialog = (RelativeLayout) videoPlayerDialog.findViewById(R.id.close_pager);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissLoader();
            }
        });

        webView = (WebView) videoPlayerDialog.findViewById(R.id.webview);
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
        webView.loadUrl(videoLink);

//        try {
//            // Start the MediaController
//            MediaController mediacontroller = new MediaController(context);
//            mediacontroller.setAnchorView(videoview);
//            // Get the URL from String VideoURL
//            Uri video = Uri.parse(videoLink);
//            videoview.setMediaController(mediacontroller);
//            videoview.setVideoURI(video);
//
//        } catch (Exception e) {
//            Log.e("Video Error ", e.getMessage());
//            e.printStackTrace();
//        }
//
//        videoview.requestFocus();
//        videoview.setOnPreparedListener(new OnPreparedListener() {
//            // Close the progress bar and play the video
//            public void onPrepared(MediaPlayer mp) {
//                pBar.setVisibility(View.GONE);
//                videoview.start();
//            }
//        });

    }

    public void showLoader() {
        videoPlayerDialog.show();
    }

    public void dismissLoader() {
        videoPlayerDialog.dismiss();
    }

}
