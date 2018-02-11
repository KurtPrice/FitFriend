package com.bignybble.fitfriend;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

public class EditProfileActivity extends AppCompatActivity {
    private String userName;
    private String mEmail;
    private String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    // f-football, s-soccer, w-swimming, g-gym(weights), r-running.
    private char[] interestsList = {'f','s','w','g','r'};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        userName = getIntent().getExtras().getString("name");
        mEmail = getIntent().getExtras().getString("email");
        //Toast.makeText(getApplicationContext(),"Hello :"+userName,Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(),"Hello :"+bool[4],Toast.LENGTH_LONG).show();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onInterestCheckboxClicked(View view) {
    }
}
