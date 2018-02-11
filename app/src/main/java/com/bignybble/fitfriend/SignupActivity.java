package com.bignybble.fitfriend;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameView;
    private EditText passwordView;
    private EditText emailView;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* Get our views */
        usernameView = (EditText)findViewById(R.id.nameTextEdit);
        passwordView = (EditText)findViewById(R.id.passwordTextEdit);
        emailView = (EditText)findViewById(R.id.emailTextEdit);
        signUpButton = findViewById(R.id.buttonSignup);

        signUpButton.setOnClickListener(this);
    }

    private void startEditView(Card user){
        Intent startEdit = new Intent(this, EditProfileActivity.class);
        startEdit.putExtra("name", user.name);
        startEdit.putExtra("email", emailView.getText().toString());
        startActivity(startEdit);
    }

    private void makeUser(View view){
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String email = emailView.getText().toString();

        new RestClientTask().execute("http://bignybble.com:105/api/auth/register",
                                     "name="+username+"&email="+email
                                             +"&password="+password);
    }

    @Override
    public void onClick(View v) {
        makeUser(v);
    }

    private class RestClientTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            RestClient client = new RestClient();
            return client.makePost(urls[0], urls[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                Card userCard = CardTools.cardFromJson(json);
                startEditView(userCard);
            } catch (Exception ex) {
                Log.d("DEBUG", "how?: " + ex.getMessage());
            }
        }
    }
}
