package com.bignybble.fitfriend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

import io.saeid.fabloading.LoadingView;

public class MatchActivity extends NavigationDrawerActivity implements View.OnClickListener{

    private ImageView iconImageView;
    private ImageView profileImageView;
    private ImageButton checkButton;
    private ImageButton rejectButton;
    private TextView nameView;
    private String userToken;
    private LoadingView mLoadingView;
    private ArrayList<Card> cards;
    private int cardIndex = 0;
    private int cardSize;
    private final String USER_URL = "http://bignybble.com:105/api/auth/cards/";
    private String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

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

        /* Set images for our ImageButtons */
        checkButton.setImageResource(R.drawable.check);
        rejectButton.setImageResource(R.drawable.x);

        /* Set various listeners for our GUI components */
        checkButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);

        /* Handle Any Extras */
        userToken = getIntent().getExtras().getString("token");

        /* While we wait for cards to load present a loading bar */
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        int marvel_1 = R.drawable.loading_1;
        int marvel_2 = R.drawable.loading_2;
        int marvel_3 = R.drawable.loading_3;
        int marvel_4 = R.drawable.loading_4;
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), marvel_1,
                LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), marvel_2,
                LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), marvel_3,
                LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), marvel_4,
                LoadingView.FROM_BOTTOM);

        mLoadingView.addListener(new LoadingView.LoadingListener() {
            @Override
            public void onAnimationStart(int currentItemPosition) {

            }

            @Override
            public void onAnimationRepeat(int nextItemPosition) {

            }

            @Override
            public void onAnimationEnd(int nextItemPosition) {

            }
        });


        /* Asynchronously get cards from server */
        mLoadingView.startAnimation();
        new RestClientTask().execute(USER_URL, "x-access-token", userToken);
        mLoadingView.setVisibility(View.GONE);
    }

    /* Set user data on UI and increment cardIndex
    * if we have depleted the number of cards, request more */
    public void loadCardToUI(){
        // Find our next card --Kurtpr
        if(cardIndex < cardSize){
            Card card = cards.get(cardIndex);
            nameView.setText(card.name);
            new DownloadImageTask(profileImageView).execute(card.URL);

            String checkBoxId = "checkbox_";
            for(int i=0; i< card.schedule.length; i++)
            {

                checkBoxId = checkBoxId + daysOfWeek[i];
                CheckBox checkBox = (CheckBox)findViewById(getResources().getIdentifier(checkBoxId,"id", getPackageName()));
                checkBox.setChecked(card.schedule[i]);
                checkBoxId = "checkbox_";

            }
        } else{
            new RestClientTask().execute(USER_URL, "x-access-token", userToken);
        }
    }

    @Override
    public void onClick(View v) {
        Context context = getApplicationContext();
        CharSequence text;
        Toast toast;

        if(v.getId() == R.id.checkButton){
            /* Send notification that we like this user */
            Card likedUser = cards.get(cardIndex);
            new RestLikeTask().execute("http://bignybble.com:105/api/auth/like/"+userToken,
                    "body.liked="+likedUser.uid);

            toast = Toast.makeText(context, "Looks like fun!", Toast.LENGTH_SHORT);
            toast.show();
            cardIndex++;
            loadCardToUI();

        } else{
            text = "Eh, not feeling it.";
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            cardIndex++;
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

    /* Used to make a GET request to the server backend and retrieve user
     * information on accounts and other users. --Kurtpr
     */
    private class RestClientTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            RestClient client = new RestClient();
            String results = client.makeGet(urls[0], urls[1], urls[2]);
            try {
                return new JSONArray(results).toString();
            } catch(Exception ex){
                Log.d("DEBUG", "Could not get JSON, " + ex.getLocalizedMessage());
            }
            return "[]"; //We do not need to return, but need String to use onPostExecute below.
        }

        /* Called after doInBackground finishes processing. Should be used to interact with UI
        * that depends on having the data from doInBackground.
        * */
        @Override
        protected void onPostExecute(String result){
            try {
                cards = CardTools.getCards(new JSONArray(result));
            } catch(Exception ex){
                Log.d("FAIL", ex.getMessage());
            }
            cardSize = cards.size();
            cardIndex = 0;
            loadCardToUI();
        }
    }

    /* Specially constructed for use in liking other users' cards.
     * This will use the makePut method to update information. --Kurtpr
     */
    private class RestLikeTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            RestClient client = new RestClient();
            String results = client.makePut(urls[0], urls[1]);
            try {
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
            cardSize = cards.size();
            cardIndex = 0;
            if(cardSize > 0) {
                loadCardToUI();
            }
        }
    }
}
