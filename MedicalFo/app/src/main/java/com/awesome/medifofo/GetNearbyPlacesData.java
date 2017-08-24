package com.awesome.medifofo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eunsik on 2017-06-19.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String data;
    private GoogleMap mMap;
    private String url;
    private Context context;
    public static List<String> placeDetailsList = new ArrayList<String>();


    public GetNearbyPlacesData(Context c) {
        this.context = c;
    }

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

        PlaceDataParser parser = new PlaceDataParser();

        Log.d("PlaceReadTask", "onPostExecute Exit");
    }


    private String getURL(String place_id) {
        StringBuilder googlePlacesDetailUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesDetailUrl.append("placeid=").append(place_id);
        googlePlacesDetailUrl.append("&key=").append("AIzaSyCCTe--IEn-XC2SQ8aU21TvPo6U4YKj4zk");
        Log.d("getUrlDetails", googlePlacesDetailUrl.toString());
        return (googlePlacesDetailUrl.toString());
    }

    private class PlacesDetailData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("DATA", "doInBackground entered");
                url = urls[0];
                DownloadURL downloadURL = new DownloadURL();
                data = downloadURL.readUrl(url);
                Log.d("PlaceDetailsReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("PlaceDetailsReadTask", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("PlaceDetailReadTask", "onPostExecute Entered");
            HashMap<String, String> placeDetail = null;
            PlaceDataDetailsParser parser = new PlaceDataDetailsParser();
            placeDetail = parser.parse(result);
            placeDetailsList.add(printPlaceDetails(placeDetail));
            Log.d("PlaceDetailReadTask", "onPostExecute Exit");

        }

        private String printPlaceDetails(HashMap<String, String> placeDetails) {

            Log.d("PlacesDetails", "printPlaceDetails Entered");
            String placeName = placeDetails.get("place_name");
            String vicinity = placeDetails.get("vicinity");

            String rating = placeDetails.get("rating");
            if (rating.isEmpty()) {
                rating = "unknown";
            }
            String reviews = placeDetails.get("reviews");
            if (placeDetails.get("reviews").isEmpty()) {
                reviews = "unknown";
            }
            String monday = placeDetails.get("monday");
            if (monday.isEmpty()) {
                monday = "unknown";
            }
            String tuesday = placeDetails.get("tuesday");
            if (tuesday.isEmpty()) {
                tuesday = "unknown";
            }
            String wednesday = placeDetails.get("wednesday");
            if (wednesday.isEmpty()) {
                wednesday = "unknown";
            }
            String thursday = placeDetails.get("thursday");
            if (thursday.isEmpty()) {
                thursday = "unknown";
            }
            String friday = placeDetails.get("friday");
            if (friday.isEmpty()) {
                friday = "unknown";
            }
            String saturday = placeDetails.get("saturday");
            if (saturday.isEmpty()) {
                saturday = "unknown";
            }
            String sunday = placeDetails.get("sunday");
            if (sunday.isEmpty()) {
                sunday = "unknown";
            }
            String formatted_phone_number = placeDetails.get("formatted_phone_number");
            if (formatted_phone_number.isEmpty()) {
                formatted_phone_number = "unknown";
            }

            return placeName + "%" + vicinity + "%" + rating + "%" + reviews + "%" + formatted_phone_number + "%" + monday + "%" + tuesday + "%" + wednesday + "%" + thursday + "%" + friday + "%" + saturday + "%" + sunday;
        }
    }

}