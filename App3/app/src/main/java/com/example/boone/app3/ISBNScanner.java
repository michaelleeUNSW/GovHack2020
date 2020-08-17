package com.example.boone.app3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ISBNScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;
    private String barcodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbnscanner);
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

    @Override
    public void handleResult(Result result) {
        onPause();
        try{
            if(result != null){
                String barcodeFormat = result.getBarcodeFormat().toString();
                // If ISBN
                if(result.getBarcodeFormat() != null && barcodeFormat.equalsIgnoreCase("EAN_13")){
                    barcodeValue = result.getText();
                    Toast.makeText(getApplicationContext(), barcodeValue,Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "Invalid ISBN barcode",Toast.LENGTH_SHORT).show();
                    zXingScannerView.resumeCameraPreview(this);
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Not a valid scan!", Toast.LENGTH_SHORT).show();
                zXingScannerView.resumeCameraPreview(this);
            }
            Intent intent = new Intent("ISBN_Result");
            intent.putExtra("ISBN", barcodeValue);
            setResult(Activity.RESULT_OK, intent);
            stopCamera();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
