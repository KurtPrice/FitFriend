package com.bignybble.fitfriend;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MatchActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iconImageView;
    private ImageView profileImageView;
    private Button checkButton;
    private Button rejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        iconImageView = findViewById(R.id.iconImageView);
        profileImageView = findViewById(R.id.profileImageView);
        checkButton = findViewById(R.id.checkButton);
        rejectButton = findViewById(R.id.rejectButton);

        profileImageView.setImageResource(R.drawable.test_profile);
        iconImageView.setImageAlpha(127);

        checkButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = getApplicationContext();
        CharSequence text;
        Toast toast;

        if(v.getId() == R.id.checkButton){
            text = "Ayy lmao";
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        } else{
            text = "Ooo weee, he's trying!";
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
