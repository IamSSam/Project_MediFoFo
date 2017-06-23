package com.awesome.medifofo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
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

    public static class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View markerView;
        private final View markerContents;
        private Activity currentActivity;

        public CustomInfoWindowAdapter(Activity activity) {
            currentActivity = activity;
            markerView = currentActivity.getLayoutInflater().inflate(R.layout.content_info_window, null);
            markerContents = currentActivity.getLayoutInflater().inflate(R.layout.content_info_window_contents, null);
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
            if (snippet != null) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

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
            String placeID = place.get("place_id");
            String placeName = place.get("place_name");
            String vicinity = place.get("vicinity");
            LatLng latLng = new LatLng(latitude, longitude);
            markerOptions.position(latLng)
                    .title(placeName)
                    .snippet(vicinity)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

            String placeDetailsQuery = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID +
                    "&key=AIzaSyCCTe--IEn-XC2SQ8aU21TvPo6U4YKj4zk";

        }
    }


}
