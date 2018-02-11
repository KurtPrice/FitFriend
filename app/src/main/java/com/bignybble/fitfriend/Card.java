package com.bignybble.fitfriend;

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
    public int uid;
    public String bio;

    public Card(String name, String URL, boolean[] schedule, char[] interests, int uid, String bio){
        this.name = name;
        this.URL = URL;
        this.schedule = schedule;
        this.interests = interests;
        this.uid = uid;
        this.bio = bio;
    }

    /*
    * This will get the default number of cards (25) from the server and
    * return them in an array list for use in MatchActivity
    */
    public static ArrayList<Card> getCard(){
        ArrayList<Card> results = new ArrayList<>();

        results.add(new Card("George Lucas", "http://www.filmdumpster.com/wp-content/uploads/2015/12/george-lucas_0.jpg",
                new boolean[] {false, true, true, false, false, true, true}, new char[] {'s', 'f', 'g'}, 10,
                "I like to take photos and work on making amateur films."));
        results.add(new Card("Jessica Alba", "https://www.biography.com/.image/t_share/MTE4MDAzNDEwMzY0NDMzOTM0/jessica-alba-299896-1-402.jpg",
                new boolean[] {true, true, false, false, true, false, false}, new char[] {'f', 'g'}, 11,
                "Looking for a partner to do nascar racing!"));
        results.add(new Card("Sundar Pichai", "https://pctechmag.com/wp-content/uploads/2018/01/Sundar-Pichai.jpg",
                new boolean[] {true, false, true, false, true, false, true}, new char[] {'f', 'r'}, 12,
                "Since I started my new job I have a lot of extra time on my hands and would like to start" +
                        "working out with a partner again!"));

        return results;
    }

    public Card cardFromJson(JSONObject json){
        try {
            return new Card(json.getString("name"), json.getString("URL"),
                    (boolean[])json.get("schedule"), (char[])json.get("interests"), json.getInt("uid"),
                    json.getString("bio"));

        } catch(JSONException ex){
            return null;
        }
    }

    public JSONObject jsonFromCard(Card card){
        JSONObject json = new JSONObject();
        /* If a JSONException occurs, we do not want to
         * use a potentially corrupt or partially completed
         * JSONObject, so we will return an empty JSONObject. --Kurtpr
         */
        try {

            json.put("name", card.name);
            json.put("URL", card.URL);
            json.put("schedule", card.schedule);
            json.put("interests", card.interests);
            json.put("uid", card.uid);
            json.put("bio", card.bio);

            return json;
        } catch(JSONException ex){
            return new JSONObject();
        }
    }
}
