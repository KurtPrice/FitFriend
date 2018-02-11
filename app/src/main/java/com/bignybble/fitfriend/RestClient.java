package com.bignybble.fitfriend;

import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pricek21 on 2/10/18.
 */

public class RestClient {

    /* Currently Deprecated in lew of makeGet.
     * This method could still be used to easily
     * integrate the messaging features of the backend
     * with more time. --Kurtpr
     */
    public String makeRequest(String urls) {
        try {
            URL url = new URL(urls);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch(Exception ex){
            Log.d("DEBUG", ex.getMessage());
        }
        return "";
    }

    /* data will need to be in the form of key=value&key2=value2
     * Used for API interactions that add new entries, such as
     * users, to the database. --Kurtpr
     */
    public String makePost(String urls, String data){
        //TODO: URLEncode data
        try{
            URL url = new URL(urls);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            /* Create output stream and write our data out */
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(data);
            outputStream.flush();
            outputStream.close();

            urlConnection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();

        } catch(MalformedURLException ex){
            Log.d("DEBUG", "Make post: " + ex.getMessage());
        } catch(IOException ex){
            Log.d("DEBUG", "IOException: " + ex.getLocalizedMessage());
        }
        return null;
    }

    /* Used to get data that is read only for our user. urls or data will usually
     * include use of the userToken to read this information --Kurtpr
     */
    public String makeGet(String urls, String header, String data){
        //TODO: URLEncode data
        try{
            URL url = new URL(urls);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty(header, data);
            urlConnection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch(IOException ex){
            Log.d("DEBUG", ex.getMessage());
        }
        return "";
    }

    /* Updates current entries in the database that belongs to the
     * current user. --Kurtpr
     */
    public String makePut(String urls, String data){
        //Todo: URLEncode data
        try{
            URL url = new URL(urls);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "x-www-form-urlencoded");

            /* Create output stream and write our data out */
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(data);
            outputStream.flush();
            outputStream.close();

            urlConnection.getResponseCode();


        } catch(MalformedURLException ex){
            Log.d("DEBUG", "Make post: " + ex.getMessage());
        } catch(IOException ex){
            Log.d("DEBUG", "IOException: " + ex.getLocalizedMessage());
        }
        return null;
    }

}
