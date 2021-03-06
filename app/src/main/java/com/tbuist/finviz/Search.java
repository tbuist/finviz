package com.tbuist.finviz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Search extends AppCompatActivity {

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
            displayStocks(doc);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListView lv = (ListView)findViewById(R.id.resultsListView);
                lv.setAdapter(null);
                startMyTask();
                hideKeyboard(v);
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

    public void displayStocks(Document doc) {

        int count = 0;
        if (doc != null) {
            Element table = doc.select("table[class=table-search]").first();

            ArrayList<StockResult> results = new ArrayList<>();


            for (Iterator<Element> ite = table.select("tr[class=search-light-row]").iterator(); ite.hasNext(); count++) {
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

            ListView lv = (ListView) findViewById(R.id.resultsListView);
            StockAdapter resultsAdapter = new StockAdapter(this, results);
            lv.setAdapter(resultsAdapter);

            final ArrayList<StockResult> finalResults = results;

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                    String ticker = finalResults.get(position).ticker;
                    intent.putExtra("ticker", ticker);
                    startActivity(intent);
                }
            });
        }

        String title = " results found";
        if (count >= 200) {
            title = "200+" + title;
        } else if (count == 0) {
            title = "0" + title;
        } else {
            title = count + title;
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, title, duration);
        toast.show();
    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
