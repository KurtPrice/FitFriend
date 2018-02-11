package com.bignybble.fitfriend;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pricek21 on 2/10/18.
 */

public class RestClient {

    public String makeRequest(String urls) {
        try {
            //TLSSocketFactory objTlsSocketFactory = new TLSSocketFactory();
            URL url = new URL(urls);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setSSLSocketFactory(objTlsSocketFactory);

            int responseCode = urlConnection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            Log.d("JSON RESULTS", response.toString());
            return response.toString();
        } catch(Exception ex){
            Log.d("FUCK", ex.getMessage());
        }
        return "";
    }
}
