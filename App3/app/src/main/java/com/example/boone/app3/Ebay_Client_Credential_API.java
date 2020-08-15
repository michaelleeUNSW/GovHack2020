package com.example.boone.app3;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Ebay_Client_Credential_API {


    private ArrayList<ArrayList<Retail_Items>> arrayList_new = new ArrayList<>();

    private ArrayList<Retail_Items> arrayList_used = new ArrayList<>();
    public ArrayList<String> tags = new ArrayList<>();

    private MainActivity mainActivity;

    public  void setUpRecycleView(){

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Retail_Items> recycle_view_array = new ArrayList<>();
                for(ArrayList<Retail_Items> sub_array:arrayList_new){
                    for(Retail_Items retail_items:sub_array){
                        recycle_view_array.add(retail_items);
                    }
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(mainActivity, recycle_view_array);
                RecyclerView recyclerView = mainActivity.findViewById(R.id.OnlineItemsScroll);
                recyclerView.setAdapter(adapter);


            }
        });
    }

    public void update_sub_array_size(){
        int target = 15;
        sub_array_size = (int)Math.ceil(target/(arrayList_new.size()+1));

        for( int i=0; i<arrayList_new.size(); i++){
            ArrayList<Retail_Items> sub_array = arrayList_new.get(i);
            ArrayList<Retail_Items> sub_array_sliced = new ArrayList<Retail_Items>(sub_array.subList(0,sub_array_size));
            arrayList_new.set(i,sub_array_sliced);
        }
    }
}
