package com.tbuist.finviz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // first called from activity_search.xml,
    public void goToSearch(View view) {
        Intent search = new Intent(this, Search.class);
        startActivity(search);
    }
}
