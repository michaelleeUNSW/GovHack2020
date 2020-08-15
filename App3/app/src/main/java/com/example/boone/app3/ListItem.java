package com.example.boone.app3;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v4.app.ActivityCompat;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mindorks.paracamera.Camera;
import com.tylersuehr.chips.ChipsInputLayout;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListItem extends AppCompatActivity {

    private Button catBtn;
    private ImageButton camBtn;
    private ImageView imageView;
    private EditText priceField;
    private TextView testText;
    boolean selection = false;
    long mImageTakenTime;
    private Camera camera;
    private int n_o_images = 0;
    private LinearLayout linearLayout;
    private boolean logo;
    private Clarifia clarifia;
    public  ArrayList<String> bitmap_array = new ArrayList<>();
    private int FULL_SCREEN_RESULT=200;
    private int ISBN_SCANNER_RESULT_CODE = 100;

    static final String BASE_URL = "http://127.0.0.1:8000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("CircEx");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        // Activating category select popup
        catBtn = findViewById(R.id.categoryBtn);
        catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] categories = {"Antiques, Art & Collectables",
                        "Cars & Vehicles",
                        "Baby & Children"};



                // Making a selection
                final AlertDialog.Builder builder = new AlertDialog.Builder(ListItem.this);
                builder.setTitle("Select categories");
                builder.setSingleChoiceItems(categories, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        catBtn.setText(categories[i]);
                        selection = true;
                    }
                });

                // TODO: Prompt to tell user to make a selection first
                // Close category select popup once selection made else display prompt
                // telling user to first make a selection
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selection == false) {
                            //Toast.makeText(getApplicationContext(), "Please select a category",
                            //               Toast.LENGTH_SHORT).show();


                            //Toast toast = new Toast(getApplicationContext());
                            //toast.setDuration(Toast.LENGTH_LONG);
                            //toast.setView(R.layout.no_category_toast);
                            //toast.show();

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.no_category_toast,
                                                            (ViewGroup) findViewById(R.id.toast));
                            TextView toastText = layout.findViewById(R.id.toastText);
                            toastText.setText("Please select a category");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER, 0, 10);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    }
                });

                // Cancel category selection
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        camBtn = findViewById(R.id.bigImgButton);
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePictures();
            }
        });


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        BackendDatabaseApi dbApi = retrofit.create(BackendDatabaseApi.class);
//
//        String userName = "admin";
//        String password = "CircEx";
//
//        String base = userName + ":" + password;
//
//        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
//
//        Call<List<Book>> getBooks = dbApi.getBooks(authHeader);
//        getBooks.enqueue(new Callback<List<Book>>() {
//            @Override
//            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//                if(!response.isSuccessful()){
//                    Log.d("ERROR CODE:- ", "" + response.code());
//
//                    return;
//                }
//
//                List<Book> bookList = response.body();
//
//                Log.d("***** BOOK LIST ******", null);
//                for(Book book: bookList){
//                    Log.d("Book:- ", book.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Book>> call, Throwable t) {
//                Log.d("ERROR:- ", t.getMessage());
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    private void TakePictures(){
        mImageTakenTime = System.currentTimeMillis();

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(ListItem.this);
        try {

            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void AddImage(final String bitmap) {
        n_o_images++;

        linearLayout = findViewById(R.id.imgScroll);
        imageView = new ImageView(this);
        ScrollView cameraScroll = findViewById(R.id.picScroll);

        //imageView.setAdjustViewBounds(true);
        final float s = getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, (int) (s * 100));
        int margin = (int) s * 10;
        lp.setMargins(margin, margin, margin, margin);
        imageView.setLayoutParams(lp);

        Bitmap bitmap_decoded = ImageProcessing.decodeFile(bitmap);
        imageView.setImageBitmap(bitmap_decoded);

        linearLayout.addView(imageView, linearLayout.getChildCount() - 1);
        cameraScroll.fullScroll(View.FOCUS_RIGHT);

        bitmap_array.add(bitmap);
        Log.d("StringPath", bitmap);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListItem.this, FullScreenImage.class);
                intent.putExtra("bitmap", bitmap);
                intent.putExtra("index", 1);
                startActivityForResult(intent, FULL_SCREEN_RESULT);

            }
        });
    }

    // ???
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            //Get Image
            String bitmap = null;
            try {
                bitmap = camera.getCameraBitmapPath();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                AddImage(bitmap);
                //clear_added_gallery_image();
                if(n_o_images >2){
                    Toast.makeText(this, "Check if the autogenerated info are correct", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "The other text boxes don't have to filled yet", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Press \"NEXT\" if you are ready", Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode== FULL_SCREEN_RESULT && resultCode == RESULT_OK){
            //RemoveImage(data.getStringExtra("Bitmap"));
        }
        if (requestCode == ISBN_SCANNER_RESULT_CODE && resultCode == Activity.RESULT_OK){
//            Log.d("DATA ######", data.toString());
            EditText bookTitle = findViewById(R.id.productTitle);

            bookTitle.setText(data.getStringExtra("bookTitle"));
        }
//        Log.d("REQUEST CODE********", requestCode + " --- " + resultCode + "^^^" + Activity.RESULT_OK);
    }

    public void scanCode(View view) {
        // Activity to call the scanner
        Intent intent = new Intent(ListItem.this, ISBNScanner.class);

        startActivityForResult(intent, ISBN_SCANNER_RESULT_CODE);
    }

}
