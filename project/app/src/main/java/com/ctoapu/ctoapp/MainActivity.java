package com.ctoapu.ctoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.*;

public class MainActivity extends Activity {
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri uri = r.getUrl();
                String sc = uri.getScheme();
                if ("http".equals(sc) || "https".equals(sc)) {
                    if (uri.toString().startsWith("file://")) return false;
                    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); return true; }
                    catch (Exception e) { return false; }
                }
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView v, String u, String m, final JsResult r) {
                new AlertDialog.Builder(MainActivity.this)
                    .setMessage(m).setPositiveButton(android.R.string.ok, (d, w) -> r.confirm())
                    .setCancelable(false).show();
                return true;
            }
            @Override
            public boolean onJsConfirm(WebView v, String u, String m, final JsResult r) {
                new AlertDialog.Builder(MainActivity.this)
                    .setMessage(m).setPositiveButton(android.R.string.ok, (d, w) -> r.confirm())
                    .setNegativeButton(android.R.string.cancel, (d, w) -> r.cancel())
                    .setCancelable(false).show();
                return true;
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
        setContentView(webView);
    }

    @Override
    public boolean onKeyDown(int kc, KeyEvent e) {
        if (kc == KeyEvent.KEYCODE_BACK && webView != null && webView.canGoBack()) {
            webView.goBack(); return true;
        }
        return super.onKeyDown(kc, e);
    }
    @Override protected void onPause() { super.onPause(); if (webView != null) webView.onPause(); }
    @Override protected void onResume() { super.onResume(); if (webView != null) webView.onResume(); }
}
