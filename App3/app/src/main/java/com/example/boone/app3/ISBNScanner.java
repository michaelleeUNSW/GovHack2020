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
import android.widget.Toast;

import com.google.zxing.Result;

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

public class ISBNScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;
    protected String prevBarcode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbnscanner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void stopCamera(){
        zXingScannerView.stopCamera();
    }

    public void writeToFile(String data)
    {

        // Get the directory for the user's public pictures directory.
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator  + "CircEx";
        // Create the folder.
        File folder = new File(path);
        if (!folder.exists()) {

            // Make it, if it doesn't exist
            if (folder.mkdirs()) {
                // Created DIR
                Log.i("INFO", "Log Directory Created Trying to Dump Logs");
            } else {
                // FAILED
                Log.e("INFO", "Error: Failed to Create Log Directory");
                return;
            }
        } else {
            Log.i("INFO", "Log Directory Exist Trying to Dump Logs");
        }

//        // Make sure the path directory exists.
//        if(!path.exists())
//        {
//            // Make it, if it doesn't exit
//            path.mkdirs();
//        }
        Log.d("INSIDE WRITE", path);

        final File file = new File(path, "ISBN_List.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.append("\n");

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    @Override
    public void handleResult(Result result) {
        onPause();
        try{
            if(result != null){
                String barcodeFormat = result.getBarcodeFormat().toString();
                // If ISBN
                if(result.getBarcodeFormat() != null && barcodeFormat.equalsIgnoreCase("EAN_13")){
                    final String barcodeValue = result.getText();

                    Toast.makeText(getApplicationContext(), barcodeValue,Toast.LENGTH_SHORT).show();
                    // String bookSearchString = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+barcodeValue+"&key=your_key";

                    if (prevBarcode != barcodeValue){
                        writeToFile(barcodeValue);

                        new GetBookInfo().execute(barcodeValue);

                        prevBarcode = barcodeValue;
                    }

                    stopCamera();
//                    Intent intent = new Intent(barcodeValue);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();

//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            zXingScannerView.resumeCameraPreview(ISBNScanner.this);
//                        }
//                    }, 1000);


                } else{
                    Toast.makeText(getApplicationContext(), "Invalid ISBN barcode",Toast.LENGTH_SHORT).show();
                    zXingScannerView.resumeCameraPreview(this);
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Not a valid scan!", Toast.LENGTH_SHORT).show();
                zXingScannerView.resumeCameraPreview(this);
            }

//            zXingScannerView.resumeCameraPreview(this);
//            setContentView(R.layout.activity_main);
//            stopCamera();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class GetBookInfo extends AsyncTask<String, Object, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... bookISBN) {
            // Stop if cancelled
            if(isCancelled()){
                return null;
            }
            Log.d(getClass().getName(), "ISBN String: " + bookISBN[0]);

            String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + bookISBN[0];

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
                    Toast.makeText(getApplicationContext(), "Book NOT Found!",Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject bookDetails = responseJson.getJSONArray("items").getJSONObject(0);
                    String title = bookDetails.getJSONObject("volumeInfo").getString("title");
                    Toast.makeText(getApplicationContext(), title,Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent("ISBN_Result");
                    intent.putExtra("bookTitle", title);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;

        }

    }
}
