package com.awesome.medifofo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

/**
 * Created by Eunsik on 2017-04-05.
 */

public class PersonalInputActivity extends AppCompatActivity {

    private EditText year, month, day;
    public static String userName, userGender, userAge;
    private String[] countriesName;
    //TODO: R.draw.sp, zg
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

    public static String sfYear = "userYear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_input);

        setMyCountry();
        setPersonalInfo();
        moveToMainActivity();
    }

    @Override
    protected void onStop(){
        super.onStop();
        calculateUserAge();
        SharedPreferences sharedPreferences = getSharedPreferences(sfYear, 1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("YEAR", userAge);
        editor.commit();
    }

    private void setMyCountry() {
        countriesName = Locale.getISOCountries();
        ArrayList<CountryItem> list = new ArrayList<>();

        for (int i = 0; i < countriesName.length; i++) {
            Locale object = new Locale("", countriesName[i]);
            list.add(new CountryItem(object.getDisplayName() + " (" + countriesName[i] + ")", flagData[i]));
        }

        Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.layout_spinner, R.id.country_name, list);
        spinner.setAdapter(adapter);
    }

    private void moveToMainActivity() {
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = (EditText) findViewById(R.id.year);
                year.setNextFocusDownId(R.id.month);
                month = (EditText) findViewById(R.id.month);
                month.setNextFocusDownId(R.id.day);
                day = (EditText) findViewById(R.id.day);

                // User Input Exception
                if ((year.getText().toString().equals("")) && (year.getText().toString().length() != 4)) {
                    year.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please check 'the year' field again.", Toast.LENGTH_LONG).show();
                } else if (month.getText().toString().equals("") && (month.getText().length() != 2)) {
                    month.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please check 'the month' field again.", Toast.LENGTH_LONG).show();
                } else if (day.getText().toString().equals("") && (day.getText().length() != 2)) {
                    day.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please check 'the day' field again.", Toast.LENGTH_LONG).show();
                } else {
                    calculateUserAge();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void setPersonalInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.sharedPreferenceFile, 0);
        userName = sharedPreferences.getString("NAME", "");
        userGender = sharedPreferences.getString("GENDER", "");

        TextView name = (TextView) findViewById(R.id.userId);
        name.setText(userName);

        TextView gender = (TextView) findViewById(R.id.gender);
        gender.setText(userGender);
    }

    private void calculateUserAge(){
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int userYear = currentYear - Integer.parseInt(year.getText().toString());
        userAge = String.valueOf(userYear);
    }

}
