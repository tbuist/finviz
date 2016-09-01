package com.tbuist.finviz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        String tmp = "ticker";
        String ticker;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                ticker = null;
            } else {
                ticker = extras.getString(tmp);
            }
        } else {
            ticker = (String) savedInstanceState.getSerializable(tmp);
        }

        RetrieveDocumentTask rdt = new RetrieveDocumentTask();
        rdt.execute("http://finviz.com/quote.ashx?t=" + ticker);
    }

    private class RetrieveDocumentTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            // network stuff where Jsoup grabs html data
            String url = urls[0];
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException ioe) {
                System.err.println("IOException, Search.java");
                return null;
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            showInfo(doc);
        }
    }

    public void showInfo(Document doc) {

    }
}


