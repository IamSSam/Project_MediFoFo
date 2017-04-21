package com.awesome.medifofo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.medifofo.model.CountryItem;
import com.awesome.medifofo.R;

import java.util.ArrayList;

/**
 * Created by Eunsik on 2017-04-03.
 */

public class SpinnerAdapter extends ArrayAdapter<CountryItem> {

    private int groupId;
    private ArrayList<CountryItem> list;
    private LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupId, int id, ArrayList<CountryItem> list) {
        super(context, id, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupId, parent, false);
        TextView textView = (TextView) itemView.findViewById(R.id.country_name);
        textView.setText(list.get(position).getCountryName());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
