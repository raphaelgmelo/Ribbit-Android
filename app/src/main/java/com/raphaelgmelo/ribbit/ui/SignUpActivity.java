package com.raphaelgmelo.ribbit.ui;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.raphaelgmelo.ribbit.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends Activity {

    @InjectView(R.id.usernameField) EditText mUsername;
    @InjectView(R.id.passwordField) TextView mPassword;
    @InjectView(R.id.emailField) TextView mEmail;
    @InjectView(R.id.signUpButton) TextView mSignUpButton;
    @InjectView(R.id.cancelButton) TextView mCancelButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.inject(this);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                username = username.trim();
                password = password.trim();
                email = email.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    String message = "Please make sure you enter your username, password and email!";
                    String title = "Error!";
                    builder.setMessage(message)
                        .setTitle(title)
                        .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{
                    //create the new user
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                // Success!
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);

                                // set Flags to make sure the signup screen goes away
                                // so the user doesn't end up on signup again when tapping Back
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                            else{
                                if (!isFinishing()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                    String title = "Error!";
                                    builder.setMessage(e.getMessage())
                                            .setTitle(title)
                                            .setPositiveButton(android.R.string.ok, null);

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }

                            }
                        }
                    });
                }

            }
        });

    }

}
