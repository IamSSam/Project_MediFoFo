package com.awesome.medifofo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText userEmail;
    private EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        Button buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailLogin();
            }
        });

        Button buttonForgotPassword = (Button) findViewById(R.id.button_forgot_password);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goResetPassword();
            }
        });
    }


    private void initView() {
        userEmail = (EditText) findViewById(R.id.login_user_email);
        userPassword = (EditText) findViewById(R.id.login_user_password);
        userPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_UNSPECIFIED || id == R.id.ime_password) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void emailLogin() {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Proccessing...", true);

        (firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Success",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void attemptLogin() {

        userEmail.setError(null);

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        boolean cancel = false;
        View focus = null;

        if (TextUtils.isEmpty(email)) {
            userEmail.setError(getString(R.string.error_field_required));
            focus = userEmail;
            cancel = true;
        } else if (!this.isEmailValid(email)) {
            userEmail.setError(getString(R.string.error_invalid_email));
            focus = userEmail;
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
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 15;
    }

    private void goResetPassword() {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }


}
