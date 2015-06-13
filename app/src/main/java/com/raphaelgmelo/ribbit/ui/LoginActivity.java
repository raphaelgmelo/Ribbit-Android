package com.raphaelgmelo.ribbit.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.raphaelgmelo.ribbit.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends Activity {


    @InjectView(R.id.signUpText) TextView mSignUpTextView;
    @InjectView(R.id.usernameField) EditText mUsername;
    @InjectView(R.id.passwordField) TextView mPassword;
    @InjectView(R.id.loginButton) TextView mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // must be called before setContentView, on methods with indeterminate time of conclusion
        // use this to show progress
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();

                if (username.isEmpty() || password.isEmpty()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    String message = "Please make sure you enter your username and password!";
                    String title = "Error!";
                    builder.setMessage(message)
                            .setTitle(title)
                            .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{
                    //login

                    setProgressBarIndeterminateVisibility(true);

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            setProgressBarIndeterminateVisibility(false);

                            if (e == null){
                                //Success!

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                // set Flags to make sure the signup screen goes away
                                // so the user doesn't end up on signup again when tapping Back
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);



                            }
                            else{
                                if (!isFinishing()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
