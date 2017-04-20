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
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yoonjae on 29/11/2016.
 */

public class PersonalHealthRecordActivity extends AppCompatActivity {

    public static EditText tab1_abo;
    static EditText tab1_allergy;
    static EditText tab1_dailyStride;
    static EditText tab1_height;
    static EditText tab1_history;
    static EditText tab1_medicine;
    static EditText tab1_sleepTime;
    static EditText tab1_weight;

    static Button saveButton;

    String currentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phr);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_phr);
        setSupportActionBar(toolbar);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        //int age = year - Person.birth.getYear();

        int cmonth = Calendar.getInstance().get(Calendar.MONTH);
        //int myMonth = Person.birth.getMonth();

        tab1_height = (EditText) findViewById(R.id.phr_height);
        tab1_weight = (EditText) findViewById(R.id.phr_weight);
        tab1_abo = (EditText) findViewById(R.id.phr_abo);
        tab1_medicine = (EditText) findViewById(R.id.phr_medicine);
        tab1_allergy = (EditText) findViewById(R.id.phr_allergy);
        tab1_history = (EditText) findViewById(R.id.phr_history);
        tab1_sleepTime = (EditText) findViewById(R.id.phr_sleepTime);
        tab1_dailyStride = (EditText) findViewById(R.id.phr_dailyStride);

        setInformationIfExist();

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("0yyyy-MM-dd");
                currentDateTime = sdfNow.format(date);

                /*
                new HttpAsyncTask().execute("http://igrus.mireene.com/applogin/personInfo.php/?userid=" + Person.userId + "&height=" + PersonalInfoFragment.tab1_height.getText().toString()
                        + "&weight=" + PersonalInfoFragment.tab1_weight.getText().toString() + "&abo=" + PersonalInfoFragment.tab1_abo.getText().toString() + "&medicine=" + PersonalInfoFragment.tab1_medicine.getText().toString()
                        + "&allergy=" + PersonalInfoFragment.tab1_allergy.getText().toString() + "&history=" + PersonalInfoFragment.tab1_history.getText().toString() + "&sleeptime=" + PersonalInfoFragment.tab1_sleepTime.getText().toString()
                        + "&dailystride=" + PersonalInfoFragment.tab1_dailyStride.getText().toString() + "&date=" + currentDateTime.toString());


                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root, new EditedPersonalInfoFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
                */
            }
        });

        /*
        String user_name = Person.name;
        String user_age = "" + age;

        if (((cmonth + 1) - myMonth) >= 0) {
            tab1_age.setText("만 " + age + "세" + ". " + (cmonth - myMonth + 1) + "개월");
            System.out.println("=============================================" + cmonth);
        } else {
            tab1_age.setText("만 " + (age - 1) + "세" + ". " + (cmonth - myMonth + 13) + "개월");
            System.out.println("=============================================" + cmonth);
        }

        String user_sex;
        if (Person.sex == 1)
            user_sex = "남";
        else
            user_sex = "여";
        tab1_sex.setText(user_sex);
        */

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
            // TODO: Implement goAboutPage();
            // case R.id.main_about:


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setInformationIfExist() {
        if (!Person.d_height.equals("-1")) {
            tab1_height.setText(Person.d_height, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_weight.equals("-1")) {
            tab1_weight.setText(Person.d_weight, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_abo.equals("-1")) {
            tab1_abo.setText(Person.d_abo, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_medicine.equals("-1")) {
            tab1_medicine.setText(Person.d_medicine, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_allergy.equals("-1")) {
            tab1_allergy.setText(Person.d_allergy, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_history.equals("-1")) {
            tab1_history.setText(Person.d_history, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_sleeptime.equals("-1")) {
            tab1_sleepTime.setText(Person.d_sleeptime, TextView.BufferType.EDITABLE);
        }
        if (!Person.d_dailystride.equals("-1")) {
            tab1_dailyStride.setText(Person.d_dailystride, TextView.BufferType.EDITABLE);
        }
    }




}