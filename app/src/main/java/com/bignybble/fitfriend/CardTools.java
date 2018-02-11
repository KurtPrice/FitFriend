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

public class CardTools {
    private JSONArray jsonCards;


    /*
    * This will get the default number of cards (25) from the server and
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
            return new Card(json.getString("name"), json.getString("image"),
                    getBooleans(json.getJSONArray("schedule")), getInterests(json.getJSONArray("interests")),
                    json.getString("_id"), json.getString("bio"));

        } catch(JSONException ex){
            Log.d("DEBUG", "JSON ISSUE: " + ex.getMessage());
            return new Card("FUCK", "FUCK", new boolean[]{}, new char[]{}, "0", "FUCK");
        }
    }

    private static boolean[] getBooleans(JSONArray json){
        boolean[] schedule = new boolean[7];
        try {
            for (int i = 0; i < 7; i++) {
                int temp = json.getJSONObject(i).getInt("" + i);
                if(temp == 0){
                    schedule[i] = false;
                } else{
                    schedule[i] = true;
                }
            }
            return schedule;
        } catch(Exception ex){
            Log.d("DEBUG", "Failed to parse array of bools");
            return schedule;
        }
    }

    private static char[] getInterests(JSONArray json){
        char[] schedule = new char[7];
        try {
            for (int i = 0; i < 7; i++) {
                String temp = json.getJSONObject(i).getString("" + i);
                schedule[i] = temp.charAt(i);
            }
            return schedule;
        } catch(Exception ex){
            Log.d("DEBUG", "Failed to parse array of bools");
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
