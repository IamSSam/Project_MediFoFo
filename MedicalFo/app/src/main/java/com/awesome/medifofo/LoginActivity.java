package com.awesome.medifofo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Eunsik on 03/26/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    public static String publicName, publicGender, publicPictureURL, publicAge;
    private TextView userName, userGender;
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
            R.drawable.hr, R.drawable.ht, R.drawable.hu,R.drawable.id,  R.drawable.ie, R.drawable.il, R.drawable.im, R.drawable.in, R.drawable.io,
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
            R.drawable.vn, R.drawable.vu, R.drawable.wf, R.drawable.ws, R.drawable.ye, R.drawable.yt, R.drawable.za, R.drawable.zm,  R.drawable.zm, R.drawable.zw
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        facebookLogIn(callbackManager);
        setMyCountry();
        moveToMainActivity();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogIn(CallbackManager callbackManager) {
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            // TODO: resist auto login
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

                // Token, UserID
                Log.d("TAG", "Facebook Token : " + loginResult.getAccessToken().getToken());
                Log.d("TAG", "Facebook UserID : " + loginResult.getAccessToken().getUserId());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //Log.d("TAG", "로그인 결과: " + response.toString());
                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String gender = object.getString("gender");
                                    //String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    userName = (TextView) findViewById(R.id.userId);
                                    userName.setText(name.toString());
                                    publicName = name;

                                    userGender = (TextView) findViewById(R.id.gender);
                                    userGender.setText(gender.toString());
                                    publicGender = gender;

                                    //publicPictureURL = picture;
                                    publicPictureURL = "https://graph.facebook.com/" + id + "/picture?type=large";
                                    //Log.d("TAG", publicPictureURL);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                if (!loginResult.getAccessToken().getToken().isEmpty()) {
                    setLayerAfterLogin();
                }

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,picture");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "User cancel Login", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error : " + error, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void moveToMainActivity() {
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText year = (EditText) findViewById(R.id.year);
                Calendar current = Calendar.getInstance();
                int currentYear = current.get(Calendar.YEAR);
                int userYear = currentYear - Integer.parseInt(year.getText().toString());
                publicAge = String.valueOf(userYear);

                year.getNextFocusRightId();
                EditText month = (EditText) findViewById(R.id.month);
                month.getNextFocusRightId();
                EditText day = (EditText) findViewById(R.id.day);

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void setLayerAfterLogin() {
        // TODO:중앙으로 내려올 때 스무스한 애니메이션 적용하기
        LinearLayout informationContainer = (LinearLayout) findViewById(R.id.information_container);
        informationContainer.setVisibility(View.VISIBLE);
        TextView mainTitle = (TextView) findViewById(R.id.main_title);
        mainTitle.setVisibility(View.GONE);
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.button_container);
        buttonContainer.setVisibility(View.GONE);
        LinearLayout mainContainer = (LinearLayout) findViewById(R.id.main_container);
        mainContainer.setGravity(Gravity.CENTER_VERTICAL);
    }


    private void setMyCountry() {
        countriesName = Locale.getISOCountries();
        ArrayList<CountryItem> list = new ArrayList<>();

        for (int i = 0; i < countriesName.length; i++) {
            Locale object = new Locale("", countriesName[i]);
            list.add(new CountryItem(object.getDisplayName() + " ("+countriesName[i]+")", flagData[i]));
        }

        Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.layout_spinner, R.id.country_name, list);
        spinner.setAdapter(adapter);
    }

    /*
    private void logOutFromFacebook() {

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                }

            }
        };

    }*/

}