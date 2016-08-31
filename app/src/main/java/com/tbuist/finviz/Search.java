package com.tbuist.finviz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Search extends AppCompatActivity {

    static class StockResult {
        public String ticker;
        public String company;
        public String sector;
        public String industry;
        public String country;
        public String cap;

        public StockResult(String in_ticker, String in_company, String in_sector, String in_industry, String in_country, String in_cap) {
            this.ticker = in_ticker;
            this.company = in_company;
            this.sector = in_sector;
            this.industry = in_industry;
            this.country = in_country;
            this.cap = in_cap;
        }
    }

    private class RetrieveDocumentTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            // network stuff where Jsoup grabs html data
            String url = urls[0];
            Document doc = null;
            try {
                int count = 0;
                while (count < 5 && doc == null) {
                    count++;
                    doc = Jsoup.connect(url).get();
                }
            } catch (IOException ioe) {
                System.err.println("IOException, Search.java");
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {

            // important view stuff with scraped data happens here?

            if (doc == null) {
                return;
            }
            /*
            String title = doc.title();
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, title, duration);
            toast.show();
            */

            Element table = doc.select("table[class=table-search]").first();

            if (table == null) {
                // no search results found
                String title = "0 results found";
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, title, duration);
                toast.show();
                return;
            }

            ArrayList<StockResult> results = new ArrayList<>();

            for (Iterator<Element> ite = table.select("tr[class=search-light-row]").iterator(); ite.hasNext(); ) {
                // ticker, company, sector, industry, country, cap
                String ticker, company, sector, industry, country, cap;
                // chain tr/td
                Elements tds = ite.next().select("td");
                ticker = tds.eq(0).text();
                company = tds.eq(1).text();
                sector = tds.eq(2).text();
                industry = tds.eq(3).text();
                country = tds.eq(4).text();
                cap = tds.eq(5).text();

                results.add(new StockResult(ticker, company, sector, industry, country, cap));
            }

            String title = " results found";
            if (results.size() == 200) {
                title = "200+" + title;
            } else {
                title = results.size() + title;
            }

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, title, duration);
            toast.show();
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMyTask();
            }
        });
    }

    public String getURL() {
        EditText text = (EditText)findViewById(R.id.search_str);
        String value;
        if (text == null) {
            value = "ALK";
        } else {
            value = text.getText().toString();
        }

        return "http://finviz.com/search.ashx?t=c&p=" + value;
    }

    public void startMyTask() {
        RetrieveDocumentTask task = new RetrieveDocumentTask();
        task.execute(getURL());
    }

    public void goToSearch(View view) {
        startMyTask();
    }
}
