package com.awesome.medifofo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Eunsik on 03/26/2017.
 */

public class LoginActivity extends FragmentActivity {

    private CallbackManager callbackManager;
    public static String userName, userGender, userAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login Success" , Toast.LENGTH_LONG).show();

                // Token, UserID
                Log.d("TAG", "Facebook Token : " + loginResult.getAccessToken().getToken());
                Log.d("TAG", "Facebook UserID : " + loginResult.getAccessToken().getUserId());

                // Request information to get info
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject obj, GraphResponse response){
                        Log.d("TAG","로그인 결과: " + response.toString());


                        try{
                            String name = obj.getString("name");
                            String gender = obj.getString("gender");
                            String ageRange = obj.getString("age_range");
                            //String birthday = obj.getString("birthday");

                            TextView userName = (TextView)findViewById(R.id.userId);
                            userName.setText(name.toString());

                            TextView userGender = (TextView)findViewById(R.id.gender);
                            userGender.setText(gender.toString());

                            TextView userAge = (TextView)findViewById(R.id.age);
                            userAge.setText(ageRange.toString());

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, gender, age_range");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error : " + error, Toast.LENGTH_LONG).show();
            }
        });

        Button submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
