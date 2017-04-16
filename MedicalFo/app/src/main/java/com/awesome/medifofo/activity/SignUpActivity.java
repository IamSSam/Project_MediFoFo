package com.awesome.medifofo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eunsik on 2017-04-16.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static String gender, month;
    public static EditText firstName, lastName, birth;
    public EditText userEmail, userPassword;
    private Button manButton, womanButton, signUpButton;

    private HttpAsyncTask httpAsyncTask = null;

    private AutoCompleteTextView userFirstName, userLastName, userEmailView;
    private View progressBarView, signUpFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = (EditText) findViewById(R.id.user_first_name);
        lastName = (EditText) findViewById(R.id.user_last_name);
        birth = (EditText) findViewById(R.id.user_year);

        userEmail = (EditText) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);

        initView();


    }

    public void initView() {
        signUpFormView = findViewById(R.id.form_sign_up);
        progressBarView = findViewById(R.id.progress_sign_up);
        userFirstName = (AutoCompleteTextView) findViewById(R.id.user_first_name);
        userLastName = (AutoCompleteTextView) findViewById(R.id.user_last_name);
        userEmailView = (AutoCompleteTextView) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);

        userPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL || id == R.id.ime_password) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        manButton = (Button) findViewById(R.id.user_man);
        manButton.setOnClickListener(this);
        womanButton = (Button) findViewById(R.id.user_woman);
        womanButton.setOnClickListener(this);
        signUpButton = (Button) findViewById(R.id.button_register);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_man:
                if (!manButton.isSelected()) {
                    manButton.setSelected(true);
                    womanButton.setSelected(false);
                    gender = "Male";

                } else {
                    manButton.setSelected(false);
                    gender = null;
                }

                break;

            case R.id.user_woman:
                if (!womanButton.isSelected()) {
                    womanButton.setSelected(true);
                    manButton.setSelected(false);
                    gender = "Female";

                } else {
                    womanButton.setSelected(false);
                    gender = null;
                }

                break;

            case R.id.button_sign_up:
                /*
                if (userEmail.getText().length() == 0 || userPassword.getText().length() < 8) {
                    Toast.makeText(SignUpActivity.this, "아이디와 비밀번호를 확인해주세요. 비밀번호는 8자 이상으로 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (getFirstName().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (getLastName().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (getYear().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: 회원가입하면 DB에 등록되는 부분. 이 정보들을 서버로 넘겨야함.
                    new HttpAsyncTask().execute("http://igrus.mireene.com/applogin/register.php");
                    Log.d("Sign up Clicked", "Success");
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                */

                break;
        }
    }

    public static String getFirstName() {
        return firstName.getText().toString();
    }

    public static String getLastName() {
        return lastName.getText().toString();
    }

    public static String getYear() {
        return birth.getText().toString();
    }

    public String getGender() {
        return gender;
    }

    static String getMonth() {
        if (month.length() == 2) {
            return month.substring(0, 1);
        } else {
            return month.substring(0, 2);
        }
    }

    private void attemptSignUp() {
        if (httpAsyncTask != null) {
            return;
        }

        userFirstName.setError(null);
        userLastName.setError(null);
        userEmailView.setError(null);

        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String email = userEmailView.getText().toString();
        String password = userPassword.getText().toString();

        boolean cancel = false;
        View focus = null;

        if (TextUtils.isEmpty(firstName)) {
            userFirstName.setError(getString(R.string.error_field_required));
            focus = userFirstName;
            cancel = true;
        } else if (TextUtils.isEmpty(lastName)) {
            userLastName.setError(getString(R.string.error_field_required));
            focus = userLastName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            userEmailView.setError(getString(R.string.error_field_required));
            focus = userEmailView;
            cancel = true;
        } else if (!this.isEmailValid(email)) {
            userEmailView.setError(getString(R.string.error_invalid_email));
            focus = userEmailView;
            cancel = true;
        } else if (!this.isPasswordValid(password)) {
            userPassword.setError(getString(R.string.error_invalid_password));
            focus = userPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(password) && isEmailValid(email)) {
            userPassword.setError(getString(R.string.error_field_required));
            focus = userPassword;
            cancel = true;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            showProgress(true);
            //httpAsyncTask = new HttpAsyncTask();
            //httpAsyncTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 15;
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        signUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        signUpFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(
                new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        signUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }

                });

        progressBarView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBarView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(
                new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        signUpFormView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }

                });
    }


    public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            httpAsyncTask = null;
            showProgress(false);

            if (result.equals("Did not work!")) {
                Toast.makeText(SignUpActivity.this, "Fail! Please check internet connection.", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject jobj = new JSONObject(result);
                if (jobj.getString("error").equals("true")) {
                    Toast.makeText(SignUpActivity.this, "You already have same email or server error.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Success sign up!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            httpAsyncTask = null;
            showProgress(false);
        }

    }

    public String POST(String url) {

        httpAsyncTask = null;

        showProgress(false);
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

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
            nameValuePair.add(new BasicNameValuePair("userid", userEmail.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("password", userPassword.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("firstname", firstName.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("lastname", lastName.getText().toString()));


            //Log.d("monthandday", month + " " + day.getText().toString());

            String monthtmp;
            if (getMonth().length() == 1) {
                monthtmp = "0" + getMonth();
            } else monthtmp = getMonth();

            nameValuePair.add(new BasicNameValuePair("birth", birth.getText().toString() + monthtmp));
            String sextmp;
            if (gender.equals("Men")) sextmp = "1";
            else sextmp = "0";
            nameValuePair.add(new BasicNameValuePair("sex", sextmp));

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
//            Log.d("InputStream", e.getLocalizedMessage());
        }
//        Log.d("http", result);

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