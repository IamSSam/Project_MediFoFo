package com.awesome.medifofo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.awesome.medifofo.RecyclerItemClickListener;
import com.awesome.medifofo.adapter.DoctorListAdapter;
import com.awesome.medifofo.adapter.SymptomListAdapter;
import com.awesome.medifofo.model.ListItem;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DoctorListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorListAdapter adapter;
    private List<ListItem> data;
    private SharedPreferences information_text, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recycler_view);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /**
         * @Information_text
         * NAME, COUNTRY, GENDER, AGE
         * PHR = height + "cm, " + weight + "kg, " + abo + ", " + medicine + ", " + allergy + ", " + history + ", " + sleeptime + ", " + dailystride;
         */
        information_text = getSharedPreferences("information_text", MODE_PRIVATE);
        question_text = getSharedPreferences("question_text", MODE_PRIVATE);
        initRecyclerView();
    }


    private void initRecyclerView() {
        Context context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.rec_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new DoctorListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_doctor_list.php");

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        data.get(position).getTitle();

                        new DoctorListActivity.QueueAsyncTask().execute("http://igrus.mireene.com/medifofo/patient_treatment_queue.php");

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(DoctorListActivity.this, "Fail. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
            try {
                Log.d("jsoncheck", result);
                JSONArray jsonArray = new JSONArray(result);
                data = new ArrayList<>();
                Log.d("doctor_name: ", jsonArray.getJSONObject(0).getString("doctor_name"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    ListItem item = new ListItem();
                    item.setTitle(jsonArray.getJSONObject(i).getString("doctor_name"));
                    item.setHospitalName(jsonArray.getJSONObject(i).getString("hospital_name"));
                    data.add(item);

                    adapter = new DoctorListAdapter(data);
                    recyclerView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
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

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);

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
        Log.d("Ddd", inputStream.toString());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();

        if (!result.contains("]"))
            result += "]";
        return result;
    }

    private class QueueAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DoctorListActivity.this, "Just for seconds", "Searching for doctors...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST1(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(DoctorListActivity.this, "Fail. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }else{
                progressDialog.dismiss();
                Toast.makeText(DoctorListActivity.this, "Success Please wait for seconds.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String POST1(String url) {
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

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();


            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);

            nameValuePair.add(new BasicNameValuePair("information_text", information_text.getString("PHR", "")));
            nameValuePair.add(new BasicNameValuePair("question_text", question_text.getString("question_text", "")));

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

}



