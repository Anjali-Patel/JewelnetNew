package com.gss.jbc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AdvWebview extends AppCompatActivity {
    WebView adv_webView;
    ProgressBar AdvProgress;
    String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_webview);
        adv_webView = findViewById(R.id.adv_webview);
        AdvProgress = findViewById(R.id.adv_web_progress);
        Url = getIntent().getStringExtra("adv_link");
        Log.e("URL",Url);
        adv_webView.loadUrl(Url);
//        adv_webView.setWebViewClient(new WebViewClient());
        adv_webView.getSettings().setJavaScriptEnabled(true);
//        adv_webView.setWebChromeClient(new WebChromeClient());
/*
        adv_webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                AdvProgress.setProgress(progress);
                if (progress == 100) {
                    AdvProgress.setVisibility(View.GONE);
                } else {
                    AdvProgress.setVisibility(View.VISIBLE);
                }
            }
        });
*/
        adv_webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);

                // runs when a page starts loading
                AdvProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // page finishes loading
                AdvProgress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                // runs when there's a failure in loading page
                AdvProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failure on loading web page", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (adv_webView.canGoBack()){
            adv_webView.goBack();
        }
        else {
            finish();
        }
    }
}
