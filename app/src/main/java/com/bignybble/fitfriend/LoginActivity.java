package com.bignybble.fitfriend;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView usernameView;
    private TextView passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        usernameView = findViewById(R.id.usernameView);
        passwordView = findViewById(R.id.editTextPassword);

    }

    public void login(View v){
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        Log.d("DEBUG", "USER INFO" + username);
        Log.d("DEBUG", "PASS INFO" + password);
        new RestLoginTask().execute("http://bignybble.com:105/api/auth/login",
                                    "email="+username+"&password="
                                            +password);
    }

    public void launchMatchActivity(Card user) {
        Intent startMatch = new Intent(this, MatchActivity.class);
        startActivity(startMatch);
    }

    public void launchSignupActivity(View view) {
        Intent startSignup = new Intent(this, SignupActivity.class);
        startActivity(startSignup);
    }


    private class RestLoginTask extends  AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            RestClient client = new RestClient();
            String token = client.makePost(urls[0], urls[1]);
            try {
                JSONObject json = new JSONObject(token);
                token = json.getString("token");
            } catch(JSONException ex){
                Log.d("ERROR", "THIS IS SAD");
            }

            String result = client.makeGet("http://bignybble.com:105/api/auth/me", "x-access-token", token);
            Log.d("RESULT", "Resulting in: " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject json = new JSONObject(result);
                Card userCard = CardTools.cardFromJson(json);
                launchMatchActivity(userCard);
            } catch(Exception ex){
                Log.d("DEBUG", "how?: " + ex.getMessage());
            }
        }
    }
}
