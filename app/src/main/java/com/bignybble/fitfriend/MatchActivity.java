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

        /* Get cards from server and load into app */
        cards = Card.getCard();
        cardSize = cards.size();
        /* Load first cards data into UI */
        loadCardToUI();

        /* Set images for our ImageButtons */
        checkButton.setImageResource(R.drawable.check);
        rejectButton.setImageResource(R.drawable.x);

        /* Get interests of the user and present in image */
        iconImageView.setImageResource(R.drawable.ic_menu_camera);


        /* Set various listeners for our GUI components */
        checkButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);
    }

    /* Set user data on UI and increment cardIndex
    * if we have depleted the number of cards, request more */
    private void loadCardToUI(){
        nameView.setText(cards.get(cardIndex).name);
        new DownloadImageTask(profileImageView).execute(cards.get(cardIndex).URL);
        if(cardIndex < cardSize - 1){
            cardIndex++;
        } else{
            cardIndex = 0;
            cards = Card.getCard();
            cardSize = cards.size();
        }
    }

    @Override
    public void onClick(View v) {
        Context context = getApplicationContext();
        CharSequence text;
        Toast toast;

        if(v.getId() == R.id.checkButton){
            text = "Looks like fun!";
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            loadCardToUI();
        } else{
            text = "Eh, not feeling it.";
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            loadCardToUI();
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
