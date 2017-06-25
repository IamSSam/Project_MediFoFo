package com.awesome.medifofo.activity;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.medifofo.GetNearbyPlacesData;
import com.awesome.medifofo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient = null;
    private double latitude, longitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private static final int PLACE_PICKER_REQUEST = 1;
    SlidingUpPanelLayout panelLayout = null;

    LinearLayout markerDetailsLayout, markerInfoLayout;
    TextView markerTitle, markerVicinity, markerPhoneNumber, markerWeek, markerSaturday, markerSunday, markerRating;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker, nearByMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!checkGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button currentLocationButton = (Button) findViewById(R.id.button_search_place);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, SearchLocationActivity.class));
            }
        });

        panelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        markerDetailsLayout = (LinearLayout) findViewById(R.id.layout_marker_details);

    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        // The condition depends on Android version
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        Button hospitalButton = (Button) findViewById(R.id.button_hospital);
        hospitalButton.setOnClickListener(new View.OnClickListener() {

            final String HOSPITAL = "hospital";

            @Override
            public void onClick(View view) {
                Log.d("onClick", "Button is Clicked");
                mMap.clear();

                Log.d("Latlang: ", String.valueOf(latitude) + ", " + String.valueOf(longitude));

                String url = getUrl(latitude, longitude, HOSPITAL);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(getApplicationContext());
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(MapsActivity.this, "Nearby Hospital", Toast.LENGTH_LONG).show();

                mMap.setInfoWindowAdapter(new GetNearbyPlacesData.CustomInfoWindowAdapter(MapsActivity.this));

            }

        });

        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();

    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        Log.d("Latlang: ", String.valueOf(latitude) + ", " + String.valueOf(longitude));

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=" + 5000);
        googlePlacesUrl.append("&type=").append(nearbyPlace);
        googlePlacesUrl.append("&key=" + "AIzaSyCCTe--IEn-XC2SQ8aU21TvPo6U4YKj4zk");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("onConnected", "onConnected Entered");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, connectionResult.RESOLUTION_REQUIRED);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("TAG", "Location services connection failed with code==>" + connectionResult.getErrorCode());
            Log.e("TAG", "Location services connection failed Because of==> " + connectionResult.getErrorMessage());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are here ^^");
        markerOptions.alpha(0.7f);

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        Toast.makeText(MapsActivity.this, "Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        nearByMarker = marker;
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        List<String> markerDetailList = GetNearbyPlacesData.placeDetailsList;
        for (int i = 0; i < markerDetailList.size(); i++) {
            Log.d("markerDetailList", markerDetailList.get(i));
        }
        Log.d("For loop Entered Size", String.valueOf(markerDetailList.size()));

        String markerID = marker.getId().substring(1, marker.getId().length());
        int markerIndex = Integer.parseInt(markerID);
        Log.d("MarkerIndex", String.valueOf(markerIndex));

        Log.d("For loop Entered", markerDetailList.get(markerIndex - 1));

        String markerInfo[] = markerDetailList.get(markerIndex - 1).split("%");

        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        markerTitle = (TextView) findViewById(R.id.marker_details_title);
        markerTitle.setText(markerInfo[0]);
        //markerTitle.setText(marker.getId());

        markerVicinity = (TextView) findViewById(R.id.marker_details_vicinity);
        markerVicinity.setText(markerInfo[1]);

        markerRating = (TextView) findViewById(R.id.marker_details_rating);
        markerRating.setText("Average: " + markerInfo[2]);

        markerPhoneNumber = (TextView) findViewById(R.id.marker_details_phone_number);
        markerPhoneNumber.setText(markerInfo[4]);

        markerWeek = (TextView) findViewById(R.id.marker_details_week);
        if (!markerInfo[5].isEmpty()) {
            markerWeek.setText(markerInfo[5]);
        } else {
            markerWeek.setText("Unknown");
        }

        markerSaturday = (TextView) findViewById(R.id.marker_details_saturday);
        if (!markerInfo[10].isEmpty()) {
            markerSaturday.setText(markerInfo[10]);
        } else {
            markerSaturday.setText("Unknown");
        }

        markerSunday = (TextView) findViewById(R.id.marker_details_sunday);
        if (!markerInfo[11].isEmpty()) {
            markerSunday.setText(markerInfo[11]);
        } else {
            markerSunday.setText("Unknown");
        }

        panelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.toString().equals("EXPANDED")) {
                    int colorFrom = ContextCompat.getColor(getApplicationContext(), R.color.white);
                    int colorTo = ContextCompat.getColor(getApplicationContext(), R.color.light_blue_primary);

                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(250); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {

                            markerInfoLayout = (LinearLayout) findViewById(R.id.layout_marker_details_information);
                            markerInfoLayout.setBackgroundColor((int) animator.getAnimatedValue());

                            markerTitle.setTextColor(Color.WHITE);
                            markerVicinity.setTextColor(Color.WHITE);
                        }

                    });
                    colorAnimation.start();

                    markerDetailsLayout.setVisibility(View.VISIBLE);


                } else {
                    markerInfoLayout = (LinearLayout) findViewById(R.id.layout_marker_details_information);
                    markerInfoLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                    markerTitle.setTextColor(Color.BLACK);
                    markerVicinity.setTextColor(Color.DKGRAY);

                    markerDetailsLayout.setVisibility(View.GONE);
                }
                Log.i("PanelState", "onPanelStateChanged " + newState);

            }
        });

        return false;
    }


}
