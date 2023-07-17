package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends Activity {

    private WebView mWebView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });

        mWebView = findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());

        // REMOTE RESOURCE
        mWebView.loadUrl("https://vlr.gg");

        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            mSwipeRefreshLayout.setRefreshing(false);

            // Load and execute custom JavaScript from a URL
            String scriptUrl = "https://gist.githubusercontent.com/myhiy/a40e2481427ee909d8430f872ffdfb4a/raw/browser.js";
            String javascriptCode = "(function() {" +
                    "   var xhr = new XMLHttpRequest();" +
                    "   xhr.onreadystatechange = function() {" +
                    "       if (xhr.readyState === 4 && xhr.status === 200) {" +
                    "           eval(xhr.responseText);" +
                    "       }" +
                    "   };" +
                    "   xhr.open('GET', '" + scriptUrl + "', true);" +
                    "   xhr.send();" +
                    "})()";
            view.evaluateJavascript(javascriptCode, null);

            // Load and apply custom CSS styles from a URL
            String cssUrl = "https://gist.githubusercontent.com/myhiy/22fc97267c306139660c30d222d49344/raw/browser.css";
            String javascriptCodeCss = "(function() {" +
                    "   var xhr = new XMLHttpRequest();" +
                    "   xhr.onreadystatechange = function() {" +
                    "       if (xhr.readyState === 4 && xhr.status === 200) {" +
                    "           var style = document.createElement('style');" +
                    "           style.innerHTML = xhr.responseText;" +
                    "           document.head.appendChild(style);" +
                    "       }" +
                    "   };" +
                    "   xhr.open('GET', '" + cssUrl + "', true);" +
                    "   xhr.send();" +
                    "})()";
            view.evaluateJavascript(javascriptCodeCss, null);
        }
    }
}
