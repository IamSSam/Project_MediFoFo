package com.awesome.medifofo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by Eunsik on 26/03/2017.
 */

public class UserInformationActivity extends AppCompatActivity {

    private Button submitButton;
    private TextView name, gender, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinformation);

        name = (TextView) findViewById(R.id.userId);
        //Log.d("TAG", LoginActivity.userName);
        //name.setText(LoginActivity.userName);

        gender = (TextView) findViewById(R.id.gender);
        //gender.setText(extras.getString("GENDER"));

        age = (TextView) findViewById(R.id.age);
        //age.setText(extras.getString("AGE"));

        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
