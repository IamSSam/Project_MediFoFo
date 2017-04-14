package com.awesome.medifofo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.awesome.medifofo.adapter.GridAdapter;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Eunsik on 03/26/2017.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gridView;
    private ImageView userPicture;
    private TextView userName, userAge;


    private DrawerLayout drawerLayout;

    private int image[] = {
            R.drawable.head, R.drawable.face, R.drawable.eye, R.drawable.nouse, R.drawable.ear, R.drawable.tongue,
            R.drawable.jaw, R.drawable.neck, R.drawable.breast, R.drawable.belly, R.drawable.back, R.drawable.spine,
            R.drawable.arm, R.drawable.elbow, R.drawable.hand, R.drawable.finger, R.drawable.leg,
            R.drawable.hip, R.drawable.ankle, R.drawable.sole, R.drawable.man, R.drawable.woman,
            R.drawable.digestive, R.drawable.respiratory, R.drawable.heart
    };

    final String[ /* For UI */][ /* For Naver Maps */] st_place = {
            {"머리", "내과"},
            {"얼굴", "외과"},
            {"식도", "내과"},
            {"가슴", ""},
            {"배", ""},
            {"등", "정형외과"},
            {"다리", "정형외과"},
            {"팔", "정형외과"},
            {"발", "정형외과"},
            {"위", "내과"},
            {"폐", "내과"},
            {"손", "외과"},
            {"간", "내과"},
            {"엉덩이", "비뇨기과"},
            {"턱", "내과"},
            {"치아", "치과"},
            {"생식기 (남자)", ""},
            {"생식기 (여자)", ""},
            {"목", "소아과"},
            {"코", "소아과"},
            {"발바닥", "피부과"},
            {"손가락", "정형외과"},
            {"혀", "내과"},
            {"척추", "정형외과"},
            {"귀", "이비인후과"},
            {"팔근육", "정형외과"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.sharedPreferenceFile, 0);
        userPicture = (ImageView) headerView.findViewById(R.id.navigation_my_picture);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(1000))
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage(sharedPreferences.getString("URL", "").toString(), userPicture, displayImageOptions);

        userName = (TextView) headerView.findViewById(R.id.navigation_my_name);
        userName.setText(sharedPreferences.getString("NAME", ""));

        userAge = (TextView) headerView.findViewById(R.id.navigation_my_age);
        if (PersonalInputActivity.userAge == null) {
            SharedPreferences sf = getSharedPreferences(PersonalInputActivity.sfYear, 1);
            userAge.setText(sf.getString("YEAR", "") + " years old");
        } else {
            userAge.setText(PersonalInputActivity.userAge + " years old");
        }


        final GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), R.layout.row, image);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.invalidateViews();
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                /*
                ((TextView) levelDialog.findViewById(R.id.evaluation_title)).setText(st_place[position][0]);
                currentPosition = position;
                levelDialog.show();
                */
                Intent intent = new Intent(getApplicationContext(), SymptomListActivity.class);
                intent.putExtra("POSITION", position); // Pass gridview's position
                startActivity(intent);
            }
        });

        if (AccessToken.getCurrentAccessToken() == null) {
            Toast.makeText(getApplicationContext(), "Please Login again", Toast.LENGTH_SHORT).show();
            goLoginActivity();
        }


        /*
        symptom_main_btn1 = (Button) levelDialog.findViewById(R.id.symptom_main_btn1);
        symptom_main_btn2 = (Button) levelDialog.findViewById(R.id.symptom_main_btn2);
        symptom_main_btn3 = (Button) levelDialog.findViewById(R.id.symptom_main_btn3);
        find_hospital = (Button) levelDialog.findViewById(R.id.find_hospital);

        more_confirm = (Button) moreDialog.findViewById(R.id.more_confirm);
        more_confirm.setOnClickListener(this);

        symptom_main_btn1.setOnClickListener(this);
        symptom_main_btn2.setOnClickListener(this);
        symptom_main_btn3.setOnClickListener(this);
        find_hospital.setOnClickListener(this);

        detail_symptom_cb1 = (CheckBox) moreDialog.findViewById(R.id.checkbox_btn1);
        detail_symptom_cb2 = (CheckBox) moreDialog.findViewById(R.id.checkbox_btn2);
        detail_symptom_cb3 = (CheckBox) moreDialog.findViewById(R.id.checkbox_btn3);

        tmp_st_comment = (EditText) moreDialog.findViewById(R.id.symptom_comment);
        tmp_st_scale = 10;

        */
        /* SeekBar 부분, 통증 선택하기 */
        /*
        final TextView levelTxt = (TextView) levelDialog.findViewById(R.id.level_txt);
        final SeekBar levelSeek = (SeekBar) levelDialog.findViewById(R.id.level_seek);

        levelSeek.setMax(10);
        levelSeek.setProgress(10);

        levelSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //change to progress
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                levelTxt.setText(Integer.toString(progress));
            }

            //methods to implement but not necessary to amend
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        */

        /*
        final TextView moreTxt = (TextView) moreDialog.findViewById(R.id.more_txt);
        final SeekBar moreSeek = (SeekBar) moreDialog.findViewById(R.id.more_seek);

        moreSeek.setMax(10);
        moreSeek.setProgress(10);

        moreSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //change to progress
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moreTxt.setText(Integer.toString(progress));
                tmp_st_scale = progress;
            }

            //methods to implement but not necessary to amend
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button okBtn = (Button) levelDialog.findViewById(R.id.level_cancel);
        Button goDialogBtn;

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //respond to level

                //int chosenLevel = levelSeek.getProgress();
                levelDialog.dismiss();
            }
        });

        level_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDialog.show();
            }
        });
        */


    }

    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.symptom_main_btn1:
                tmp_st_main = symptom_main_btn1.getText().toString();
                moreDialog.show();
                break;

            case R.id.symptom_main_btn2:
                tmp_st_main = symptom_main_btn2.getText().toString();
                moreDialog.show();
                break;

            case R.id.symptom_main_btn3:
                tmp_st_main = symptom_main_btn3.getText().toString();
                moreDialog.show();
                break;

            case R.id.more_confirm:
                levelDialog.dismiss();
                tmp_st_sub = new StringBuilder();
                tmp_st_sub.append(detail_symptom_cb1.isChecked() ? "true " : "false ");
                tmp_st_sub.append(detail_symptom_cb2.isChecked() ? "true " : "false ");
                tmp_st_sub.append(detail_symptom_cb3.isChecked() ? "true " : "false ");

                Person.st_main = tmp_st_main;
                Person.st_place = st_place[currentPosition][0];
                Person.st_scale = tmp_st_scale;
                Person.st_sub = tmp_st_sub.toString();
                Person.st_comment = tmp_st_comment.getText().toString();

                // = new PersonalSymptomFragment.HttpAsyncTask();
                //httpAsyncTask.execute("http://igrus.mireene.com/applogin/stchange.php");
                //moreDialog.dismiss();
                break;

            default:
                break;
        }

    }
    */

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
            //case R.id.main_about:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        goLoginActivity();
    }

    private void goLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent intent = new Intent();

        switch (item.getItemId()) {

            case R.id.navigation_phr:
                intent.setClass(this, PersonalInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_about:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_share:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_feedback:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_settings:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_logout:
                this.logOut();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
