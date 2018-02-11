package com.bignybble.fitfriend;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
<<<<<<< HEAD
import android.widget.Button;
=======
>>>>>>> 9c18fd37713e0b4cebc5ec7ec0e1296014ba84cd
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private void startMatchView(Card user){
        Intent startMatch = new Intent(this, MatchActivity.class);
        startActivity(startMatch);
    }

    private void makeUser(View view){
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String email = emailView.getText().toString();

        new RestClientTask().execute("http://bignybble.com:105/api/auth/register",
                                     "name="+username+"&email="+email
                                             +"&password="+password);
    }

<<<<<<< HEAD
    @Override
    public void onClick(View v) {
        makeUser(v);
    }

    private class RestClientTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            RestClient client = new RestClient();
            return client.makePost(urls[0], urls[1]);
        }

        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject json = new JSONObject(result);
                Card userCard = CardTools.cardFromJson(json);
                startMatchView(userCard);
            } catch(Exception ex){
                Log.d("DEBUG", "how?: " + ex.getMessage());
            }
        }
=======
    public void launchEditProfileActivity(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        EditText etv = (EditText) findViewById(R.id.editTextName);

        Toast.makeText(getApplicationContext(),"Hello :"+etv.getText(),Toast.LENGTH_LONG).show();
        intent.putExtra("name",etv.getText());
        startActivity(intent);
>>>>>>> 9c18fd37713e0b4cebc5ec7ec0e1296014ba84cd
    }
}
