package com.bignybble.fitfriend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pricek21 on 2/10/18.
 */

public class CardTools {
    private JSONArray jsonCards;


    /*
    * This will get the default number of cards (10) from the server and
    * return them in an array list for use in MatchActivity
    */
    public static ArrayList<Card> getCards(JSONArray jsonArray){
        ArrayList<Card> results = new ArrayList<>();

        //Populate JSONArray jsonCards, wait for task to finish
        Log.d("DEBUG", "COUNT " + jsonArray.toString());
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Card card = cardFromJson(json);
                results.add(card);
            }
        } catch(JSONException ex) {
            Log.e("JSON", "Could not parse JSON in getCards()");
        }

        return results;
    }

    public static Card cardFromJson(JSONObject json){
        try {
            boolean[] schedule = new boolean[7];
            schedule[0] = json.getBoolean("sun");
            schedule[1] = json.getBoolean("mon");
            schedule[2] = json.getBoolean("tue");
            schedule[3] = json.getBoolean("wed");
            schedule[4] = json.getBoolean("thu");
            schedule[5] = json.getBoolean("fri");
            schedule[6] = json.getBoolean("sat");
            return new Card(json.getString("name"), json.getString("image"), schedule
                    , getInterests(json.getJSONArray("interests")),
                    json.getString("_id"), json.getString("bio"));

        } catch(JSONException ex){
            Log.d("DEBUG", "JSON ISSUE: " + ex.getMessage());
            return new Card("FUCK", "FUCK", new boolean[]{}, new char[]{}, "0", "FUCK");
        }
    }

    private static char[] getInterests(JSONArray json){
        char[] schedule = new char[7];
        try {
            for (int i = 0; i < 7; i++) {
                String temp = json.getString(i);
                schedule[i] = temp.charAt(i);
            }
            return schedule;
        } catch(Exception ex){
            Log.d("DEBUG", "Failed to parse array of chars");
            return schedule;
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
