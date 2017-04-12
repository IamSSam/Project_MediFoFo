package com.awesome.medifofo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.awesome.medifofo.R;
import com.awesome.medifofo.adapter.SymptomListAdapter;
import com.awesome.medifofo.model.SymptomData;

public class SymptomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symtom);

        recyclerView = (RecyclerView) findViewById(R.id.rec_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SymptomListAdapter(SymptomData.getListData(), this);
        recyclerView.setAdapter(adapter);

    }
}
