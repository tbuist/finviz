package com.tbuist.finviz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class StockResult {
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

class StockStat {
    public String stat;
    public String value;
    // var for text color

    public StockStat(String in_stat, String in_value) {
        stat = in_stat;
        value = in_value;
    }
}

class StockAdapter extends ArrayAdapter<StockResult> {
    // View lookup cache
    private class ViewHolder {
        TextView tick;
        TextView comp;
    }
    public StockAdapter(Context context, ArrayList<StockResult> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StockResult stockresult = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_stock_result, parent, false);
            viewHolder.tick = (TextView) convertView.findViewById(R.id.result_ticker);
            viewHolder.comp = (TextView) convertView.findViewById(R.id.result_company);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tick.setText(stockresult.ticker);
        viewHolder.comp.setText(stockresult.company);
        // Return the completed view to render on screen
        return convertView;
    }
}

class StockStatAdaptor extends ArrayAdapter<StockStat> {
    // View lookup cache
    private class ViewHolder {
        TextView stat;
        TextView value;
    }
    public StockStatAdaptor(Context context, ArrayList<StockStat> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StockStat stockstat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_stat_layout, parent, false);
            viewHolder.stat = (TextView) convertView.findViewById(R.id.stat);
            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.stat.setText(stockstat.stat);
        viewHolder.value.setText(stockstat.value);
        // Return the completed view to render on screen
        return convertView;
    }
}
