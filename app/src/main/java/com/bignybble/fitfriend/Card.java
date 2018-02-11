package com.bignybble.fitfriend;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pricek21 on 2/10/18.
 */

public class Card {

    /*
    * Each card will represent the profile for a unique user.
    * URL will be the URL to the profile image on the server, schedule
    * will be an array of bools representing the day of the week the
    * person is available. True represents that the day is available and
    * False unavailable. index 0 is Sunday and index 6 is Saturday.
    * interests will be an array of characters representing 'f':football,
    * 's':soccer, etc. for us to present on the profile. uid is the unique
    * identifier of the user so we can keep track of the response we gave to
    * the server for accept or reject of that user.
    */

    public String name;
    public String URL;
    public boolean[] schedule;
    public char[] interests;
    public String uid;
    public String bio;
    private JSONArray jsonCards;

    public Card(String name, String URL, boolean[] schedule, char[] interests, String uid, String bio) {
        this.name = name;
        this.URL = URL;
        this.schedule = schedule;
        this.interests = interests;
        this.uid = uid;
        this.bio = bio;
    }
}