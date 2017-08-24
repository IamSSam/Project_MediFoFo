package com.awesome.medifofo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.awesome.medifofo.Person;
import com.awesome.medifofo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by Eunsik on 11/29/2016.
 */

public class PersonalHealthRecordActivity extends AppCompatActivity {

    public TextView phrHeight;
    public TextView phrWeight;
    public TextView phrAbo;
    public TextView phrMedicine;
    public TextView phrAllergy;
    public TextView phrHistory;
    public TextView phrSleepTime;
    public TextView phrDailyStride;

    private Button saveButton;

    String currentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_phr);

        initView();

        //setInformationIfExist();

        saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                currentDateTime = sdfNow.format(date);

            }
        });


    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_phr);
        setSupportActionBar(toolbar);

        phrHeight = (TextView) findViewById(R.id.phr_height);
        phrWeight = (TextView) findViewById(R.id.phr_weight);
        phrAbo = (TextView) findViewById(R.id.phr_abo);
        phrMedicine = (TextView) findViewById(R.id.phr_medicine);
        phrAllergy = (TextView) findViewById(R.id.phr_allergy);
        phrHistory = (TextView) findViewById(R.id.phr_history);
        phrSleepTime = (TextView) findViewById(R.id.phr_sleepTime);
        phrDailyStride = (TextView) findViewById(R.id.phr_dailyStride);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}