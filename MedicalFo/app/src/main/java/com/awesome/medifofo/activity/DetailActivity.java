package com.awesome.medifofo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.awesome.medifofo.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);

        //Bundle extras = getIntent().getBundleExtra("BUNDLE_EXTRAS");

        //  ((TextView)findViewById(R.id.symptom_title)).setText(extras.getString("EXTRA_QUOTE"));
    }
}
