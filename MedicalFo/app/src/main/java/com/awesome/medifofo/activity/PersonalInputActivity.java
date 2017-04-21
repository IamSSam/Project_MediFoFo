package com.awesome.medifofo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.medifofo.model.CountryItem;
import com.awesome.medifofo.R;
import com.awesome.medifofo.adapter.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Eunsik on 2017-04-05.
 */

public class PersonalInputActivity extends AppCompatActivity {

    private EditText year, month, day;
    public static String userName, userGender, userAge;
    private Animation animationShake;

    public static String sfYear = "userYear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_input);

        animationShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
        setMyCountry();
        setPersonalInfo();
        moveToMainActivity();
    }

    private void setMyCountry() {
        String[] countriesName = Locale.getISOCountries();
        ArrayList<CountryItem> list = new ArrayList<>();

        for (int i = 0; i < countriesName.length; i++) {
            Locale object = new Locale("", countriesName[i]);
            list.add(new CountryItem(object.getDisplayName() + " (" + countriesName[i] + ")"));
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner_personal_input_country);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.content_spinner, R.id.country_name, list);
        spinner.setAdapter(adapter);
    }

    private void moveToMainActivity() {
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextInputLayout textInputLayoutMonth = (TextInputLayout) findViewById(R.id.text_input_layout_month);
                TextInputLayout textInputLayoutDay = (TextInputLayout) findViewById(R.id.text_input_layout_day);
                TextInputLayout textInputLayoutYear = (TextInputLayout) findViewById(R.id.text_input_layout_year);

                boolean cancel = false;
                View focus = null;

                year = (EditText) findViewById(R.id.year);
                month = (EditText) findViewById(R.id.month);
                day = (EditText) findViewById(R.id.day);

                String userMonth = month.getText().toString();
                String userDay = day.getText().toString();
                String userYear = year.getText().toString();


                // User Input Exception
                if (TextUtils.isEmpty(userMonth)) {
                    textInputLayoutMonth.setError(getString(R.string.error_field_required));
                    textInputLayoutMonth.setAnimation(animationShake);
                    textInputLayoutMonth.setAnimation(animationShake);
                    focus = month;
                    cancel = true;
                } else if (userMonth.length() != 2) {
                    textInputLayoutMonth.setError(getString(R.string.error_invalid_field));
                    textInputLayoutMonth.setAnimation(animationShake);
                    textInputLayoutMonth.setAnimation(animationShake);
                    focus = month;
                    cancel = true;
                } else if (TextUtils.isEmpty(userDay)) {
                    textInputLayoutDay.setError(getString(R.string.error_field_required));
                    textInputLayoutDay.setAnimation(animationShake);
                    textInputLayoutDay.setAnimation(animationShake);
                    focus = day;
                    cancel = true;
                } else if (userDay.length() != 2) {
                    textInputLayoutMonth.setError(getString(R.string.error_invalid_field));
                    textInputLayoutMonth.setAnimation(animationShake);
                    textInputLayoutMonth.setAnimation(animationShake);
                    focus = day;
                    cancel = true;
                } else if (TextUtils.isEmpty(userYear)) {
                    textInputLayoutYear.setError(getString(R.string.error_field_required));
                    textInputLayoutYear.setAnimation(animationShake);
                    textInputLayoutYear.setAnimation(animationShake);
                    focus = year;
                    cancel = true;
                } else if (userYear.length() != 4) {
                    textInputLayoutMonth.setError(getString(R.string.error_invalid_field));
                    textInputLayoutMonth.setAnimation(animationShake);
                    textInputLayoutMonth.setAnimation(animationShake);
                    focus = year;
                    cancel = true;
                }


                if (cancel) {
                    focus.requestFocus();
                } else {
                    calculateUserAge();
                    SharedPreferences sharedPreferences = getSharedPreferences(sfYear, 1);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("YEAR", userAge);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        });
    }


    private void setPersonalInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(FirstActivity.sharedPreferenceFile, 0);
        userName = sharedPreferences.getString("NAME", "");
        userGender = sharedPreferences.getString("GENDER", "");

        TextView name = (TextView) findViewById(R.id.userId);
        name.setText(userName);

        TextView gender = (TextView) findViewById(R.id.gender);
        gender.setText(userGender);
    }

    private void calculateUserAge() {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int userYear = currentYear - Integer.parseInt(year.getText().toString());
        userAge = String.valueOf(userYear);
    }

}
