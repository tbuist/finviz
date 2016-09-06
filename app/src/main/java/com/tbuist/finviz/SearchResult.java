package com.tbuist.finviz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;



public class SearchResult extends AppCompatActivity {

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static final String[] STATS = {"Index", "Market Cap", "Income", "Sales", "Book/sh",
            "Cash/sh", "Dividend", "Dividend %", "Employees", "Optionable", "Shortable", "Recom",
            "P/E", "Forward P/E", "PEG", "P/S", "P/B", "P/C", "P/FCF", "Quick Ratio", "Current Ratio",
            "Debt/Eq", "LT Debt/Eq", "SMA20", "EPS (ttm)", "EPS next Y", "EPS next Q", "EPS this Y",
            "EPS next Y", "EPS next 5Y", "EPS past 5Y", "Sales past 5Y", "Sales Q/Q", "EPS Q/Q",
            "Earnings", "SMA50", "Insider Own", "Insider Trans", "Inst Own", "Inst Trans", "ROA",
            "ROE", "ROI", "Gross Margin", "Oper. Margin", "Profit Margin", "Payout", "SMA200",
            "Shs Outstand", "Shs Float", "Short Float", "Short Ratio", "Target Price", "52W Range",
            "52W High", "52W Low", "RSI (14)", "Rel Volume", "Avg Volume", "Volume", "Perf Week",
            "Perf Month", "Perf Quarter", "Perf Half Y", "Perf Year", "Beta", "ATR", "Volatility",
            "Prev Close", "Price", "Change"};

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
        int count = 0;
        if (doc != null) {
            // get tab info
            Element table = doc.select("td[class=fullview-links]").get(1);
            String[] info = new String[3];// 0 = sector, 1 = industry, 2 = country
            int idx = 0;
            for (Iterator<Element> ite = table.select("a[class=tab-link]").iterator(); ite.hasNext(); idx++) {
                info[count] = ite.next().text();
            }

            idx = 0;
            table = doc.select("table[class=snapshot-table2]").first();


            String sector = doc.select("a[class=tab-link]").eq(0).text();
            String industry = doc.select("a[class=tab-link]").eq(1).text();
            String country = doc.select("a[class=tab-link]").eq(2).text();


            HashMap<String, String> stats = new HashMap<>();

            Elements rows = table.getElementsByTag("tr");
            for (Element row : rows) {
                Elements entries = row.getElementsByTag("td");
                for (int i = 0; i < entries.size()-1; i=i+2) {
                    stats.put(entries.get(i).text(), entries.get(i+1).text());
                }
            }

            ArrayList<StockStat> sortedStats = new ArrayList<>();
            for (String str : STATS) {
                sortedStats.add(new StockStat(str, stats.get(str)));
            }

            ListView lv = (ListView) findViewById(R.id.stock_info);
            TextView stockname = (TextView)findViewById(R.id.stock_name);

            table = doc.select("table[class=fullview-title]").first();
            rows = table.getElementsByTag("tr");
            stockname.setText(rows.get(1).text());

            String link = doc.getElementById("chart0").attr("abs:src");
            new DownloadImageTask((ImageView) findViewById(R.id.stock_graph)).execute(link);

            StockStatAdaptor statAdapter = new StockStatAdaptor(this, sortedStats);
            lv.setAdapter(statAdapter);

            final ArrayList<StockStat> finalResults = sortedStats;

        }
    }
}


