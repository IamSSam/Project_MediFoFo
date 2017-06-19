package com.awesome.medifofo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.medifofo.R;
import com.awesome.medifofo.model.ListItem;

import java.util.List;

/**
 * Created by Eunsik on 2017-06-16.
 */

public class SubwayListAdapter extends RecyclerView.Adapter<SubwayListAdapter.ListHolder> {

    private List<ListItem> listData;
    private LayoutInflater layoutInflater;

    public SubwayListAdapter(List<ListItem> listData) {
        this.listData = listData;
    }

    @Override
    public SubwayListAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_item, parent, false);
        return new SubwayListAdapter.ListHolder(view);
    }

    @Override
    public void onBindViewHolder(SubwayListAdapter.ListHolder holder, int position) {
        final ListItem item = listData.get(position);
        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class ListHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView subwayIcon;
        private View container;

        private ListHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.subway_item);
            subwayIcon = (ImageView) view.findViewById(R.id.subway_icon);
            container = view.findViewById(R.id.subway_container);
        }
    }
}
