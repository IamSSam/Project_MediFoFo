package com.awesome.medifofo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.awesome.medifofo.FindHospitalActivity;
import com.awesome.medifofo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewActivity extends AppCompatActivity implements GeolocationPermissions.Callback {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview_nMap);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        Geoclient geoclient = new Geoclient();
        webView.setWebChromeClient(geoclient);
        String origin = "";
        geoclient.onGeolocationPermissionsShowPrompt(origin, this);
        webView.loadUrl("http:igrus.mireene.com/medifofo_web/webview2/webview.html");
    }

    @Override
    public void invoke(String origin, boolean allow, boolean retain) {

    }

    class Geoclient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }
    }

}
