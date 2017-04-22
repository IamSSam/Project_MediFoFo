package com.awesome.medifofo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.medifofo.Person;
import com.awesome.medifofo.R;
import com.awesome.medifofo.adapter.SpinnerAdapter;
import com.awesome.medifofo.model.CountryItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/*
 * Created by Eunsik on 04/16/2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public String temp_gender, country;
    public EditText userPassword;
    private Button manButton, womanButton;
    private ScrollView scrollView;
    private AutoCompleteTextView userFirstName, userLastName, userEmailView, userYear, userMonth, userDay;

    public String sharedPreferenceFile = "userSignUpFILE";
    private FirebaseAuth firebaseAuth;
    private Animation animationShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
        setMyCountry();
    }

    public void initView() {
        firebaseAuth = FirebaseAuth.getInstance();
        userFirstName = (AutoCompleteTextView) findViewById(R.id.user_first_name);
        userLastName = (AutoCompleteTextView) findViewById(R.id.user_last_name);
        userEmailView = (AutoCompleteTextView) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);
        userYear = (AutoCompleteTextView) findViewById(R.id.user_year);
        userMonth = (AutoCompleteTextView) findViewById(R.id.user_month);
        userDay = (AutoCompleteTextView) findViewById(R.id.user_day);

        userPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_UNSPECIFIED || id == R.id.ime_password) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        manButton = (Button) findViewById(R.id.user_man);
        manButton.setOnClickListener(this);
        womanButton = (Button) findViewById(R.id.user_woman);
        womanButton.setOnClickListener(this);
        Button signUpButton = (Button) findViewById(R.id.button_register);
        signUpButton.setOnClickListener(this);

        scrollView = (ScrollView) findViewById(R.id.form_sign_up);
        animationShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
    }

    public void saveUserInformation(String firstName, String lastName, String email, String gender, int age, String country) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferenceFile, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FIRSTNAME", firstName);
        editor.putString("LASTNAME", lastName);
        editor.putString("EMAIL", email);
        editor.putString("GENDER", gender);
        editor.putInt("AGE", age);
        editor.putString("COUNTRY", country);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_man:
                // man 버튼을 누른다면..
                if (!manButton.isSelected()) {
                    manButton.setSelected(true);
                    manButton.setTextColor(Color.BLACK);
                    womanButton.setSelected(false);
                    womanButton.setTextColor(Color.parseColor("#999999"));
                    temp_gender = "Male";

                } else {
                    manButton.setSelected(false);
                    manButton.setTextColor(Color.parseColor("#999999"));
                    temp_gender = null;
                }

                break;

            case R.id.user_woman:
                // woman 버튼을 누른다면..
                if (!womanButton.isSelected()) {
                    womanButton.setSelected(true);
                    womanButton.setTextColor(Color.BLACK);
                    manButton.setSelected(false);
                    manButton.setTextColor(Color.parseColor("#999999"));
                    temp_gender = "Female";

                } else {
                    womanButton.setSelected(false);
                    womanButton.setTextColor(Color.parseColor("#999999"));
                    temp_gender = null;
                }

                break;

            case R.id.button_register:
                attemptSignUp();
                break;
        }
    }

    private void createUser(String email, String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, "Please wait...", "Processing...", true);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Registration successful",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void attemptSignUp() {
        TextInputLayout textInputLayoutFirstName = (TextInputLayout) findViewById(R.id.text_input_layout_user_first_name);
        TextInputLayout textInputLayoutLastName = (TextInputLayout) findViewById(R.id.text_input_layout_user_last_name);
        TextInputLayout textInputLayoutEmail = (TextInputLayout) findViewById(R.id.text_input_layout_user_email);
        TextInputLayout textInputLayoutPassword = (TextInputLayout) findViewById(R.id.text_input_layout_user_password);
        TextInputLayout textInputLayoutMonth = (TextInputLayout) findViewById(R.id.text_input_layout_user_month);
        TextInputLayout textInputLayoutDay = (TextInputLayout) findViewById(R.id.text_input_layout_user_day);
        TextInputLayout textInputLayoutYear = (TextInputLayout) findViewById(R.id.text_input_layout_user_year);

        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String email = userEmailView.getText().toString();
        String password = userPassword.getText().toString();
        String gender = temp_gender;
        String month = userMonth.getText().toString();
        String day = userDay.getText().toString();
        String age = userYear.getText().toString();

        boolean cancel = false;
        View focus = null;

        if (TextUtils.isEmpty(firstName)) {
            textInputLayoutFirstName.setError(getString(R.string.error_field_required));
            textInputLayoutFirstName.setAnimation(animationShake);
            textInputLayoutFirstName.startAnimation(animationShake);
            focus = userFirstName;
            cancel = true;
        } else if (TextUtils.isEmpty(lastName)) {
            textInputLayoutLastName.setError(getString(R.string.error_field_required));
            textInputLayoutLastName.setAnimation(animationShake);
            textInputLayoutLastName.startAnimation(animationShake);
            focus = userLastName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            textInputLayoutEmail.setError(getString(R.string.error_field_required));
            textInputLayoutEmail.setAnimation(animationShake);
            textInputLayoutEmail.startAnimation(animationShake);
            focus = userEmailView;
            cancel = true;
        } else if (!this.isEmailValid(email)) {
            textInputLayoutEmail.setError(getString(R.string.error_invalid_email));
            textInputLayoutEmail.setAnimation(animationShake);
            textInputLayoutEmail.startAnimation(animationShake);
            focus = userEmailView;
            cancel = true;
        } else if (!this.isPasswordValid(password)) {
            textInputLayoutPassword.setError(getString(R.string.error_invalid_password));
            textInputLayoutPassword.setAnimation(animationShake);
            textInputLayoutPassword.startAnimation(animationShake);
            focus = userPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(password) && isEmailValid(email)) {
            textInputLayoutPassword.setError(getString(R.string.error_field_required));
            textInputLayoutPassword.setAnimation(animationShake);
            textInputLayoutPassword.startAnimation(animationShake);
            focus = userPassword;
            cancel = true;
        } else if (!manButton.isSelected() && !womanButton.isSelected()) {
            Toast.makeText(getApplication(),
                    R.string.error_no_gender, Toast.LENGTH_SHORT).show();
            focus = manButton;
            cancel = true;
        } else if (TextUtils.isEmpty(month)) {
            textInputLayoutMonth.setError(getString(R.string.error_field_required));
            textInputLayoutMonth.setAnimation(animationShake);
            textInputLayoutMonth.startAnimation(animationShake);
            focus = userMonth;
            cancel = true;
        } else if (TextUtils.isEmpty(day)) {
            textInputLayoutDay.setError(getString(R.string.error_field_required));
            textInputLayoutDay.setAnimation(animationShake);
            textInputLayoutDay.startAnimation(animationShake);
            focus = userDay;
            cancel = true;
        } else if (TextUtils.isEmpty(age)) {
            textInputLayoutYear.setError(getString(R.string.error_field_required));
            textInputLayoutYear.setAnimation(animationShake);
            textInputLayoutYear.startAnimation(animationShake);
            focus = userYear;
            cancel = true;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            createUser(email, password);
            saveUserInformation(firstName, lastName, email, gender, calculateUserAge(), country);
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 15;
    }

    private int calculateUserAge() {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        return currentYear - Integer.parseInt(userYear.getText().toString());
    }

    private void setMyCountry() {
        String[] countriesName;
        countriesName = Locale.getISOCountries();
        ArrayList<CountryItem> list = new ArrayList<>();

        for (int i = 0; i < countriesName.length; i++) {
            Locale object = new Locale("", countriesName[i]);
            list.add(new CountryItem(object.getDisplayName() + " (" + countriesName[i] + ")"));
        }

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_sign_up_country);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.content_spinner, R.id.country_name, list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinner.getSelectedItem().toString();
                country = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}