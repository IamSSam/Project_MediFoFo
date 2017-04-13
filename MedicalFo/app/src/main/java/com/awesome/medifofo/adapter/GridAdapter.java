package com.awesome.medifofo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.awesome.medifofo.R;
import com.awesome.medifofo.activity.LoginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Eunsik on 03/26/2017.
 */

public class GridAdapter extends BaseAdapter {

    Context context;
    int layout;
    // image에 position 값이 연결되어있음.
    int image[];
    LayoutInflater inflater;

    public GridAdapter(Context context, int layout, int[] image) {
        this.context = context;
        this.layout = layout;
        this.image = image;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return image[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(layout, null);

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        //imageView.setImageResource(image[position]);
        imageLoader.displayImage("drawable://" + image[position], imageView);

        return convertView;
    }

}
