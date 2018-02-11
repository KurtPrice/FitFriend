package com.bignybble.fitfriend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class MatchActivity extends NavigationDrawerActivity implements View.OnClickListener{

    private ImageView iconImageView;
    private ImageView profileImageView;
    private ImageButton checkButton;
    private ImageButton rejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        /* Set up components on the UI for programmatic use --Kurtpr */
        iconImageView = findViewById(R.id.iconImageView);
        profileImageView = findViewById(R.id.profileImageView);
        checkButton = findViewById(R.id.checkButton);
        rejectButton = findViewById(R.id.rejectButton);

        /* Get image from URL and load as profile image */
        new DownloadImageTask(profileImageView)
                .execute("https://avatars2.githubusercontent.com" +
                        "/u/1095875?s=400&u=99d416c0bf538e21fa827bb16617fa0e52a2fba0&v=4");

        /* Set images for our ImageButtons */
        checkButton.setImageResource(R.drawable.check);
        rejectButton.setImageResource(R.drawable.x);


        /* Set various listeners for our GUI components */
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

    /* Taken from the Android Developer blog at the following link. Used under
     * the license of the author for open source use and development.
     * http://web.archive.org/web/20120804171902/http://developer.aiwgame.com:80/about-aiw
     * --Kurtpr
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
