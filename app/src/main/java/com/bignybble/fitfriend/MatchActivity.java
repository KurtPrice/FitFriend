package com.bignybble.fitfriend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

public class MatchActivity extends NavigationDrawerActivity implements View.OnClickListener{

    private ImageView iconImageView;
    private ImageView profileImageView;
    private ImageButton checkButton;
    private ImageButton rejectButton;
    private TextView nameView;
    private ArrayList<Card> cards;
    private int cardIndex = 0;
    private int cardSize;
    private String results;
    private CardTools cardTool;
    private final String USER_URL = "http://bignybble.com:105/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_match);
        super.onCreate(savedInstanceState);

        /* Set up components on the UI for programmatic use --Kurtpr */
        iconImageView = findViewById(R.id.iconImageView);
        profileImageView = findViewById(R.id.profileImageView);
        checkButton = findViewById(R.id.checkButton);
        rejectButton = findViewById(R.id.rejectButton);
        nameView = findViewById(R.id.nameTextView);

        /* Get profiles for our user to read through */
        cardTool = new CardTools();

        /* Get cards from server and load into app */
        new RestClientTask().execute(USER_URL);

        /* Set images for our ImageButtons */
        checkButton.setImageResource(R.drawable.check);
        rejectButton.setImageResource(R.drawable.x);

        /* Set various listeners for our GUI components */
        checkButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);

        profileImageView.setImageResource(R.drawable.local_profile_pic);
        new RestClientTask().execute(USER_URL);
    }

    /* Set user data on UI and increment cardIndex
    * if we have depleted the number of cards, request more */
    public void loadCardToUI(){
        nameView.setText(cards.get(cardIndex).name);
        new DownloadImageTask(profileImageView).execute(cards.get(cardIndex).URL);
        // Find our next card --Kurtpr
        if(cardIndex < cardSize - 1){
            cardIndex++;
        } else{
            cardIndex = 0;
            new RestClientTask().execute(USER_URL);
        }
    }

    @Override
    public void onClick(View v) {
        Context context = getApplicationContext();
        CharSequence text;
        Toast toast;

        if(v.getId() == R.id.checkButton){
            RestClient client = new RestClient();
            toast = Toast.makeText(context, results, Toast.LENGTH_SHORT);
            toast.show();
            new RestClientTask().execute(USER_URL);

        } else{
            text = "Eh, not feeling it.";
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            new RestClientTask().execute(USER_URL);
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

    private class RestClientTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            RestClient client = new RestClient();
            String results = client.makeRequest(urls[0]);
            try {
                Log.d("DEBUG", "WUT " + results);
                return new JSONArray(results).toString();
            } catch(Exception ex){
                Log.d("DEBUG", "Could not get JSON, " + ex.getLocalizedMessage());
            }
            return "[]"; //We do not need to return, but need String to use onPostExecute below.
        }

        @Override
        protected void onPostExecute(String result){
            try {
                cards = CardTools.getCards(new JSONArray(result));
            } catch(Exception ex){
                Log.d("FAIL", ex.getMessage());
            }
            Log.d("DEBUG", "Size " + cards.get(1).schedule[0]);
            cardSize = cards.size();
            loadCardToUI();
        }
    }
}
