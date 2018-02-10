package com.bignybble.fitfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MatchActivity extends AppCompatActivity {

    private ImageView iconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        //findViewById(R.id.icomImageView);
        //iconImageView.setImageAlpha(127);
    }
}
