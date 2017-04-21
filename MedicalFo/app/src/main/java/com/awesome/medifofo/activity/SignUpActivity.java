package com.awesome.medifofo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * Created by Eunsik on 2017-04-16.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public String gender, month;
    public EditText userEmail, userPassword;
    private Button manButton, womanButton;
    private ScrollView scrollView;
    private AutoCompleteTextView userFirstName, userLastName, userEmailView, userBirth;

    public static String sharedPreferenceFile = "userSignUpFILE";
    private FirebaseAuth firebaseAuth;

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
        userBirth = (AutoCompleteTextView) findViewById(R.id.user_year);

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
                    gender = "Male";

                } else {
                    manButton.setSelected(false);
                    manButton.setTextColor(Color.parseColor("#999999"));
                    gender = null;
                }

                break;

            case R.id.user_woman:
                // woman 버튼을 누른다면..
                if (!womanButton.isSelected()) {
                    womanButton.setSelected(true);
                    womanButton.setTextColor(Color.BLACK);
                    manButton.setSelected(false);
                    manButton.setTextColor(Color.parseColor("#999999"));
                    gender = "Female";

                } else {
                    womanButton.setSelected(false);
                    womanButton.setTextColor(Color.parseColor("#999999"));
                    gender = null;
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

        userFirstName.setError(null);
        userLastName.setError(null);
        userEmailView.setError(null);
        userPassword.setError(null);
        userBirth.setError(null);

        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String email = userEmailView.getText().toString();
        String password = userPassword.getText().toString();
        String age = userBirth.getText().toString();

        boolean cancel = false;
        View focus = null;

        if (TextUtils.isEmpty(firstName)) {
            userFirstName.setError(getString(R.string.error_field_required));
            focus = userFirstName;
            cancel = true;
        } else if (TextUtils.isEmpty(lastName)) {
            userLastName.setError(getString(R.string.error_field_required));
            focus = userLastName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            userEmailView.setError(getString(R.string.error_field_required));
            focus = userEmailView;
            cancel = true;
        } else if (!this.isEmailValid(email)) {
            userEmailView.setError(getString(R.string.error_invalid_email));
            focus = userEmailView;
            cancel = true;
        } else if (!this.isPasswordValid(password)) {
            userPassword.setError(getString(R.string.error_invalid_password));
            focus = userPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(password) && isEmailValid(email)) {
            userPassword.setError(getString(R.string.error_field_required));
            focus = userPassword;
            cancel = true;
        } else if (!manButton.isSelected() && !womanButton.isSelected()) {
            Toast.makeText(getApplication(),
                    "Please press one of the men and women.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(age)) {
            userBirth.setError(getString(R.string.error_field_required));
            focus = userBirth;
            cancel = true;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            createUser(email, password);
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferenceFile, 2);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("FIRSTNAME", firstName);
            editor.putString("LASTNAME", lastName);
            editor.putString("AGE", String.valueOf(calculateUserAge()));
            editor.apply();
            startActivity(intent);

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 15;
    }

    private int calculateUserAge() {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        return currentYear - Integer.parseInt(userBirth.getText().toString());
    }

    private void setMyCountry() {
        String[] countriesName;
        countriesName = Locale.getISOCountries();
        ArrayList<CountryItem> list = new ArrayList<>();

        for (int i = 0; i < countriesName.length; i++) {
            Locale object = new Locale("", countriesName[i]);
            list.add(new CountryItem(object.getDisplayName() + " (" + countriesName[i] + ")"));
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner_sign_up_country);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.content_spinner, R.id.country_name, list);
        spinner.setAdapter(adapter);
    }

}