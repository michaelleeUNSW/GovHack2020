package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.input.SearchClause;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.SearchHit;
import clarifai2.dto.search.SearchInputsResult;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DeleteActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;
    ArrayList<Hit> arrayList = new ArrayList<Hit>();
    BookAdapter adapter;


    // the FAB that the user clicks to select an image
    View fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        arrayList.add( new Hit("a","a"));
        ListView listView = (ListView) findViewById(R.id.list);
        adapter = new BookAdapter(this, arrayList);
        listView.setAdapter(adapter);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImagePicked(v);
            }
        });

    }


    private void onImagePicked(View view) {
        Log.d("testing","onImagePicked");
        for (int i=0; i<=5; i++){
            arrayList.add( new Hit("a","a"));
        }
        adapter.notifyDataSetChanged();

    }






}
