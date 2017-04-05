package com.awesome.medifofo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Eunsik on 03/26/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    public static String publicPictureURL;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        facebookLogIn(callbackManager);

        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("Current Token: ", accessToken.getToken().toString());

        /*
        if(!accessToken.getToken().isEmpty()){
            setContentView(R.layout.activity_login);
            facebookLogIn(callbackManager);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }else{

        }
        */

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogIn(CallbackManager callbackManager) {
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            // TODO: resist auto login
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

                // Token, UserID
                Log.d("TAG", "Facebook Token : " + loginResult.getAccessToken().getToken());
                Log.d("TAG", "Facebook UserID : " + loginResult.getAccessToken().getUserId());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String gender = object.getString("gender");
                                    //String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    Intent intent = new Intent(LoginActivity.this, PersonalInputActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("gender", gender);

                                    moveToPersonalInputActivity(intent);

                                    //publicPictureURL = picture;
                                    publicPictureURL = "https://graph.facebook.com/" + id + "/picture?type=large";

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,picture");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "User cancel Login", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error : " + error, Toast.LENGTH_LONG).show();
            }
        });

    }


    private void moveToPersonalInputActivity(Intent intent) {
        startActivity(intent);
    }

    /*
    private void logOutFromFacebook() {

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                }

            }
        };

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

}