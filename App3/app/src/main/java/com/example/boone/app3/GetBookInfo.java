package com.example.boone.app3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class GetBookInfo extends AsyncTask<String, Object, JSONObject> {
    private String barcodeValue;
    private ListingActivity currActivity;

    public GetBookInfo(ListingActivity curr, String bar){
        currActivity = curr;
        barcodeValue = bar;
    }

    protected JSONObject doInBackground(String... bookISBN) {
        // Stop if cancelled
        if(isCancelled()){
            return null;
        }
        Log.d(getClass().getName(), "ISBN String: " + barcodeValue);

        String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" +barcodeValue;

        try{
            HttpURLConnection connection = null;
            // Build Connection.
            try{
                URL url = new URL(apiUrlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000); // 5 seconds
                connection.setConnectTimeout(5000); // 5 seconds
            } catch (MalformedURLException e) {
                // Impossible: The only two URLs used in the app are taken from string resources.
                e.printStackTrace();
            } catch (ProtocolException e) {
                // Impossible: "GET" is a perfectly valid request method.
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            Log.d(getClass().getName(), "Response CODE: " + responseCode);
            if(responseCode != 200){
                Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                connection.disconnect();
                return null;
            }

            // Read data from response.
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = responseReader.readLine();
            while (line != null){
                builder.append(line);
                line = responseReader.readLine();
            }
            String responseString = builder.toString();
//                Log.d(getClass().getName(), "Response String: " + responseString);
            JSONObject responseJson = new JSONObject(responseString);

            // Close connection and return response code.
            connection.disconnect();
            return responseJson;
        } catch (SocketTimeoutException e) {
            Log.w(getClass().getName(), "Connection timed out. Returning null");
            return null;
        } catch(IOException e){
            Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        } catch (Exception e){
            Log.d(getClass().getName(), "Exception Occurred: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject responseJson) {

        if(isCancelled() || responseJson == null){
            // Request was cancelled due to no network connection.
            return;
        } else{
            responseJson.toString();
        }

        try {
            if(responseJson.getInt("totalItems") < 1){
                Toast("Book NOT Found!",Toast.LENGTH_SHORT);
            } else {
                JSONObject bookDetails = responseJson.getJSONArray("items").getJSONObject(0);
                String Name = bookDetails.getJSONObject("volumeInfo").getString("title");
                String Description = bookDetails.getJSONObject("volumeInfo").getString("description");
                String ImageUrl = bookDetails.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");

                EditText name = (EditText) currActivity.findViewById(R.id.name);
                EditText description = (EditText) currActivity.findViewById(R.id.description);
                ImageView imageView = (ImageView) currActivity.findViewById(R.id.coverImage);
                if (Name != null) {name.setText(Name);};
                if (Description != null) {description.setText(Description);};
                if (ImageUrl != null) {
                    ImageUrl = ImageUrl.replace("http://", "https://");
                    Picasso.get().load(ImageUrl).fit().into(imageView);
                    currActivity.imageUrl = ImageUrl;
                }
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;

    }
    private void Toast(final String message, final int duration){
        currActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(currActivity, message,duration).show();
            }
        });
    }

}