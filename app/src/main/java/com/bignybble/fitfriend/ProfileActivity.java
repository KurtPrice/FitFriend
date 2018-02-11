package com.bignybble.fitfriend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private Card userCard;
    private String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userCard = new Card("Allen Turing", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Alan_Turing_Aged_16.jpg/220px-Alan_Turing_Aged_16.jpg",
                new boolean[] {false, true, true, false, false, true, true}, new char[] {'s', 'f', 'g'}, "10", "I made computers");
        userCard.bio = "Hi I'm Allen and I'd love to go swmming with you some time. I like to go in the evenings around 9 pm. This is a lot of text to check if the scrolling works or not. We need more text. more and more text. more and more text. Ok maybe we're good now";
        loadUserAccount(userCard);
    }

    public void onCheckboxClicked(View view) {

    }
    private void loadUserAccount(Card card)
    {
        TextView dtv = (TextView)findViewById(R.id.descriptionTextView);
        dtv.setText(card.bio);

        TextView ntv = (TextView)findViewById(R.id.nameTextView);
        ntv.setText(card.name);

        ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic);
        new ProfileActivity.DownloadImageTask(profilePictureView).execute(card.URL);
        loadAvailability(card.schedule);
    }

    private void loadAvailability(boolean[] availability)
    {
        String checkBoxId = "checkbox_";
        for(int i=0; i<availability.length; i++)
        {
            if(availability[i])
            {
                checkBoxId = checkBoxId + daysOfWeek[0];
                findViewById(getResources().getIdentifier(checkBoxId,"id", getPackageName()));
            }
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
