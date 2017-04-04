package com.awesome.medifofo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 17 on 2017-04-03.
 */

public class SpinnerAdapter extends ArrayAdapter<CountryItem> {

    int groupId;
    Activity context;
    ArrayList<CountryItem> list;
    LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupId, int id, ArrayList<CountryItem> list) {
        super(context, id, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupId, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.flag);
        imageView.setImageResource(list.get(position).getFlagId());
        TextView textView = (TextView) itemView.findViewById(R.id.country_name);
        textView.setText(list.get(position).getCountryName());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
