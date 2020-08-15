package com.example.boone.app3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Ebay_Authorisation_Code_API {

    private static final String clientId = "MichaelL-Testing-SBX-8393f3dea-33a18be8";//clientId
    private static final String clientSecret = "SBX-393f3dea6b06-b32b-4b7e-9b5e-c8f2";//client secret
    private static final String auth = clientId + ":" + clientSecret;
    private static final String authentication = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
    private static final String RuName = "Michael_Lee-MichaelL-Testin-lcfyfuf";
    private static final String STATE = "MY_RANDOM_STRING_1";
    private OkHttpClient client = new OkHttpClient.Builder()
         .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private int Try;
    private MainActivity mainActivity;

    public Ebay_Authorisation_Code_API(MainActivity mA) {
        mainActivity = mA;
    }

    public void startSignIn(int T) {
        Try = T;
        prefs = mainActivity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getLong("ClientExpire", 0) - 300 * 1000 > System.currentTimeMillis() && Try == 0&&prefs.contains("ClientToken")) {
            Sell_item(prefs.getString("ClientToken",null)); //TODO refresh
        } else if (Try == 1 || Try == 0) {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("auth.sandbox.ebay.com")
                    .appendPath("oauth2")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", clientId)
                    .appendQueryParameter("redirect_uri", RuName)
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("state", STATE)
                    .appendQueryParameter("scope", "https://api.ebay.com/oauth/api_scope https://api.ebay.com/oauth/api_scope/sell.inventory https://api.ebay.com/oauth/api_scope/sell.account")
                    .appendQueryParameter("prompt", "login");
            String myUrl = builder.build().toString();

            Log.d("testing", Uri.parse(myUrl).toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myUrl));
            mainActivity.startActivity(intent);
        }
    }

    public void ResumeCode(Intent intent) {
        if (intent != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            Log.d("Testing_return_url", uri.toString());

            if (uri.getQueryParameter("error") != null) {
                String error = uri.getQueryParameter("error");
                Log.e("Testing_", "An error has occurred : " + error);
            } else {
                String state = uri.getQueryParameter("state");
                if (state.equals(STATE)) {
                    String code = uri.getQueryParameter("code");
                    Log.d("Testing_code", code);
                    ExchangeAuthorisationForToken(code);
                }
            }
        }
    }

    public void ExchangeAuthorisationForToken(final String code) {
        new client_call(code) {
            @Override
            void OnResponse(JSONObject data, Response response) {
                Log.d("testing_EAC",data.toString());
                prefs = mainActivity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                editor = prefs.edit();
               String accessToken = data.optString("access_token");
                int  access_expires = data.optInt("expires_in");
                 editor.putLong("ClientExpire",System.currentTimeMillis()+access_expires*1000);
                editor.putString("ClientToken",accessToken);

                String refreshToken = data.optString("refresh_token");
                int  refresh_expires = data.optInt("refresh_token_expires_in");
                editor.putLong("RefreshExpire",System.currentTimeMillis()+refresh_expires*1000);
                editor.putString("RefreshToken",refreshToken);
                editor.apply();
                Sell_item(accessToken);

            }

            @Override
            public Request.Builder authorisation(Request.Builder RB) throws JSONException {
                return RB
                    .addHeader("Authorization", "Basic " + authentication)
                    .url("https://api.sandbox.ebay.com/identity/v1/oauth2/token")
                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                            "grant_type=authorization_code&code=" + code +
                                    "&redirect_uri=" + RuName));

            }
        };
    }

    public void Sell_item(String accessToken) {
    //    createInventoryLocation(accessToken);
        createInventoryItem(accessToken);

    }

    public void createInventoryLocation(final String accessToken) {
        Toast("createInventoryLocation");
        new client_call(accessToken) {

            @Override
            public Request.Builder JsonRequest(Request.Builder RB) throws JSONException {
                JSONObject jsonObject = new JSONObject();
                JSONObject location = new JSONObject();
                JSONObject address = new JSONObject();
                address.put("addressLine1", "2055 Hamilton Ave");
                address.put("addressLine2", "Building 3");
                address.put("city", "San Jose");
                address.put("stateOrProvince", "CA");
                address.put("postalCode", "95125");
                address.put("country", "US");
                location.put("address", address);
                jsonObject.put("location", location);
                jsonObject.put("locationInstructions", "Items ship from here.");
                jsonObject.put("name", "Warehouse-1");

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                return RB.url("https://api.sandbox.ebay.com/sell/inventory/v1/location/Freers002")
                        .post(body);
            }

            @Override
            void OnResponse(JSONObject data, Response response) {
                Log.d("testing_EAC",response.toString());
            }
        };

    }

    public void createInventoryItem(final String accessToken) {
        Toast("createInventoryItem");
        new client_call(accessToken) {

            @Override
            public Request.Builder JsonRequest(Request.Builder RB) throws JSONException {
                JSONObject jsonObject = new JSONObject();
                JSONObject availability = new JSONObject();
                JSONObject shipToLocationAvailability = new JSONObject();

                shipToLocationAvailability.put("quantity", 503);
                availability.put("shipToLocationAvailability", shipToLocationAvailability);
                jsonObject.put("availability", availability);

                JSONObject product = new JSONObject();
                product.put("title","Water bottle3");
                product.put("description","New GoPro Hero4 Helmet Cam. Unopened box.3");
                jsonObject.put("product", product);

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Toast("building request");
                return RB.url("https://api.sandbox.ebay.com/sell/inventory/v1/inventory_item/water-bottle4")
                        .put(body);
            }

            @Override
            void OnResponse(JSONObject data, Response response) {
                Log.d("testing_01",response.toString());
                createOffer(accessToken);

            }
        };

    }
    public void createOffer(String accessToken) {
        new client_call(accessToken) {

            @Override
            public Request.Builder JsonRequest(Request.Builder RB) throws JSONException {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sku", "water-bottle4");
                jsonObject.put("marketplaceId", "EBAY_US");
                jsonObject.put("format", "FIXED_PRICE");
                jsonObject.put("categoryId", "30120");
                JSONObject policy = new JSONObject();
                policy.put("fulfillmentPolicyId", "6039235000");
                policy.put("paymentPolicyId", "6039233000");
                policy.put("returnPolicyId", "6039234000");
                jsonObject.put("listingPolicies",policy);
                JSONObject pricingSummary = new JSONObject();
                JSONObject price = new JSONObject();
                price.put("currency", "USD");
                price.put("value", "272.1");
                pricingSummary.put("price",price);
                jsonObject.put("pricingSummary",pricingSummary);
                jsonObject.put("merchantLocationKey","Freers001");

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());


                return RB.url("https://api.sandbox.ebay.com/sell/inventory/v1/offer")
                        .post(body);
            }

            @Override
            void OnResponse(JSONObject data, Response response) {
                Log.d("testing_02", response.toString());

                try {
                    String json = response.body().string();

                    data = new JSONObject(json);



                    Log.d("testing_03",data.optString("offerId"));


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

    }
    public void publishOffer(String accessToken) {
        new client_call(accessToken) {

            @Override
            public Request.Builder JsonRequest(Request.Builder RB) throws JSONException {

                return RB.url("https://api.sandbox.ebay.com/sell/inventory/v1/offer/6668323010/publish")
                        .method("POST",RequestBody.create(null, ""));
            }

            @Override
            void OnResponse(JSONObject data, Response response) {

            }
        };

    }
    private void Toast(final String message){
        Log.d("testing_Toast",message);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show();

            }
        });
    }
}
