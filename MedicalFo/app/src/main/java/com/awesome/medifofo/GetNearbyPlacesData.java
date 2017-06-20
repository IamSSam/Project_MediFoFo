package com.awesome.medifofo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
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
    private Context context;

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
        List<HashMap<String, String>> nearbyPlaceList = null;
        PlaceDataParser parser = new PlaceDataParser();
        nearbyPlaceList = parser.parse(result);
        showNearbyPlaces(nearbyPlaceList);
        Log.d("PlaceReadTask", "onPostExecute Exit");
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearByPlacesList) {

        Drawable markerIcon = ContextCompat.getDrawable(context, R.drawable.ic_local_hospital);
        Bitmap bitmap = Bitmap.createBitmap(markerIcon.getIntrinsicWidth(),
                markerIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        markerIcon.draw(canvas);

        for (int i = 0; i < nearByPlacesList.size(); i++) {
            Log.d("PlaceReadTask", "showNearbyPlaces Entered");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> place = nearByPlacesList.get(i);
            double latitude = Double.parseDouble(place.get("lat"));
            double longitude = Double.parseDouble(place.get("lng"));
            String placeName = place.get("place_name");
            String vicinity = place.get("vicinity");
            LatLng latLng = new LatLng(latitude, longitude);
            markerOptions.position(latLng)
                    .title(placeName + " : " + vicinity)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }


}
