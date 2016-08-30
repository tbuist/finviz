package com.tbuist.finviz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();

        String tmp = "url";
        String url;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                url = null;
            } else {
                url = extras.getString(tmp);
            }
        } else {
            url = (String) savedInstanceState.getSerializable(tmp);
        }

        WebView webview = (WebView) findViewById(R.id.webView);
        webview.loadUrl(url);

        View curview = this.getCurrentFocus();
        if (curview != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(curview.getWindowToken(), 0);
        }
    }
}
