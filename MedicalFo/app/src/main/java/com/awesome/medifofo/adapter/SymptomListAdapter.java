package com.awesome.medifofo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesome.medifofo.R;
import com.awesome.medifofo.model.ListItem;
import com.google.android.gms.common.data.DataHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 17 on 2017-04-09.
 */

public class SymptomListAdapter extends RecyclerView.Adapter<SymptomListAdapter.ListHolder> {

    private List<ListItem> listData;
    private LayoutInflater layoutInflater;
    private ArrayList<ListItem> arrayList;

    public SymptomListAdapter(List<ListItem> listData, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listData = listData;
        this.arrayList = new ArrayList<ListItem>();
        this.arrayList.addAll(listData);
    }


    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.content_list_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        ListItem item = listData.get(position);
        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listData.clear();
        if (charText.length() == 0) {
            listData.addAll(arrayList);
        } else {
            for (ListItem item : arrayList) {
                if (item.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listData.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    class ListHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private View container;

        public ListHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.symptom_item);
            container = view.findViewById(R.id.symptom_container);
        }
    }

}


