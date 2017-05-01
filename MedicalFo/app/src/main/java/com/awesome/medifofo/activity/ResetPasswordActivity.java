package com.awesome.medifofo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ResetPasswordActivity extends AppCompatActivity {

    private Animation animationShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageView imageView = (ImageView) findViewById(R.id.reset_password_image);
        imageLoader.displayImage("drawable://" + R.drawable.medifofo, imageView);

        animationShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);

        Button buttonSendEmail = (Button) findViewById(R.id.button_send_email);
        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout_send_email);
        EditText email = (EditText) findViewById(R.id.send_user_email);
        String emailAddress = email.getText().toString();
        textInputLayout.setError(null);
        View focus = null;
        boolean cancel = false;


        if (TextUtils.isEmpty(emailAddress)) {
            textInputLayout.setError("This field is required");
            textInputLayout.setAnimation(animationShake);
            textInputLayout.startAnimation(animationShake);
            focus = textInputLayout;
            cancel = true;
        } else if (!this.isEmailValid(emailAddress)) {
            textInputLayout.setError("This email address is invalid");
            textInputLayout.setAnimation(animationShake);
            textInputLayout.startAnimation(animationShake);
            focus = textInputLayout;
            cancel = true;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Sent Email", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
