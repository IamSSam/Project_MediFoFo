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

/**
 * Created by Eunsik on 2017-06-06.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ListHolder> {

    private Context context;
    private List<ListItem> listData;

    public DoctorListAdapter(List<ListItem> listData) {
        this.listData = listData;
    }

    @Override
    public DoctorListAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_doctor_list_item, parent, false);
        return new DoctorListAdapter.ListHolder(view);
    }

    @Override
    public void onBindViewHolder(DoctorListAdapter.ListHolder holder, int position) {
        final ListItem item = listData.get(position);
        holder.name.setText(item.getTitle());
        holder.hospital.setText(item.getHospitalName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {

        private TextView name, hospital;
        private View container;

        private ListHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.doctor_name);
            hospital = (TextView) view.findViewById(R.id.hospital_name);
            container = view.findViewById(R.id.doctor_container);
        }
    }
}


