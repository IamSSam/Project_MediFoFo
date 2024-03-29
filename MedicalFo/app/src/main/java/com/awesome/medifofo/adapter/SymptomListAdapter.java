package com.awesome.medifofo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesome.medifofo.R;
import com.awesome.medifofo.model.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Created by Eunsik on 04/09/2017.
 */

public class SymptomListAdapter extends RecyclerView.Adapter<SymptomListAdapter.ListHolder> {

    private Context context;
    private List<ListItem> listData;
    private LayoutInflater layoutInflater;

    public SymptomListAdapter(List<ListItem> listData) {
        this.listData = listData;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        final ListItem item = listData.get(position);
        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class ListHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private View container;

        private ListHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.symptom_item);
            container = view.findViewById(R.id.symptom_container);
        }
    }


}