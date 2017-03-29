package com.awesome.medifofo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
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
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_birthday"));

        facebookLogIn(callbackManager);
        moveToMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void facebookLogIn(CallbackManager callbackManager) {
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

                // Token, UserID
                Log.d("TAG", "Facebook Token : " + loginResult.getAccessToken().getToken());
                Log.d("TAG", "Facebook UserID : " + loginResult.getAccessToken().getUserId());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("TAG", "로그인 결과: " + response.toString());

                                try {
                                    String name = object.getString("name");
                                    String gender = object.getString("gender");
                                    //String ageRange = object.getString("age_range");
                                    String birthday = object.getString("birthday");

                                    TextView userName = (TextView) findViewById(R.id.userId);
                                    userName.setText(name.toString());

                                    TextView userGender = (TextView) findViewById(R.id.gender);
                                    userGender.setText(gender.toString());

                                    TextView userBirthday = (TextView) findViewById(R.id.birth);
                                    userBirthday.setText(birthday.toString());

                                    String age = birthday.substring(6,10);
                                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                                    int calenderYear = calendar.get(calendar.YEAR);
                                    int year = calenderYear - Integer.parseInt(age);
                                    TextView userAge = (TextView) findViewById(R.id.age);
                                    userAge.setText(String.valueOf(year));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if(response.getError() == null){
                                    LinearLayout buttonContainer = (LinearLayout)findViewById(R.id.button_container);
                                    buttonContainer.setVisibility(View.GONE);

                                    LinearLayout mainContainer = (LinearLayout)findViewById(R.id.main_container);
                                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.class);
                                    animation.setFillAfter(true);
                                    mainContainer.setGravity(Gravity.CENTER_VERTICAL);
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                // 혹시 몰라서 사진 받아놓기..
                parameters.putString("fields", "id,name,gender,birthday,age_range,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "User cancel Login" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error : " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void moveToMainActivity() {
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
