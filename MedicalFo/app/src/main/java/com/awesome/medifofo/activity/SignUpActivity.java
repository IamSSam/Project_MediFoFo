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

    private static Integer[] flagData = {R.drawable.ad, R.drawable.ae, R.drawable.af, R.drawable.ag, R.drawable.ai, R.drawable.al, R.drawable.am,
            R.drawable.ao, R.drawable.aq, R.drawable.ar, R.drawable.as, R.drawable.at, R.drawable.au, R.drawable.aw, R.drawable.ax, R.drawable.az,
            R.drawable.ba, R.drawable.bb, R.drawable.bd, R.drawable.be, R.drawable.bf, R.drawable.bg, R.drawable.bh, R.drawable.bi, R.drawable.bj,
            R.drawable.fr, R.drawable.bm, R.drawable.bn, R.drawable.bo, R.drawable.nl, R.drawable.br, R.drawable.bs, R.drawable.bt, R.drawable.bv,
            R.drawable.bw, R.drawable.by, R.drawable.bz, R.drawable.ca, R.drawable.cc, R.drawable.cd, R.drawable.cf, R.drawable.cg, R.drawable.ch,
            R.drawable.ci, R.drawable.ck, R.drawable.cl, R.drawable.cm, R.drawable.cn, R.drawable.co, R.drawable.cr, R.drawable.cu, R.drawable.cv,
            R.drawable.cw, R.drawable.cx, R.drawable.cy, R.drawable.cz, R.drawable.de, R.drawable.dj, R.drawable.dk, R.drawable.dm, R.drawable.doe,
            R.drawable.dz, R.drawable.ec, R.drawable.ee, R.drawable.eg, R.drawable.eh, R.drawable.er, R.drawable.es, R.drawable.et, R.drawable.fi,
            R.drawable.fj, R.drawable.fk, R.drawable.fm, R.drawable.fo, R.drawable.fr, R.drawable.ga, R.drawable.gb, R.drawable.gd, R.drawable.ge,
            R.drawable.gf, R.drawable.gg, R.drawable.gh, R.drawable.gi, R.drawable.gl, R.drawable.gm, R.drawable.gn, R.drawable.gp, R.drawable.gq,
            R.drawable.gr, R.drawable.gs, R.drawable.gt, R.drawable.gu, R.drawable.gw, R.drawable.gy, R.drawable.hk, R.drawable.hm, R.drawable.hn,
            R.drawable.hr, R.drawable.ht, R.drawable.hu, R.drawable.id, R.drawable.ie, R.drawable.il, R.drawable.im, R.drawable.in, R.drawable.io,
            R.drawable.iq, R.drawable.ir, R.drawable.is, R.drawable.it, R.drawable.je, R.drawable.jm, R.drawable.jo, R.drawable.jp, R.drawable.ke,
            R.drawable.kg, R.drawable.kh, R.drawable.ki, R.drawable.km, R.drawable.kn, R.drawable.kp, R.drawable.kr, R.drawable.kw, R.drawable.ky,
            R.drawable.kz, R.drawable.la, R.drawable.lb, R.drawable.lc, R.drawable.li, R.drawable.lk, R.drawable.lr, R.drawable.ls, R.drawable.lt,
            R.drawable.lu, R.drawable.lv, R.drawable.ly, R.drawable.ma, R.drawable.mc, R.drawable.md, R.drawable.me, R.drawable.fr, R.drawable.mg,
            R.drawable.mh, R.drawable.mk, R.drawable.ml, R.drawable.mm, R.drawable.mn, R.drawable.mo, R.drawable.mp, R.drawable.mq, R.drawable.mr,
            R.drawable.ms, R.drawable.mt, R.drawable.mu, R.drawable.mv, R.drawable.mw, R.drawable.mx, R.drawable.my, R.drawable.mz, R.drawable.na,
            R.drawable.nc, R.drawable.ne, R.drawable.nf, R.drawable.ng, R.drawable.ni, R.drawable.nl, R.drawable.no, R.drawable.np, R.drawable.nr,
            R.drawable.nu, R.drawable.nz, R.drawable.om, R.drawable.pa, R.drawable.pe, R.drawable.pf, R.drawable.pg, R.drawable.ph, R.drawable.pk,
            R.drawable.pl, R.drawable.pm, R.drawable.pn, R.drawable.pr, R.drawable.ps, R.drawable.pt, R.drawable.pw, R.drawable.py, R.drawable.qa,
            R.drawable.re, R.drawable.ro, R.drawable.rs, R.drawable.ru, R.drawable.rw, R.drawable.sa, R.drawable.sb, R.drawable.sc, R.drawable.sd,
            R.drawable.se, R.drawable.sg, R.drawable.sh, R.drawable.si, R.drawable.sj, R.drawable.sk, R.drawable.sl, R.drawable.sm, R.drawable.sn,
            R.drawable.so, R.drawable.so, R.drawable.sr, R.drawable.ss, R.drawable.st, R.drawable.sv, R.drawable.fr, R.drawable.sy, R.drawable.sz,
            R.drawable.tc, R.drawable.td, R.drawable.tf, R.drawable.tg, R.drawable.th, R.drawable.tj, R.drawable.tk, R.drawable.tl, R.drawable.tm,
            R.drawable.tn, R.drawable.to, R.drawable.tr, R.drawable.tt, R.drawable.tv, R.drawable.tw, R.drawable.tz, R.drawable.ua, R.drawable.ug,
            R.drawable.um, R.drawable.us, R.drawable.uy, R.drawable.uz, R.drawable.va, R.drawable.vc, R.drawable.ve, R.drawable.vg, R.drawable.vi,
            R.drawable.vn, R.drawable.vu, R.drawable.wf, R.drawable.ws, R.drawable.ye, R.drawable.yt, R.drawable.za, R.drawable.zm, R.drawable.zm, R.drawable.zw
    };

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
            list.add(new CountryItem(object.getDisplayName() + " (" + countriesName[i] + ")", flagData[i]));
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner_sign_up_country);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.content_spinner, R.id.country_name, list);
        spinner.setAdapter(adapter);
    }

}