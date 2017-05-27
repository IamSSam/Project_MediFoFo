package com.awesome.medifofo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.awesome.medifofo.FindHospital;
import com.awesome.medifofo.FindHospitalActivity;
import com.awesome.medifofo.R;
import com.awesome.medifofo.adapter.GridAdapter;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/*
 * Created by Eunsik on 03/26/2017.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GridView gridView;
    private ImageView userPicture;
    private TextView userName, userAge;
    private FirebaseAuth firebaseAuth;
    private AccessToken accessToken;

    private DrawerLayout drawerLayout;

    private int image_man[] = {
            R.drawable.head, R.drawable.face, R.drawable.eye, R.drawable.nouse, R.drawable.ear, R.drawable.tongue,
            R.drawable.jaw, R.drawable.neck, R.drawable.breast, R.drawable.belly, R.drawable.back, R.drawable.spine,
            R.drawable.arm, R.drawable.elbow, R.drawable.hand, R.drawable.finger, R.drawable.leg,
            R.drawable.hip, R.drawable.ankle, R.drawable.sole, R.drawable.man, R.drawable.teeth,
            R.drawable.digestive, R.drawable.respiratory, R.drawable.heart
    };

    private int image_woman[] = {
            R.drawable.head, R.drawable.face, R.drawable.eye, R.drawable.nouse, R.drawable.ear, R.drawable.tongue,
            R.drawable.jaw, R.drawable.neck, R.drawable.breast, R.drawable.belly, R.drawable.back, R.drawable.spine,
            R.drawable.arm, R.drawable.elbow, R.drawable.hand, R.drawable.finger, R.drawable.leg,
            R.drawable.hip, R.drawable.ankle, R.drawable.sole, R.drawable.woman, R.drawable.teeth,
            R.drawable.digestive, R.drawable.respiratory, R.drawable.heart
    };

    final String[ /* For UI */][ /* For Naver Maps */] st_place = {
            {"머리", "내과"},
            {"얼굴", "외과"},
            {"눈", "안과"},
            {"코", "이비인후과"},
            {"귀", "이비인후과"},
            {"혀", "내과"},
            {"턱", "내과"},
            {"목", "소아과"},
            {"가슴", ""},
            {"배", "내과"},
            {"등", "정형외과"},
            {"척추", "정형외과"},
            {"팔", "정형외과"},
            {"팔꿈치", "정형외과"},
            {"손", "외과"},
            {"손가락", "정형외과"},
            {"다리", "정형외과"},
            {"발", "정형외과"},
            {"엉덩이", "비뇨기과"},
            {"발바닥", "피부과"},
            {"생식기 (남자)", "비교기과"},
            {"생식기 (여자)", "산부인과"},
            {"이빨", "치과"},
            {"위", "내과"},
            {"폐", "내과"},
            {"심장", "내과"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadUserInformation();

        if (AccessToken.getCurrentAccessToken() == null && firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(getApplicationContext(), "Please Login again", Toast.LENGTH_SHORT).show();
            goFirstActivity();
        }
    }

    public void initView() {

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

        userPicture = (ImageView) headerView.findViewById(R.id.navigation_my_picture);
        userName = (TextView) headerView.findViewById(R.id.navigation_my_name);
        userAge = (TextView) headerView.findViewById(R.id.navigation_my_age);
        firebaseAuth = FirebaseAuth.getInstance();
        accessToken = AccessToken.getCurrentAccessToken();
        final GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), R.layout.row, image_man);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.invalidateViews();
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getApplicationContext(), SymptomListActivity.class);
                intent.putExtra("POSITION", position); // Pass gridView position
                startActivity(intent);
            }
        });
    }

    public void loadUserInformation() {

        if (firebaseAuth.getCurrentUser() != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("userSignUpFILE", Activity.MODE_PRIVATE);
            userPicture.setBackground(getDrawable(R.drawable.ic_no_image));
            userName.setText(sharedPreferences.getString("FIRSTNAME", "") + " " + sharedPreferences.getString("LASTNAME", ""));
            userAge.setText(sharedPreferences.getInt("AGE", 0) + " years old");
        }

        if (accessToken != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("userFacebookFILE", Activity.MODE_PRIVATE);
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(1000))
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            imageLoader.displayImage(sharedPreferences.getString("URL", ""), userPicture, displayImageOptions);

            userName.setText(sharedPreferences.getString("NAME", ""));

            SharedPreferences sf = getSharedPreferences("userAGE", Activity.MODE_PRIVATE);
            userAge.setText(sharedPreferences.getString("AGE", "") + " years old");

        }
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
            //case R.id.main_about:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void facebookLogOut() {
        LoginManager.getInstance().logOut();
        goFirstActivity();
    }

    private void firebaseLogOut() {
        firebaseAuth.signOut();
        goFirstActivity();
    }

    private void goFirstActivity() {
        startActivity(new Intent(this, FacebookLoginActivity.class));
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {

            case R.id.navigation_about:
                intent.setClass(this, FacebookLoginActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_qna:
                intent.setClass(this, PersonalHealthRecordActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_phr:
                intent.setClass(this, FacebookLoginActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_feedback:
                requestLocationPermission();
                break;

            case R.id.navigation_settings:
                break;

            case R.id.navigation_logout:
                if (firebaseAuth.getCurrentUser() != null) {
                    this.firebaseLogOut();
                    finish();
                }
                if (AccessToken.getCurrentAccessToken() != null) {
                    this.facebookLogOut();
                    finish();
                }
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void requestLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            FindHospital findHospital = new FindHospital();
            findHospital.initiateFindHospital(MainActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FindHospital findHospital = new FindHospital();
                    findHospital.initiateFindHospital(MainActivity.this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "동의하셔야 이용가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
