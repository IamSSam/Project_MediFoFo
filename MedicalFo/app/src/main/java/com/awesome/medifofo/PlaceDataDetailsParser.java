package com.awesome.medifofo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Eunsik on 2017-06-25.
 */

public class PlaceDataDetailsParser {
    public HashMap<String, String> parse(String jsonData) {
        JSONObject object = null;

        try {
            Log.d("Places", "parse");
            object = new JSONObject((String) jsonData).getJSONObject("result");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }

        return getPlaceDetails(object);

    }

    private HashMap<String, String> getPlaceDetails(JSONObject googlePlaceDetailsJson) {
        HashMap<String, String> googlePlaceDetails = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String rating = "";
        String reviews = "";
        String formatted_phone_number = "";
        String monday = "";
        String tuesday = "";
        String wednesday = "";
        String thursday = "";
        String friday = "";
        String saturday = "";
        String sunday = "";

        Log.d("getPlaceDetails", "Entered");

        try {
            if (!googlePlaceDetailsJson.isNull("name")) {
                placeName = googlePlaceDetailsJson.getString("name");
            }
            if (!googlePlaceDetailsJson.isNull("vicinity")) {
                vicinity = googlePlaceDetailsJson.getString("vicinity");
            }
            if (!googlePlaceDetailsJson.isNull("rating")) {
                rating = googlePlaceDetailsJson.getString("rating");
            }
            if (!googlePlaceDetailsJson.isNull("reviews")) {
                reviews = googlePlaceDetailsJson.getString("reviews");
            }
            if (!googlePlaceDetailsJson.isNull("formatted_phone_number")) {
                formatted_phone_number = googlePlaceDetailsJson.getString("formatted_phone_number");
            }

            if (!googlePlaceDetailsJson.isNull("opening_hours")) {
                monday = googlePlaceDetailsJson.getJSONObject("opening_hours").getJSONArray("weekday_text").get(0).toString();
                tuesday = googlePlaceDetailsJson.getJSONObject("opening_hours").getJSONArray("weekday_text").get(1).toString();
                wednesday = googlePlaceDetailsJson.getJSONObject("opening_hours").getJSONArray("weekday_text").get(2).toString();
                thursday = googlePlaceDetailsJson.getJSONObject("opening_hours").getJSONArray("weekday_text").get(3).toString();
                friday = googlePlaceDetailsJson.getJSONObject("opening_hours").getJSONArray("weekday_text").get(4).toString();
                saturday = googlePlaceDetailsJson.getJSONObject("opening_hours").getJSONArray("weekday_text").get(5).toString();
                sunday = googlePlaceDetailsJson.getJSONObject("opening_hours").getJSONArray("weekday_text").get(6).toString();
            }

            googlePlaceDetails.put("place_name", placeName);
            googlePlaceDetails.put("vicinity", vicinity);
            googlePlaceDetails.put("rating", rating);
            googlePlaceDetails.put("reviews", reviews);
            googlePlaceDetails.put("monday", monday);
            googlePlaceDetails.put("tuesday", tuesday);
            googlePlaceDetails.put("wednesday", wednesday);
            googlePlaceDetails.put("thursday", thursday);
            googlePlaceDetails.put("friday", friday);
            googlePlaceDetails.put("saturday", saturday);
            googlePlaceDetails.put("sunday", sunday);
            googlePlaceDetails.put("formatted_phone_number", formatted_phone_number);

            Log.d("getPlaceDetails", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlaceDetails", "Error");
            e.printStackTrace();
        }

        return googlePlaceDetails;
    }
}
