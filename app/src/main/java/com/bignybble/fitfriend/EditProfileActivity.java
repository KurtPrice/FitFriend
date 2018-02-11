package com.bignybble.fitfriend;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

public class EditProfileActivity extends NavigationDrawerActivity {
    private String userName;
    private String mEmail;
    private String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    // f-football, s-soccer, w-swimming, g-gym(weights), r-running.
    private char[] interestsList = {'f','s','w','g','r'};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_profile);
        super.onCreate(savedInstanceState);
        //mEmail = getIntent().getExtras().getString("email");
        //Toast.makeText(getApplicationContext(),"Hello :"+userName,Toast.LENGTH_LONG).show();

        String userToken = getIntent().getExtras().getString("token");
        new RestClientTask().execute("http:bignybble.com:105/api/auth/me", "x-access-token", userToken);
    }

    public void onCheckboxClicked(View view) {
    }

    public void launchImageSelector(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                //image_name_tv.setText(filePath);

                try {
                    if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                        //FINE
                        File imageFile = new File(filePath);


                        Bitmap mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                        //Bitmap mBitmap = BitmapFactory.decodeFile(imageFile.getCanonicalPath());
                        ImageView mImage = (ImageView) findViewById(R.id.uploadProlfilePicture);

                        Toast.makeText(getApplicationContext(),"Hello:"+imageFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
                        //mImage.setImageResource(R.drawable.ic_menu_running);
                         mImage.setImageBitmap(mBitmap);


                    } else {
                        //NOT IN REQUIRED FORMAT
                    }
                }catch (Exception e){

                }
            }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public boolean[] getAvailability()
    {
        String checkBoxId = "checkbox_";
        boolean[] schedule = new boolean[7];

        for (int i=0; i<daysOfWeek.length; i++)
        {
            checkBoxId = checkBoxId + daysOfWeek[i];
            CheckBox checkBox = (CheckBox)findViewById(getResources().getIdentifier(checkBoxId,"id", getPackageName()));
            schedule[i] = checkBox.isChecked();

            checkBoxId = "checkbox_";
        }
        return schedule;
    }

    public void setAvailability(boolean[] available)
    {
        String checkBoxId = "checkbox_";
        boolean[] schedule = new boolean[7];
        Toast.makeText(getApplicationContext(),"Hello :"+available.length,Toast.LENGTH_LONG).show();

        for (int i=0; i<available.length; i++)
        {
            checkBoxId = checkBoxId + daysOfWeek[i];

            CheckBox checkBox = (CheckBox)findViewById(getResources().getIdentifier(checkBoxId,"id", getPackageName()));
            Toast.makeText(getApplicationContext(),"Hello :"+checkBox,Toast.LENGTH_LONG).show();
            checkBox.setChecked(available[i]);
            checkBoxId = "checkbox_";
        }

    }
    public char[] getInterests()
    {
        String checkBoxId = "checkbox_";
        char[] mInterests = new char[5];

        for (int i=0; i<interestsList.length; i++)
        {
            checkBoxId = checkBoxId + interestsList[i];
            CheckBox checkBox = (CheckBox)findViewById(getResources().getIdentifier(checkBoxId,"id", getPackageName()));
            if(checkBox.isChecked())
            {
                mInterests[i] = interestsList[i];
            }
            checkBoxId = "checkbox_";
        }
        return mInterests;
    }
    public String getBio()
    {
        String editTextBio = "editTextBio";
        EditText editBio = (EditText) findViewById(getResources().getIdentifier(editTextBio,"id", getPackageName()));

        return  editBio.getText().toString();
    }

    public void loadInterests(char[] interests)
    {
        String activityImageId = "checkbox_";

        String [] activityList = {"football","soccer","dumbell","swimming","running"};

//        for (int i=0; i<activityList.length; i++)
//        {
//            ImageView placeholderView = findViewById(getResources().getIdentifier(activityImageId+activityList[i],"id", getPackageName()));
//            placeholderView.setImageResource(R.drawable.ic_menu_placeholder);
//        }
        for (int i=0; i<interests.length; i++)
        {

            if(interests[i] == 'f'){
                activityImageId =  activityImageId+"football";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            else if(interests[i] == 's'){
                activityImageId =  activityImageId+"soccer";
            }

            else if(interests[i] == 'g'){
                activityImageId =  activityImageId+"dumbell";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            else if(interests[i] == 'w'){
                activityImageId =  activityImageId+"swimming";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            else if(interests[i] == 'r'){
                activityImageId =  activityImageId+"running";
                //Toast.makeText(getApplicationContext(),"Hello:"+resrouceId,Toast.LENGTH_SHORT).show();
            }

            CheckBox checkBox = findViewById(getResources().getIdentifier(activityImageId,"id", getPackageName()));
            checkBox.setChecked(true);
            //imageView.setImageResource(R.drawable.ic_menu_football);


            activityImageId = "checkbox_";
        }
    }

    public String getImageUrl()
    {
        String imageUrl = "textViewImageUrl";
        EditText editImageUrl = (EditText) findViewById(getResources().getIdentifier(imageUrl,"id", getPackageName()));

        return editImageUrl.getText().toString();
    }
    /**
     * Package input into JSON and send to server.
     * @param view
     */
    public void saveChanges(View view) {

        Card card = new Card(userName,getImageUrl(),getAvailability(),getInterests(),getBio());
        CardTools cardTools = new CardTools();
        JSONObject json = cardTools.jsonFromCard(card);

        try {
            char[] bool = (char[])json.get("interests");
            Toast.makeText(getApplicationContext(),"Hello :"+card.name,Toast.LENGTH_LONG).show();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        new RestClientTask().execute("http://bignybble.com:105/api/auth/update/"+super.token,
                "name="+userName+"&url="+getImageUrl()+"&schedule="+getAvailability());
    }

    private void startProfileView(Card card)
    {
        Intent srartProfile = new Intent(this, ProfileActivity.class);
        //srartProfile.putExtra("name", user.name);
        //startEdit.putExtra("email", emailView.getText().toString());
        startActivity(srartProfile);
    }
    public void onInterestCheckboxClicked(View view) {
    }
    private class RestClientTask extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {
            RestClient client = new RestClient();
            return client.makeGet(urls[0], urls[1], urls[2]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                Card userCard = CardTools.cardFromJson(json);
                //populate edit page with returned card info

                Toast.makeText(getApplicationContext(),"Hello :"+userCard.schedule.length,Toast.LENGTH_LONG).show();
                loadInterests(userCard.interests);


                String checkBoxId = "checkbox_";
                boolean[] schedule = new boolean[7];


                for (int i=0; i<userCard.schedule.length; i++)
                {
                    checkBoxId = checkBoxId + daysOfWeek[i];

                    CheckBox checkBox = (CheckBox)findViewById(getResources().getIdentifier(checkBoxId,"id", getPackageName()));
                    Toast.makeText(getApplicationContext(),"Hello :"+checkBoxId,Toast.LENGTH_LONG).show();
                    checkBox.setChecked(userCard.schedule[i]);
                    checkBoxId = "checkbox_";
                }



                EditText editImageUrl = (EditText) findViewById(getResources().getIdentifier("textViewImageUrl","id", getPackageName()));
                editImageUrl.setText(userCard.URL);

                EditText editBio = (EditText) findViewById(getResources().getIdentifier("editTextBio","id", getPackageName()));
                editBio.setText(userCard.bio);
                //startProfileView(userCard);
            } catch (Exception ex) {
                Log.d("DEBUG", "how?: " + ex.getMessage());
            }
        }
    }
}
