package com.example.boone.app3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.tylersuehr.chips.ChipsInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import clarifai2.dto.prediction.Logo;

public class Clarifia {

    //ImageAI
    private Map<String, Map.Entry<Float, Integer>> Tags = new HashMap<>();
    private ChipsInputLayout chipsInputLayout;
    ViewDialog viewDialog;
    private AutoCompleteTextView autoCompleteTextView;
    private String[] banned_words = {"no person","business","technology","internet","indoors","office","information","",""};
    private MainActivity mainActivity;
    private ClarifaiClient client;


    public Clarifia(MainActivity ma){
        mainActivity = ma;

        viewDialog = new ViewDialog(mainActivity);
        client = new ClarifaiBuilder("6151337596614cd59034a09878d9bd62")
                .buildSync();
    }

    public void GetTags(final Bitmap bitmap) {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        final byte[] imageBytes = outStream.toByteArray();

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chipsInputLayout = mainActivity.findViewById(R.id.chips_input);
                chipsInputLayout.clearSelectedChips();
                chipsInputLayout.setInputHint("Loading...     ");
                viewDialog.showDialog();
            }
        });

        if (imageBytes != null) {
            new AsyncTask<Object, Object, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
                @Override
                protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Object... objects) {
                    return client.getDefaultModels().generalModel().predict().withInputs(ClarifaiInput.forImage(imageBytes)).executeSync();
                }

                @Override
                protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                    if (response.isSuccessful()) {
                        //asthetic
                        viewDialog.hideDialog();



                        List<Concept> concepts = response.get().get(0).data();

                        for (Concept concept : concepts) {
                            if (Tags.containsKey(concept.name())) {
                                int count = Tags.get(concept.name()).getValue();
                                float value = concept.value() - count / 5;
                                value = value * count + Tags.get(concept.name()).getKey();
                                count = Tags.get(concept.name()).getValue() + 1;
                                value = value / count + count / 5;
                                Tags.remove(concept.name());
                                Tags.put(concept.name(), new AbstractMap.SimpleEntry<Float, Integer>(value, count));

                            } else {
                                Tags.put(concept.name(), new AbstractMap.SimpleEntry<Float, Integer>(concept.value() + 1 / 10, 1));

                            }
                        }
                        Map<Float, String> sorted_tags = new TreeMap<>();

                        for (Map.Entry<String, Map.Entry<Float, Integer>> entry : Tags.entrySet()) {
                            if(!Arrays.asList(banned_words).contains(entry.getKey())){
                                sorted_tags.put(entry.getValue().getKey(), entry.getKey());
                            }
                        }

                        Log.d("testing_", sorted_tags.toString());

                        chipsInputLayout.setInputHint("Press \\'Done\\' to submit new tag      ......");

                        int number = 0;
                        for (Map.Entry<Float, String> entry : ((TreeMap<Float, String>) sorted_tags).descendingMap().entrySet()) {
                            if (number < 6) {
                                chipsInputLayout.addSelectedChip(new CoolChip(entry.getValue()));
                                number++;
                            }
                        }



                    } else {
                        Log.d("testing_", "Error status code: " + response.getStatus().statusCode());
                        Log.d("testing_", "Error description: " + response.getStatus().description());
                        if (response.getStatus().errorDetails() != null) {
                            Log.d("testing_", "Error details: " + response.getStatus().errorDetails());
                        }
                    }
                }
            }.execute();
            ;

        }
    }

    public void GetLogos(final Bitmap bitmap) {


        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        final byte[] imageBytes = outStream.toByteArray();

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                autoCompleteTextView = mainActivity.findViewById(R.id.Brand);
                autoCompleteTextView.setHint("Loading...");
                autoCompleteTextView.setText("");
                viewDialog.showDialog();
            }
        });


        if (imageBytes != null) {
            new AsyncTask<Object, Object, ClarifaiResponse<List<ClarifaiOutput<Logo>>>>() {
                @Override
                protected ClarifaiResponse<List<ClarifaiOutput<Logo>>> doInBackground(Object... objects) {

                    return client.getDefaultModels().logoModel().predict().withInputs(ClarifaiInput.forImage(imageBytes)).executeSync();
                }

                @Override
                protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Logo>>> response) {
                    viewDialog.hideDialog();

                    if (response.isSuccessful()) {
                        List<Logo> logos = response.get().get(0).data();
                        ArrayList<String> logo_array = new ArrayList<>();

                        for (Logo logo : logos) {
                            int number = 0;
                            for (Concept concept : logo.concepts()) {
                                if (number < 3 && concept.value() > 0.15) {
                                    Log.d("testing_", " " + concept.name() + " " + concept.value());
                                    logo_array.add(concept.name());
                                    number++;
                                }
                            }
                        }
                        Log.d("testing_", logo_array.toString());
                        autoCompleteTextView.setHint("Brand");
                        if (logo_array.size() > 0) {
                            autoCompleteTextView.setText(logo_array.get(0));
                            autoCompleteTextView.setAdapter(new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1, logo_array));
                        } else {
                            Toast.makeText(mainActivity, "Can't autofill brand name: no match in database", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Log.d("testing_", "Error status code: " + response.getStatus().statusCode());
                        Log.d("testing_", "Error description: " + response.getStatus().description());
                        if (response.getStatus().errorDetails() != null) {
                            Log.d("testing_", "Error details: " + response.getStatus().errorDetails());
                        }
                    }
                }
            }.execute();
            ;

        }
    }

}
