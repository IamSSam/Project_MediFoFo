package com.awesome.medifofo.activity;

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

import com.awesome.medifofo.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = CommentActivity.class.getSimpleName();
    String userComment;
    private Animation animationShake;
    private static final String CLIENT_ID = "7rxySx5aIRXRGtWXULIS";
    private static final String CLIENT_SECRET = "JGkB8R8zo5";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        animationShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);

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
        final String userComment = comment.getText().toString();
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
            finish();
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
                object.getJSONObject("message").getJSONObject("result").getJSONObject("translatedText");
                Log.d("translatedComment ", String.valueOf(object.getJSONObject("message").getJSONObject("result").getJSONObject("translatedText")));
            } catch (JSONException e) {
                Log.e("Error ", e.toString());
            }

        }
    }
}
