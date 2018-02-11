package com.bignybble.fitfriend;

import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class ProfileActivity extends NavigationDrawerActivity {

    private Card userCard;
    private String token;
    private String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setContentView(R.layout.activity_profile);
        super.onCreate(savedInstanceState);
        

        token = getIntent().getExtras().getString("token");

        userCard = new Card("Allen Turing", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Alan_Turing_Aged_16.jpg/220px-Alan_Turing_Aged_16.jpg",
                new boolean[] {false, true, true, false, false, true, true}, new char[] {'s', 'f', 'g'}, "10", "I made computers");
        userCard.bio = "Hi I'm Allen and I'd love to go swmming with you some time. I like to go in the evenings around 9 pm. This is a lot of text to check if the scrolling works or not. We need more text. more and more text. more and more text. Ok maybe we're good now";
        loadUserAccount(userCard);

        token = getIntent().getExtras().getString("token");
        new RestClientTask().execute("http://bignybble.com:105/api/auth/me", "x-access-token", token);
        Log.d("DEBUG", "LOADED PROFILE");
    }

    public void onCheckboxClicked(View view) {

    }

    /**
     * Method to set the text, url, bio fields of the user's profile page.
     * @param card
     */
    private void loadUserAccount(Card card)
    {
        TextView dtv = (TextView)findViewById(R.id.descriptionTextView);
        dtv.setText(card.bio);

        TextView ntv = (TextView)findViewById(R.id.nameTextView);
        ntv.setText(card.name);

        ImageView profilePictureView = (ImageView)findViewById(R.id.profile_pic);
        new ProfileActivity.DownloadImageTask(profilePictureView).execute(card.URL);
        loadAvailability(card.schedule);
        loadInterests(card.interests);
    }

    /**
     * Method to put a check mark on the user's avaiable days. Based on the array received via JSON object from server.
     * @param availability
     */
    private void loadAvailability(boolean[] availability)
    {
        String checkBoxId = "checkbox_";
        for(int i=0; i<availability.length; i++)
        {

            checkBoxId = checkBoxId + daysOfWeek[i];
            CheckBox checkBox = (CheckBox)findViewById(getResources().getIdentifier(checkBoxId,"id", getPackageName()));
            checkBox.setChecked(availability[i]);
            checkBoxId = "checkbox_";

        }
    }

    private void loadInterests(char[] interests)
    {
        String activityImageId = "activityImage";
        String resrouceId = "ic_menu_";
        String [] activityList = {"football","soccer","dumbell","swimming","running"};

        for (int i=0; i<activityList.length; i++)
        {
            ImageView placeholderView = findViewById(getResources().getIdentifier(activityImageId+activityList[i],"id", getPackageName()));
            placeholderView.setImageResource(R.drawable.ic_menu_placeholder);
        }
        for (int i=0; i<interests.length; i++)
        {




            if(interests[i] == 'f'){
                activityImageId =  activityImageId+"football";
                resrouceId = resrouceId+"football";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            else if(interests[i] == 's'){
                activityImageId =  activityImageId+"soccer";
                resrouceId = resrouceId+"soccer";
                Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            else if(interests[i] == 'g'){
                activityImageId =  activityImageId+"dumbell";
                resrouceId = resrouceId+"dumbell";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            else if(interests[i] == 'w'){
                activityImageId =  activityImageId+"swimming";
                resrouceId = resrouceId+"swimming";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            else if(interests[i] == 'r'){
                activityImageId =  activityImageId+"running";
                resrouceId = resrouceId+"running";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

           ImageView imageView = findViewById(getResources().getIdentifier(activityImageId,"id", getPackageName()));
            int id = getResources().getIdentifier(resrouceId, "drawable", this.getPackageName());
            imageView.setImageResource(id);
            //imageView.setImageResource(R.drawable.ic_menu_football);


            activityImageId = "activityImage";
            resrouceId = "ic_menu_";
        }
    }

    public void launchEditProfileActivity(View view) {

        Intent startEdit = new Intent(this, EditProfileActivity.class);
        startEdit.putExtra("token", token);

        startActivity(startEdit);
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
            String results = client.makeGet(urls[0], urls[1], urls[2]);
            try {
                return results;
            } catch(Exception ex){
                Log.d("DEBUG", "Could not get JSON, " + ex.getLocalizedMessage());
            }
            return "[]"; //We do not need to return, but need String to use onPostExecute below.
        }

        @Override
        protected void onPostExecute(String result){
            try {
                userCard = CardTools.cardFromJson(new JSONObject(result));
                Log.d("USERCARD", "Card: " + userCard.name);
                loadUserAccount(userCard);
            } catch(Exception ex) {
                Log.d("FAIL", ex.getMessage());
            }

        }
    }
}
