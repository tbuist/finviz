package com.tbuist.finviz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ChangeLayout(View view){

        setContentView(R.layout.activity_search);
    }

    public void searchStocks(View view) {

        EditText text = (EditText)findViewById(R.id.search_str);
        String value = text.getText().toString();

        String url = "http://finviz.com/search.ashx?t=c&p=" + value;

        Intent search = new Intent(this, Search.class);
        search.putExtra("url", url);
        startActivity(search);

    }
}
