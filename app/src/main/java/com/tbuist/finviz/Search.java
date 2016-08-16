package com.tbuist.finviz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import java.io.*;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void searchStocks(View view) {
        EditText text = (EditText)findViewById(R.id.ticker_search_str);
        String value = text.getText().toString();

        String url = "finviz.com/quote.ashx?t=" + value;

        WebView webview = (WebView)findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);

        setContentView(R.layout.activity_search_result);
    }
}
