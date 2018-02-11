package com.bignybble.fitfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);

    }

    @Override
    public void onClick(View v) {
        Intent startMatch = new Intent(this, MatchActivity.class);
        startActivity(startMatch);
    }

    public void launchNavigationBarActivity(View view) {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }

    public void launchSignupActivity(View view) {
        Intent startSignup = new Intent(this, SignupActivity.class);
        startActivity(startSignup);
    }
}
