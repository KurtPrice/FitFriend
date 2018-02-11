package com.bignybble.fitfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tv = (TextView)findViewById(R.id.descriptionTextView);
        tv.setText("Hi I'm Allen and I'd love to go swmming with you some time. I like to go in the evenings around 9 pm. This is a lot of text to check if the scrolling works or not. We need more text. more and more text. more and more text. Ok maybe we're good now");
    }

    public void onCheckboxClicked(View view) {

    }

}
