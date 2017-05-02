package com.awesome.medifofo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.awesome.medifofo.Person;
import com.awesome.medifofo.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
import java.util.Calendar;
import java.util.List;


/*
 * Created by Eunsik on 03/26/2017.
 */

public class FacebookLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;
    public static String sharedPreferenceFile = "userFacebookFILE";

    /* Database table information */
    private String tokenId, emailId;
    private final String platform = "2"; // platform of facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        initView();

        facebookLogIn(callbackManager);
        goSignUpActivity();
        geLogInActivity();

        if (AccessToken.getCurrentAccessToken() != null) {
            goMainActivity();
        }
    }

    public void initView() {
        imageLoader();
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageView imageView = (ImageView) findViewById(R.id.login_image);
        imageLoader.displayImage("drawable://" + R.drawable.medifofo, imageView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogIn(CallbackManager callbackManager) {
        facebookLoginButton = (LoginButton) findViewById(R.id.button_login_facebook);
        facebookLoginButton.setReadPermissions("public_profile", "email");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Facebook login canceled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error : " + error, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        final ProgressDialog progressDialog = ProgressDialog.show(FacebookLoginActivity.this, "Please wait...", "Signing in ...", true);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                    GraphRequest request = GraphRequest.newMeRequest(
                            token,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    System.out.println("loginResult JSON : " + object.toString());
                                    try {
                                        String id = object.getString("id");
                                        String name = object.getString("name");
                                        String gender = object.getString("gender");
                                        String pictureURL = "https://graph.facebook.com/" + id + "/picture?type=large";
                                        String birthday = object.getString("birthday");
                                        String birth[] = birthday.split("/");
                                        String email = object.getString("email");

                                        tokenId = token.toString();
                                        emailId = email;

                                        System.out.println("TOKEN : " + tokenId + ", EMAIL : " + emailId);

                                        /*
                                         * Save user information "sharedPreferenceFile"
                                         * Information : name, gender, pictureURL, email
                                         */

                                        SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferenceFile, Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("NAME", name);
                                        editor.putString("GENDER", gender);
                                        editor.putString("AGE", String.valueOf(calculateUserAge(birth[2])));
                                        editor.putString("URL", pictureURL);
                                        editor.apply();

                                        goMainActivity();
                                        new FacebookLoginActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/patient_register.php");

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,birthday,gender,email");
                    request.setParameters(parameters);
                    request.executeAsync();


                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goSignUpActivity() {
        Button signUp = (Button) findViewById(R.id.button_sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacebookLoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void geLogInActivity() {
        Button loginEmail = (Button) findViewById(R.id.button_have_account);
        loginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacebookLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private int calculateUserAge(String age) {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int userAge = currentYear - Integer.parseInt(age);
        return userAge;
    }

    private void imageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(FacebookLoginActivity.this, "Fail. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject jobj = new JSONObject(result);
                if (jobj.getString("error").equals("true"))
                    Toast.makeText(FacebookLoginActivity.this, "The Id already exists or Server error.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(FacebookLoginActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();


            } catch (JSONException e) {
                e.printStackTrace();
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
            nameValuePair.add(new BasicNameValuePair("token_id", tokenId));
            nameValuePair.add(new BasicNameValuePair("email_id", emailId));
            nameValuePair.add(new BasicNameValuePair("platform", platform));

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