package com.tbuist.finviz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;

public class Search extends AppCompatActivity {

    static class StockResult {
        public String ticker;
        public String company;
        public String sector;
        public String country;
        public String cap;

        public StockResult(String in_ticker, String in_company, String in_sector, String in_country, String in_cap) {
            this.ticker = in_ticker;
            this.company = in_company;
            this.sector = in_sector;
            this.country = in_country;
            this.cap = in_cap;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        try {
            Document doc = Jsoup.connect(url).get();
            Elements rows = doc.select("")
        }
        catch (IOException e) {
            System.err.println("IOException, Search.java");
        }
}
