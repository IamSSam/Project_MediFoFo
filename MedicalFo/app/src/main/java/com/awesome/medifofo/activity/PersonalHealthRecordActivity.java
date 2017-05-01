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

    public EditText phrHeight;
    public EditText phrWeight;
    public EditText phrAbo;
    public EditText phrMedicine;
    public EditText phrAllergy;
    public EditText phrHistory;
    public EditText phrSleepTime;
    public EditText phrDailyStride;

    static Button saveButton;

    String currentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phr);

        initView();

        //setInformationIfExist();

        saveButton = (Button) findViewById(R.id.button_register);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("0yyyy-MM-dd");
                currentDateTime = sdfNow.format(date);

                /*
                new HttpAsyncTask().execute("http://igrus.mireene.com/applogin/personInfo.php/?userid=" + Person.userId + "&height=" + PersonalInfoFragment.phrHeight.getText().toString()
                        + "&weight=" + PersonalInfoFragment.phrWeight.getText().toString() + "&abo=" + PersonalInfoFragment.phrAbo.getText().toString() + "&medicine=" + PersonalInfoFragment.phrMedicine.getText().toString()
                        + "&allergy=" + PersonalInfoFragment.phrAllergy.getText().toString() + "&history=" + PersonalInfoFragment.phrHistory.getText().toString() + "&sleeptime=" + PersonalInfoFragment.phrSleepTime.getText().toString()
                        + "&dailystride=" + PersonalInfoFragment.phrDailyStride.getText().toString() + "&date=" + currentDateTime.toString());
                */
            }
        });


    }

    public void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_phr);
        setSupportActionBar(toolbar);

        phrHeight = (EditText) findViewById(R.id.phr_height);
        phrWeight = (EditText) findViewById(R.id.phr_weight);
        phrAbo = (EditText) findViewById(R.id.phr_abo);
        phrMedicine = (EditText) findViewById(R.id.phr_medicine);
        phrAllergy = (EditText) findViewById(R.id.phr_allergy);
        phrHistory = (EditText) findViewById(R.id.phr_history);
        phrSleepTime = (EditText) findViewById(R.id.phr_sleepTime);
        phrDailyStride = (EditText) findViewById(R.id.phr_dailyStride);
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

    /*
    private void setInformationIfExist() {
        if (!Person.d_height.equals("-1")) {
            phrHeight.setText(Person.d_height, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_weight.equals("-1")) {
            phrWeight.setText(Person.d_weight, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_abo.equals("-1")) {
            phrAbo.setText(Person.d_abo, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_medicine.equals("-1")) {
            phrMedicine.setText(Person.d_medicine, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_allergy.equals("-1")) {
            phrAllergy.setText(Person.d_allergy, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_history.equals("-1")) {
            phrHistory.setText(Person.d_history, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_sleeptime.equals("-1")) {
            phrSleepTime.setText(Person.d_sleeptime, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_dailystride.equals("-1")) {
            phrDailyStride.setText(Person.d_dailystride, TextView.BufferType.EDITABLE);
        }
    }
    */

}