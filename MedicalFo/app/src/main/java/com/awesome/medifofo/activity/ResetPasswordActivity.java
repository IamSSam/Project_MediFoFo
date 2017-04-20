package com.awesome.medifofo.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

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
            focus = textInputLayout;
        } else if (!this.isEmailValid(emailAddress)) {
            textInputLayout.setError("This email address is invalid");
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
                        Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}
