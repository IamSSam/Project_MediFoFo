package com.awesome.medifofo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.awesome.medifofo.R;

import com.awesome.medifofo.adapter.ExpandableAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchLocationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private static String TAG = "TAG";
    public static final int PLACE_REQUEST_PICKER = 1;
    private ArrayList<String> arrayGroup = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> arrayChild = new HashMap<String, ArrayList<String>>();
    private TextView nameOfDetail, addressOfDetail, phoneOfDetail, uriOfDetail, openingOfDetail, ratingOfDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.open_now);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        initView();
        setArrayData();

        expandableListView.setAdapter(new ExpandableAdapter(this, arrayGroup, arrayChild));

        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, PLACE_REQUEST_PICKER);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // ...
            e.printStackTrace();
        }

    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_location);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        nameOfDetail = (TextView) findViewById(R.id.marker_details_title);
        addressOfDetail = (TextView) findViewById(R.id.marker_details_address);
        phoneOfDetail = (TextView) findViewById(R.id.marker_details_phone_number);
        uriOfDetail = (TextView) findViewById(R.id.marker_details_uri);
        openingOfDetail = (TextView) findViewById(R.id.marker_details_is_opening);
        ratingOfDetail = (TextView) findViewById(R.id.marker_details_rating);
    }

    private void setArrayData() {
        arrayGroup.add("Opening Hours");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_REQUEST_PICKER) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                String placeId = place.getId();

                nameOfDetail.setText(place.getName());
                addressOfDetail.setText(place.getAddress());
                phoneOfDetail.setText(place.getPhoneNumber());
                uriOfDetail.setText(String.valueOf(place.getWebsiteUri()));
                if (place.getRating() == -1.0) {
                    ratingOfDetail.setText("There is no rating.");
                } else {
                    ratingOfDetail.setText(String.valueOf(place.getRating()));
                }

                StringBuilder googlePlacesDetailUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
                googlePlacesDetailUrl.append("placeid=").append(placeId);
                googlePlacesDetailUrl.append("&key=").append("AIzaSyCCTe--IEn-XC2SQ8aU21TvPo6U4YKj4zk");
                Log.d("getUrlDetails", googlePlacesDetailUrl.toString());

                new HttpAsyncTask().execute(String.valueOf(googlePlacesDetailUrl));
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchLocationActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(SearchLocationActivity.this, "Fail. Check your internet connection.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject jobj = new JSONObject(result);
                    Log.d("RESULT ", result);

                    if (!jobj.getString("status").equals("OK"))
                        Toast.makeText(SearchLocationActivity.this, "Server Error.", Toast.LENGTH_SHORT).show();
                    else {

                        //Toast.makeText(SearchLocationActivity.this, "Success.", Toast.LENGTH_SHORT).show();

                        String isOpening = jobj.getJSONObject("result").getJSONObject("opening_hours").getString("open_now");
                        JSONArray weekday_text = jobj.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("weekday_text");


                        if (isOpening.equals("true")) {
                            openingOfDetail.setText(getResources().getText(R.string.detail_open));
                            openingOfDetail.setTextColor(getResources().getColor(R.color.blue_primary));
                        } else {
                            openingOfDetail.setText(getResources().getText(R.string.detail_closed));
                            openingOfDetail.setTextColor(getResources().getColor(R.color.red_primary));
                        }


                        ArrayList<String> arrayOpenHour = new ArrayList<String>();
                        if (weekday_text.isNull(0)) {
                            arrayOpenHour.add("No information");
                        } else {
                            for (int i = 0; i < weekday_text.length(); i++)
                                arrayOpenHour.add(weekday_text.get(i).toString());

                            arrayChild.put(arrayGroup.get(0), arrayOpenHour);
                        }

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("name", person.getName());
            //jsonObject.accumulate("country", person.getCountry());
            //jsonObject.accumulate("twitter", person.getTwitter());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
            //nameValuePair.add(new BasicNameValuePair("email_id", userEmailView.getText().toString()));
            //nameValuePair.add(new BasicNameValuePair("platform", PLATFORM));
            //nameValuePair.add(new BasicNameValuePair("patient_name", patientName));

            // 5. set json to StringEntity
            //StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "utf-8"));

            // 7. Set some headers to inform server about the type of the content
            //httpPost.setHeader("Accept", "application/json");
            //httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            // Log.d("InputStream", e.getLocalizedMessage());
        }
        // Log.d("http", result);

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }


}