package com.awesome.medifofo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.awesome.medifofo.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.awesome.medifofo.FindHospitalActivity.*;

public class FindHospital {

    private static final String CLIENT_ID = "7rxySx5aIRXRGtWXULIS";
    private static final String CLIENT_SECRET = "JGkB8R8zo5";
    public static String[] hospitalTitle, hospitalAddress = null;
    private Activity previousActivity;

    public void initiateFindHospital(Activity previousActivity) {
        this.previousActivity = previousActivity;

        String keyword = "정형외과";

        try {
            String location = URLEncoder.encode(keyword, "UTF-8");
            new HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_nmap_search.php?location=" + location);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }


    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

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
                hospitalAddress = new String[array.length()];

                for (int i = 0; i < array.length(); i++) {
                    hospitalTitle[i] = array.getJSONObject(i).getString("title");
                    hospitalAddress[i] = array.getJSONObject(i).getString("roadAddress");
                    Log.d("[" + i + "]: ", hospitalTitle[i]);
                    Log.d("[" + i + "]: ", hospitalAddress[i]);
                }

                Intent intent = new Intent(previousActivity, FindHospitalActivity.class);
                previousActivity.startActivity(intent);

            } catch (JSONException e) {
                Log.e("Error: ", e.toString());
            }

        }
    }

}
