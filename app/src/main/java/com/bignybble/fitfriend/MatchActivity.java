package com.bignybble.fitfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MatchActivity extends AppCompatActivity {

    private ImageView iconImageView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        iconImageView = findViewById(R.id.iconImageView);
        profileImageView = findViewById(R.id.profileImageView);

        profileImageView.setImageResource(R.drawable.test_profile);
        iconImageView.setImageAlpha(127);
    }
}
