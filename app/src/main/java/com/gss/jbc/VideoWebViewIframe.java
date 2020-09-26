package com.gss.jbc;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class VideoWebViewIframe extends AppCompatActivity {
    WebView videoWeb;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_web_view_iframe);
        url = getIntent().getStringExtra("url").replace(" ","%20");
        videoWeb = (WebView) findViewById(R.id.iframeweb);
//        String abc = "<iframe> <video width=\"512\" height=\"380\" controls controlsList=\"nodownload\"></video> src=\"+url+\" allowfullscreen=\"true\"  mozallowfullscreen=\"true\"  webkitallowfullscreen=\"true\"></iframe>";
//       videoWeb.loadDataWithBaseURL("", abc, "text/html", "UTF-8", null);
        videoWeb.loadUrl(url);
//        videoWeb.setWebViewClient(new Browser());
//        videoWeb.setWebChromeClient(new MyWebClient());
        videoWeb.setWebChromeClient(new WebChromeClient());
        videoWeb.setWebViewClient(new WebViewClient());
        videoWeb.getSettings().setJavaScriptEnabled(true);

        videoWeb.getSettings().setPluginState(WebSettings.PluginState.ON);
//        videoWeb.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);

//        videoWeb.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                request.allowScanningByMediaScanner();
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "JewelNet-News-videos");
//                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                dm.enqueue(request);
//                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
//                        Toast.LENGTH_LONG).show();
//
//            }
//        });

    }
    public class Browser extends WebViewClient
    {
        Browser() {}

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
        {
            paramWebView.loadUrl(paramString);
            return true;
        }
    }

    public class MyWebClient extends WebChromeClient
    {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public MyWebClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (VideoWebViewIframe.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(VideoWebViewIframe.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)VideoWebViewIframe.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            VideoWebViewIframe.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            VideoWebViewIframe.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = VideoWebViewIframe.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = VideoWebViewIframe.this.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)VideoWebViewIframe.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            VideoWebViewIframe.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
//        Intent i =new Intent(getApplicationContext(),ItemDisplay.class);
//        startActivity(i);

    }
}

