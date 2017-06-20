package com.awesome.medifofo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View markerView;
        private final View markerContents;

        CustomInfoWindowAdapter() {
            markerView = getLayoutInflater().inflate(R.layout.content_info_window, null);
            markerContents = getLayoutInflater().inflate(R.layout.content_info_window_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, markerView);
            return markerView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, markerContents);
            return markerContents;
        }

        private void render(Marker marker, View view) {
            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.marker_title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.marker_snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

    private GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient = null;
    private double latitude, longitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private static final int PLACE_PICKER_REQUEST = 1;

    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button currentLocationButton = (Button) findViewById(R.id.button_search_place);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, SearchLocationActivity.class));
            }
        });


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
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

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
            }
        });


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
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 5000);
        googlePlacesUrl.append("&type=" + nearbyPlace);
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
        markerOptions.title("Current Position");
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

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return false;
    }
}
