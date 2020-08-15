package com.example.boone.app3;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Retail_Items {
    public String title;
    public String condition = "";
    public String conditionId = "";
    public String itemWebUrl;
    public String imageUrl = "";
    public String value;
    public String currency;


    public Retail_Items(JSONObject item) throws JSONException {
        title = item.getString("title");
        itemWebUrl = item.getString("itemWebUrl");
        if (item.has("condition")) {
            condition = " (" + item.getString("condition")+")";
        }
        if (item.has("conditionId")) {
            conditionId = item.getString("conditionId");
        }

        JSONObject image = item.getJSONObject("image");
        if (image.has("imageUrl")) {
            imageUrl = image.getString("imageUrl");
        }

        JSONObject price = item.getJSONObject("price");
        value = price.getString("value");
        currency = price.getString("currency");
    }

}
