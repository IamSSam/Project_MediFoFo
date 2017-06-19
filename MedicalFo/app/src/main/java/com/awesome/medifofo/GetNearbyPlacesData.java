package com.awesome.medifofo;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Eunsik on 2017-06-19.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String data;
    private GoogleMap mMap;
    private String url;

    @Override
    protected String doInBackground(Object... params) {

        try {
            Log.d("DATA", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadURL downloadURL = new DownloadURL();
            data = downloadURL.readUrl(url);
            Log.d("PlaceReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("PlaceReadTask", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("PlaceReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlaceList = null;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(result);
        showNearbyPlaces(nearbyPlaceList);
        Log.d("PlaceReadTask", "onPostExecute Exit");
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearByPlacesList) {
        for (int i = 0; i < nearByPlacesList.size(); i++) {
            Log.d("PlaceReadTask", "showNearbyPlaces Entered");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> place = nearByPlacesList.get(i);
            double latitude = Double.parseDouble(place.get("latitude"));
            double longitude = Double.parseDouble(place.get("longitude"));
            String placeName = place.get("place_name");
            String vicinity = place.get("vicinity");
            LatLng latLng = new LatLng(latitude, longitude);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }
}
