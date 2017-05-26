package com.awesome.medifofo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class FindHospitalActivity extends NMapActivity {

    private static final boolean DEBUG = false;

    private NMapView mMapView;
    private NMapController mMapController;
    private static final String CLIENT_ID = "7rxySx5aIRXRGtWXULIS";
    private static final String CLIENT_SECRET = "JGkB8R8zo5";
    private NMapMyLocationOverlay mMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;
    private MapContainerView mMapContainerView;
    private NMapOverlayManager mOverlayManager;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    int markerId = NMapPOIflagType.PIN;
    private static String[] hospitalTitle, hospitalGeocodeX, hospitalGeocodeY;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = new NMapView(this);

        mMapView.setClientId(CLIENT_ID);
        setContentView(mMapView);

        mMapView.setClickable(true);
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

        mMapController = mMapView.getMapController();

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        // compass manager
        mMapCompassManager = new NMapCompassManager(this);

        // create my location overlay
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
        //startMyLocation();
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

        String keyword = "정형외과";
        try {
            String location = URLEncoder.encode(keyword, "UTF-8");
            new HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_nmap_search.php?location=" + location);
            showPOIData(hospitalTitle.length);
        } catch (Exception e) {
            e.getMessage();
        }

    }


    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

            double longitude = myLocation.getLongitude();
            double latitude = myLocation.getLatitude();

            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

            // stop location updating
            //			Runnable runnable = new Runnable() {
            //				public void run() {
            //					stopMyLocation();
            //				}
            //			};
            //			runnable.run();

            Toast.makeText(FindHospitalActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(FindHospitalActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

            stopMyLocation();
        }

    };

    /* MapView State Change Listener*/
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.
                //restoreInstanceState();
                mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);

            } else { // fail
                Log.e("NMapViewer", "onFailedToInitializeWithError: " + errorInfo.toString());

                Toast.makeText(FindHospitalActivity.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            if (DEBUG) {
                Log.i("NMapViewer", "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            if (DEBUG) {
                Log.i("NMapViewer", "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (DEBUG) {
                Log.i("NMapViewer", "onZoomLevelChange: level=" + level);
            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {

        }
    };


    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLongPressCanceled(NMapView mapView) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {

        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
        }

        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

    };

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i("TAG", "onCalloutClick: title=" + item.getTitle());
            }

            // [[TEMP]] handle a click event of the callout
            Toast.makeText(FindHospitalActivity.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                if (item != null) {
                    Log.i("TAG", "onFocusChanged: " + item.toString());
                } else {
                    Log.i("TAG", "onFocusChanged: ");
                }
            }
        }
    };

    private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
                                                         Rect itemBounds) {

            // handle overlapped items
            if (itemOverlay instanceof NMapPOIdataOverlay) {
                NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay) itemOverlay;

                // check if it is selected by touch event
                if (!poiDataOverlay.isFocusedBySelectItem()) {
                    int countOfOverlappedItems = 1;

                    NMapPOIdata poiData = poiDataOverlay.getPOIdata();
                    for (int i = 0; i < poiData.count(); i++) {
                        NMapPOIitem poiItem = poiData.getPOIitem(i);

                        // skip selected item
                        if (poiItem == overlayItem) {
                            continue;
                        }

                        // check if overlapped or not
                        if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                            countOfOverlappedItems++;
                        }
                    }

                    if (countOfOverlappedItems > 1) {
                        String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
                        Toast.makeText(FindHospitalActivity.this, text, Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
            }

            // use custom old callout overlay
            if (overlayItem instanceof NMapPOIitem) {
                NMapPOIitem poiItem = (NMapPOIitem) overlayItem;

                if (poiItem.showRightButton()) {
                    return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mMapViewerResourceProvider);
                }
            }

            // use custom callout overlay
            return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

            // set basic callout overlay
            //return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
        }

    };

    private void startMyLocation() {

        if (mMyLocationOverlay != null) {
            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
                mOverlayManager.addOverlay(mMyLocationOverlay);
            }

            if (mMapLocationManager.isMyLocationEnabled()) {

                if (!mMapView.isAutoRotateEnabled()) {
                    mMyLocationOverlay.setCompassHeadingVisible(true);
                    mMapView.setAutoRotateEnabled(true, false);
                    mMapContainerView.requestLayout();
                } else {
                    stopMyLocation();
                }

                mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(FindHospitalActivity.this, "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goToSettings);

                    return;
                }
            }
        }
    }

    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapView.setAutoRotateEnabled(false, false);

                mMapContainerView.requestLayout();
            }
        }
    }


    @Override
    protected void onStop() {
        stopMyLocation();
        super.onStop();
    }


    /**
     * Container view class to rotate map view.
     */
    private class MapContainerView extends ViewGroup {

        public MapContainerView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            final int width = getWidth();
            final int height = getHeight();
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);
                final int childWidth = view.getMeasuredWidth();
                final int childHeight = view.getMeasuredHeight();
                final int childLeft = (width - childWidth) / 2;
                final int childTop = (height - childHeight) / 2;
                view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            }

            if (changed) {
                mOverlayManager.onSizeChanged(width, height);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            int sizeSpecWidth = widthMeasureSpec;
            int sizeSpecHeight = heightMeasureSpec;

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);

                if (view instanceof NMapView) {
                    if (mMapView.isAutoRotateEnabled()) {
                        int diag = (((int) (Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
                        sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
                        sizeSpecHeight = sizeSpecWidth;
                    }
                }

                view.measure(sizeSpecWidth, sizeSpecHeight);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {

            dialog = ProgressDialog.show(FindHospitalActivity.this, "", "Loading...");
            dialog.setCancelable(true);
        }


        @Override
        protected String doInBackground(String... urls) {
            String apiURL = urls[0];
            try {

                URL url = new URL(apiURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
                connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);
                int requestCode = connection.getResponseCode();
                BufferedReader br;
                if (requestCode == 200) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
                }
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                Log.d("RESPONE: ", response.toString());
                return response.toString();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("MESSAGE", result);

            try {
                dialog.dismiss();
                String jsonFormattedString = result.replaceAll("\\\\n\\\\", "");
                jsonFormattedString = jsonFormattedString.replaceAll("\\\\n", "");
                jsonFormattedString = jsonFormattedString.replaceAll("\\\\", "");
                jsonFormattedString = jsonFormattedString.replaceAll("\"\\{", "{");
                jsonFormattedString = jsonFormattedString.replaceAll("\\}\"", "}");
                jsonFormattedString = jsonFormattedString.replaceAll("\"n", "\"");
                Log.d("FORMAT: ", jsonFormattedString);
                JSONObject object = new JSONObject(jsonFormattedString);
                JSONArray array = object.getJSONArray("items");
                Log.d("JSONARRAY: ", array.toString());

                hospitalTitle = new String[array.length()];

                for (int i = 0; i < array.length(); i++) {
                    hospitalTitle[i] = array.getJSONObject(i).getString("title");
                    Log.d("[" + i + "]: ", hospitalTitle[i]);
                    String address = array.getJSONObject(i).getString("roadAddress");
                    new GeocodeAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_nmap_geocode.php?address=" + address);

                }

            } catch (JSONException e) {
                Log.e("Error: ", e.toString());
            }
        }
    }

    private class GeocodeAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String apiURL = urls[0];
            try {

                URL url = new URL(apiURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
                connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);
                int requestCode = connection.getResponseCode();
                BufferedReader br;
                if (requestCode == 200) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
                }
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                Log.d("RESPONE: ", response.toString());
                return response.toString();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String jsonFormattedString = result.replaceAll("\\\\n", "");
                jsonFormattedString = jsonFormattedString.replaceAll("\\\\", "");
                jsonFormattedString = jsonFormattedString.replaceAll("\"\\{", "{");
                jsonFormattedString = jsonFormattedString.replaceAll("\\}\"", "}");
                jsonFormattedString = jsonFormattedString.replaceAll("\"n", "\"");
                Log.d("GEOCODE: ", jsonFormattedString);
                JSONObject object = new JSONObject(jsonFormattedString).getJSONObject("result");
                JSONArray array = object.getJSONArray("items");


                hospitalGeocodeX = new String[array.length()];
                hospitalGeocodeY = new String[array.length()];

                for (int i = 0; i < array.length(); i++) {
                    String x = array.getJSONObject(i).getJSONObject("point").getString("x");
                    String y = array.getJSONObject(i).getJSONObject("point").getString("y");
                    hospitalGeocodeX[i] = x;
                    hospitalGeocodeY[i] = y;
                    Log.d("x[" + i + "] : ", hospitalGeocodeX[i]);
                    Log.d("y[" + i + "] : ", hospitalGeocodeY[i]);
                }


            } catch (JSONException e) {
                Log.e("Error: ", e.toString());
            }
        }
    }

    private void showPOIData(int length) {
        NMapPOIdata poiData = new NMapPOIdata(length, mMapViewerResourceProvider); // # of item
        poiData.beginPOIdata(length); // # of item

        for (int i = 0; i < length; i++) {
            poiData.addPOIitem(Double.parseDouble(hospitalGeocodeX[i]), Double.parseDouble(hospitalGeocodeY[i]), hospitalTitle[i], markerId, 0); // latitude, longitude, title
        }

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        poiDataOverlay.showAllPOIdata(0);

        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

}
