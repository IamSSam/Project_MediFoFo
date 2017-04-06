package com.awesome.medifofo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

/**
 * Created by Eunsik on 03/26/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    public static String sharedPreferenceFile = "userFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        facebookLogIn(callbackManager);

        if(AccessToken.getCurrentAccessToken() != null){
            goMainAcitivty();
        }
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
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

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
                                    String pictureURL = "https://graph.facebook.com/" + id + "/picture?type=large";

                                    Log.d("Go PersonalInput", AccessToken.getCurrentAccessToken().toString());

                                    /**
                                     * Save user information "sharedPreferenceFile"
                                     * Information : name, gender, pictureURL
                                     */
                                    SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferenceFile, 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("NAME", name);
                                    editor.putString("GENDER", gender);
                                    editor.putString("URL", pictureURL);
                                    editor.commit();

                                    goPersonalInputActivity();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender");
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


    private void goPersonalInputActivity() {
        Intent intent = new Intent(LoginActivity.this, PersonalInputActivity.class);
        startActivity(intent);
    }

    private void goMainAcitivty() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    /*
    private void logOut() {

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                }

            }
        };

    }*/
}