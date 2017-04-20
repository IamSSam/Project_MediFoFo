package com.awesome.medifofo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * Created by Eunsik on 03/26/2017.
 */

public class FirstActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    public static String sharedPreferenceFile = "userInfoFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageView imageView = (ImageView) findViewById(R.id.login_image);
        imageLoader.displayImage("drawable://" + R.drawable.medifofo, imageView);

        callbackManager = CallbackManager.Factory.create();
        facebookLogIn(callbackManager);
        goSignUpActivity();
        geLogInActivity();

        if (AccessToken.getCurrentAccessToken() != null) {
            goMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void facebookLogIn(CallbackManager callbackManager) {
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_login_facebook);
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
                Toast.makeText(getApplicationContext(), "User cancels login", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error : " + error, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void goSignUpActivity() {
        Button signUp = (Button) findViewById(R.id.button_sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void geLogInActivity(){
        Button loginEmail = (Button) findViewById(R.id.button_have_account);
        loginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goPersonalInputActivity() {
        Intent intent = new Intent(FirstActivity.this, PersonalInputActivity.class);
        startActivity(intent);
        finish();
    }

    private void goMainActivity() {
        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}