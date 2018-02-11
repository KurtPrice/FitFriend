package com.bignybble.fitfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void launchEditProfileActivity(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        EditText etv = (EditText) findViewById(R.id.editTextName);

        Toast.makeText(getApplicationContext(),"Hello :"+etv.getText(),Toast.LENGTH_LONG).show();
        intent.putExtra("name",etv.getText());
        startActivity(intent);
    }
}
