package com.awesome.medifofo.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.medifofo.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = CommentActivity.class.getSimpleName();
    private static String translatedComment;
    private Animation animationShake;
    private static final String CLIENT_ID = "7rxySx5aIRXRGtWXULIS";
    private static final String CLIENT_SECRET = "JGkB8R8zo5";
    private SharedPreferences information_text, question_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        animationShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);

        information_text = getSharedPreferences("information_text", MODE_PRIVATE);
        question_text = getSharedPreferences("question_text", MODE_PRIVATE);
        Log.d("information_text ", information_text.getString("PHR", ""));
        Log.d("question_text", question_text.getString("question_text", ""));

        Button buttonComment = (Button) findViewById(R.id.button_send_comment);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommentToDoctor();
                //finish();
            }
        });
    }

    private void sendCommentToDoctor() {
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout_send_comment);
        EditText comment = (EditText) findViewById(R.id.send_user_comment);
        String userComment = comment.getText().toString();
        textInputLayout.setError(null);
        View focus = null;
        boolean cancel = false;


        if (TextUtils.isEmpty(userComment)) {
            textInputLayout.setError("This field is required");
            textInputLayout.setAnimation(animationShake);
            textInputLayout.startAnimation(animationShake);
            focus = textInputLayout;
            cancel = true;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            new HttpAsyncTask().execute("http://igrus.mireene.com/medifofo_web/php/papa.php?comment=" + userComment);
            //finish();
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
            Log.d("Result ", result);
            try {

                JSONObject object = new JSONObject(result);
                translatedComment = object.getJSONObject("message").getJSONObject("result").getString("translatedText");

            } catch (JSONException e) {
                Log.e("Error ", e.toString());
            }

            new QueueAsyncTask().execute("http://igrus.mireene.com/medifofo/patient_treatment_queue.php");

        }
    }

    private class QueueAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CommentActivity.this, "Just for seconds", "Searching for doctors...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST1(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(CommentActivity.this, "Fail. Check your internet connection.", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(CommentActivity.this, "Success Please wait for seconds.", Toast.LENGTH_SHORT).show();
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

            String new_information_text = information_text.getString("PHR", "") + ", " + translatedComment;
            Log.d("translatedComment ", translatedComment);
            Log.d("new_information_text ", new_information_text);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
            nameValuePair.add(new BasicNameValuePair("information_text", new_information_text));
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

}
